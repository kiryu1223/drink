/*
 * Copyright 2017-2024 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.kiryu1223.drink.base.metaData;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.annotation.*;
import io.github.kiryu1223.drink.base.converter.NameConverter;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;
import io.github.kiryu1223.drink.base.toBean.handler.TypeHandlerManager;
import io.github.kiryu1223.drink.base.util.DrinkUtil;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.kiryu1223.drink.base.util.DrinkUtil.cast;


/**
 * 类型元数据
 *
 * @author kiryu1223
 * @since 3.0
 */
public class MetaData
{
    /**
     * 字段列表
     */
    private final List<FieldMetaData> fields = new ArrayList<>();
    /**
     * 类型
     */
    private final Class<?> type;
    /**
     * 构造函器
     */
    private final Constructor<?> constructor;
    /**
     * 表名
     */
    private final String tableName;
    /**
     * 模式
     */
    private final String schema;
    /**
     * 是否为空from查询表
     */
    private final boolean isEmptyTable;

    public MetaData(Class<?> type, IConfig config)
    {
        NameConverter nameConverter = config.getNameConverter();
        try
        {
            this.type = type;
            this.constructor = (!type.isAnonymousClass() && !type.isInterface()) ? type.getConstructor() : null;
            Table table = type.getAnnotation(Table.class);
            this.tableName = hasTableName(table) ? table.value() : nameConverter.convertTableName(type.isAnonymousClass() ? type.getSuperclass().getSimpleName() : type.getSimpleName());
            this.schema = table == null ? "" : table.schema();
            this.isEmptyTable = type.isAnnotationPresent(EmptyTable.class);
            if (type.isInterface()) return;
            for (PropertyDescriptor descriptor : propertyDescriptors(type))
            {
                String fieldName = descriptor.getName();
                Field field = DrinkUtil.findField(type, fieldName);
                Column column = field.getAnnotation(Column.class);
                UseTypeHandler useTypeHandler = field.getAnnotation(UseTypeHandler.class);
                IgnoreColumn ignoreColumn = field.getAnnotation(IgnoreColumn.class);
                Navigate navigate = field.getAnnotation(Navigate.class);

                boolean notNull;
                String columnName;
                Method getter = descriptor.getReadMethod();
                Method setter = descriptor.getWriteMethod();
                ITypeHandler<?> typeHandler;
                boolean isIgnoreColumn = ignoreColumn != null;
                NavigateData navigateData;
                boolean isPrimaryKey;
                boolean isGeneratedKey;
                if (column != null)
                {
                    String value = column.value();
                    if (DrinkUtil.isEmpty(value))
                    {
                        value = nameConverter.convertFieldName(fieldName);
                    }
                    columnName=value;
                    notNull = column.notNull();
                    isPrimaryKey = column.primaryKey();
                    isGeneratedKey = column.generatedKey();
                }
                else
                {
                    columnName = nameConverter.convertFieldName(fieldName);
                    notNull = false;
                    isPrimaryKey = false;
                    isGeneratedKey = false;
                }
                if (useTypeHandler != null)
                {
                    typeHandler = TypeHandlerManager.getByHandlerType(cast(useTypeHandler.value()));
                }
                else
                {
                    typeHandler = TypeHandlerManager.get(field.getGenericType());
                }
                if (navigate != null)
                {
                    Class<?> navigateTargetType;
                    if (Collection.class.isAssignableFrom(field.getType()))
                    {
                        Class<? extends Collection<?>> collectionType = (Class<? extends Collection<?>>) field.getType();
                        Type genericType = field.getGenericType();
                        if (genericType instanceof Class<?>)
                        {
                            Class<?> aClass = navigate.targetType();
                            if (aClass != Empty.class)
                            {
                                navigateTargetType = aClass;
                                navigateData = new NavigateData(navigate, navigateTargetType, collectionType);
                            }
                            else
                            {
                                throw new RuntimeException("匿名类字段上的@Navigate注解的targetType不能为空:" + field);
                            }
                        }
                        else
                        {
                            navigateTargetType = DrinkUtil.getTargetType(genericType);
                            navigateData = new NavigateData(navigate, navigateTargetType, collectionType);
                        }
                    }
                    else
                    {
                        navigateTargetType = field.getType();
                        navigateData = new NavigateData(navigate, navigateTargetType, null);
                    }
                }
                else
                {
                    navigateData = null;
                }
                FieldMetaData fieldMetaData = new FieldMetaData(notNull, fieldName, columnName, getter, setter, field, typeHandler, isIgnoreColumn, navigateData, isPrimaryKey, isGeneratedKey);
                fields.add(fieldMetaData);
            }
        }
        catch (NoSuchMethodException e)
        {

            throw new RuntimeException(e);
        }
    }

    private boolean hasTableName(Table table)
    {
        return table != null && !table.value().isEmpty();
    }

    private PropertyDescriptor[] propertyDescriptors(Class<?> c)
    {
        try
        {
            BeanInfo beanInfo = Introspector.getBeanInfo(c, Object.class);
            return beanInfo.getPropertyDescriptors();
        }
        catch (IntrospectionException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取所有字段
     */
    public List<FieldMetaData> getFields()
    {
        return fields;
    }

    /**
     * 获取所有非忽略字段
     */
    public List<FieldMetaData> getNotIgnoreAndNavigateFields()
    {
        return fields.stream().filter(f -> !f.isIgnoreColumn() && !f.hasNavigate()).collect(Collectors.toList());
    }

    public List<FieldMetaData> getOnInsertOrUpdateFields() {
        return fields.stream()
                // 首先不是忽略字段
                // 其次不是导航字段
                // 最后是主键字段或者不是数据库生成值的字段
                .filter(f -> !f.isIgnoreColumn()&&!f.hasNavigate() && (f.isPrimaryKey()||!f.isGeneratedKey()))
                .collect(Collectors.toList());
    }

    /**
     * 根据字段名获取字段元数据
     *
     * @param key 字段名
     */
    public FieldMetaData getFieldMetaDataByFieldName(String key)
    {
        return fields.stream().filter(f -> f.getFieldName().equals(key)).findFirst().orElseThrow(() -> new RuntimeException(key));
    }

    /**
     * 根据列名获取字段元数据
     *
     * @param columnName 列名
     */
    public FieldMetaData getFieldMetaDataByColumnName(String columnName)
    {
        return fields.stream().filter(f -> f.getColumn().equals(columnName)).findFirst().orElseThrow(() -> new RuntimeException(columnName));
    }

    /**
     * 根据getter获取字段元数据
     *
     * @param getter getter方法
     */
    public FieldMetaData getFieldMetaDataByGetter(Method getter)
    {
        return fields.stream().filter(f -> f.getGetter().equals(getter)).findFirst().orElseThrow(() -> new RuntimeException(getter.toGenericString()));
    }

    public FieldMetaData getFieldMetaDataByGetterName(String getter)
    {
        return fields.stream().filter(f -> f.getGetter().getName().equals(getter)).findFirst().orElseThrow(() -> new RuntimeException(getter));
    }

    /**
     * 根据setter获取字段元数据
     *
     * @param setter setter方法
     */
    public FieldMetaData getFieldMetaDataBySetter(Method setter)
    {
        return fields.stream().filter(f -> f.getSetter().equals(setter)).findFirst().orElseThrow(() -> new RuntimeException(setter.toGenericString()));
    }

    /**
     * 根据getter获取列名
     *
     * @param getter getter方法
     */
    public String getColumnNameByGetter(Method getter)
    {
        return fields.stream().filter(f -> f.getGetter().equals(getter)).findFirst().orElseThrow(() -> new RuntimeException(getter.toGenericString())).getColumn();
    }

    /**
     * 根据setter获取列名
     *
     * @param setter setter方法
     */
    public String getColumnNameBySetter(Method setter)
    {
        return fields.stream().filter(f -> f.getSetter().equals(setter)).findFirst().orElseThrow(() -> new RuntimeException(setter.toGenericString())).getColumn();
    }

    /**
     * 获取主键的字段元数据
     */
    public FieldMetaData getPrimary()
    {
        return fields.stream().filter(f -> f.isPrimaryKey()).findFirst().orElseThrow(() -> new RuntimeException(type + "找不到主键"));
    }

    public List<FieldMetaData> getPrimaryList()
    {
        return fields.stream().filter(f -> f.isPrimaryKey()).collect(Collectors.toList());
    }

    public FieldMetaData getGeneratedKey()
    {
        return fields.stream().filter(f -> f.isGeneratedKey()).findAny().orElseThrow(() -> new RuntimeException(type + "找不到自增键"));
    }

    /**
     * 获取实体类型
     */
    public Class<?> getType()
    {
        return type;
    }

    /**
     * 获取表名
     */
    public String getTableName()
    {
        return tableName;
    }

    /**
     * 获取模式
     */
    public String getSchema()
    {
        return schema;
    }

    /**
     * 是否为空from表
     */
    public boolean isEmptyTable()
    {
        return isEmptyTable;
    }

    /**
     * 获取构造函器
     */
    public Constructor<?> getConstructor()
    {
        return constructor;
    }

    public FieldMetaData getChildrenField()
    {
        return fields.stream().filter(f -> f.hasNavigate() && f.getNavigateData().getNavigateTargetType() == type).findAny().orElseThrow(() -> new DrinkException(String.format("%s类找不到配置了导航属性的子字段", type)));
    }
}
