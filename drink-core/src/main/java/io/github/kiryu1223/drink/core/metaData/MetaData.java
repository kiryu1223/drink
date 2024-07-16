package io.github.kiryu1223.drink.core.metaData;

import io.github.kiryu1223.drink.annotation.Column;
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
import java.util.*;

public class MetaData
{
    private final Map<String, PropertyMetaData> Columns = new HashMap<>();
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
            Columns.put(property, new PropertyMetaData(property, columnStr, descriptor.getReadMethod(), descriptor.getWriteMethod(), field.getType(), converter));
        }
    }

    public Map<String, PropertyMetaData> getColumns()
    {
        return Columns;
    }

    public PropertyMetaData getPropertyMetaData(String key)
    {
        return Columns.get(key);
    }

    private List<Field> getAllFields(Class<?> clazz)
    {
        List<Field> fields = new ArrayList<>();
        if (!clazz.isAnonymousClass())
        {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null)
            {
                List<Field> allFields0 = getAllFields(superClass);
                fields.addAll(allFields0);
            }
        }
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        return fields;
    }

    public PropertyDescriptor[] propertyDescriptors(Class<?> c)
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

    public String getColumnNameByPropertyName(String property)
    {
        return Columns.get(property).getColumn();
    }

    public PropertyMetaData getPropertyMetaDataByColumnName(String asName)
    {
        return Columns.values().stream().filter(f -> f.getColumn().equals(asName)).findFirst().get();
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
        return Columns.values().stream().filter(f -> f.getGetter().equals(getter)).findFirst().get().getColumn();
    }

    public String getColumnNameBySetter(Method setter)
    {
        return Columns.values().stream().filter(f -> f.getSetter().equals(setter)).findFirst().get().getColumn();
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
