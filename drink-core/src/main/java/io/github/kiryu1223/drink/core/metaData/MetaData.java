package io.github.kiryu1223.drink.core.metaData;

import io.github.kiryu1223.drink.annotation.Column;
import io.github.kiryu1223.drink.annotation.IgnoreColumn;
import io.github.kiryu1223.drink.annotation.Navigate;
import io.github.kiryu1223.drink.annotation.Table;
import io.github.kiryu1223.drink.core.builder.ConverterCache;
import io.github.kiryu1223.drink.ext.IConverter;
import io.github.kiryu1223.drink.ext.NoConverter;
import io.github.kiryu1223.expressionTree.util.ReflectUtil;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class MetaData
{
    private final List<PropertyMetaData> propertys = new ArrayList<>();
    private final Class<?> type;
    private final String tableName;

    public MetaData(Class<?> type)
    {
        this.type = type;
        Table table = type.getAnnotation(Table.class);
        this.tableName = (table == null || table.value().isEmpty()) ? type.getSimpleName() : table.value();
        for (PropertyDescriptor descriptor : propertyDescriptors(type))
        {
            String property = descriptor.getName();
            Field field = ReflectUtil.getField(type, property);
            Column column = field.getAnnotation(Column.class);
            String columnStr = (column == null || column.value().isEmpty()) ? property : column.value();
            IConverter<?, ?> converter = column == null ? ConverterCache.get(NoConverter.class) : ConverterCache.get(column.converter());
            NavigateData navigateData = null;
            Navigate navigate = field.getAnnotation(Navigate.class);
            if (navigate != null)
            {
                Class<?> navigateTargetType;
                if (Collection.class.isAssignableFrom(field.getType()))
                {
                    Type genericType = field.getGenericType();
                    navigateTargetType = (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
                    navigateData = new NavigateData(navigate, navigateTargetType, (Class<? extends Collection<?>>) field.getType());
                }
                else
                {
                    navigateTargetType = field.getType();
                    navigateData = new NavigateData(navigate, navigateTargetType,null);
                }
            }
            boolean ignoreColumn = field.getAnnotation(IgnoreColumn.class) != null || navigateData != null;
            propertys.add(new PropertyMetaData(property, columnStr, descriptor.getReadMethod(), descriptor.getWriteMethod(), field, converter, ignoreColumn, navigateData));
        }
    }

    private PropertyDescriptor[] propertyDescriptors(Class<?> c)
    {
        try
        {
            BeanInfo beanInfo = Introspector.getBeanInfo(c, Object.class);
            return beanInfo.getPropertyDescriptors();
        } catch (IntrospectionException e)
        {
            throw new RuntimeException(e);
        }
    }

    public List<PropertyMetaData> getPropertys()
    {
        return propertys;
    }

    public List<PropertyMetaData> getNotIgnorePropertys()
    {
        return propertys.stream().filter(f -> !f.isIgnoreColumn()).collect(Collectors.toList());
    }

    public PropertyMetaData getPropertyMetaData(String key)
    {
        return propertys.stream().filter(f -> f.getProperty().equals(key)).findFirst().get();
    }

    public PropertyMetaData getPropertyMetaDataByColumnName(String asName)
    {
        return propertys.stream().filter(f -> f.getColumn().equals(asName)).findFirst().get();
    }

    public PropertyMetaData getPropertyMetaDataByGetter(Method getter)
    {
        return getPropertyMetaDataByColumnName(getColumnNameByGetter(getter));
    }

    public PropertyMetaData getPropertyMetaDataBySetter(Method setter)
    {
        return getPropertyMetaDataByColumnName(getColumnNameBySetter(setter));
    }

    public String getColumnNameByGetter(Method getter)
    {
        return propertys.stream().filter(f -> f.getGetter().equals(getter)).findFirst().get().getColumn();
    }

    public String getColumnNameBySetter(Method setter)
    {
        return propertys.stream().filter(f -> f.getSetter().equals(setter)).findFirst().get().getColumn();
    }

    public Class<?> getType()
    {
        return type;
    }

    public String getTableName()
    {
        return tableName;
    }
}
