package io.github.kiryu1223.drink.core.builder;

import io.github.kiryu1223.drink.annotation.Column;
import io.github.kiryu1223.expressionTree.util.ReflectUtil;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;

public class MetaData
{
    private final Map<String, PropertyMetaData> Columns = new HashMap<>();

    public MetaData(Class<?> type)
    {
        for (PropertyDescriptor descriptor : propertyDescriptors(type))
        {
            String property = descriptor.getName();
            Field field = ReflectUtil.getField(type, property);
            Column column = field.getAnnotation(Column.class);
            String columnStr = column == null ? property : column.value();
            Columns.put(property,new PropertyMetaData(property, columnStr, descriptor.getReadMethod(), descriptor.getWriteMethod()));
        }

//        for (Field field : getAllFields(type))
//        {
//            if (Modifier.isTransient(field.getModifiers())
//                    || Modifier.isStatic(field.getModifiers())
//                    || Modifier.isFinal(field.getModifiers()))
//            {
//                continue;
//            }
//            String name = field.getName();
//            Column column = field.getAnnotation(Column.class);
//            if (column == null || column.value().isEmpty())
//            {
//                Columns.put(name, field);
//            }
//            else
//            {
//                Columns.put(column.value(), field);
//            }
//        }
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
}
