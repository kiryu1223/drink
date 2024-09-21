package io.github.kiryu1223.drink.core.builder;

import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;
import io.github.kiryu1223.drink.ext.IConverter;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ObjectBuilder<T>
{
    private final ResultSet resultSet;
    private final Class<T> target;
    private final List<PropertyMetaData> propertyMetaDataList;
    private final boolean isSingle;

    public static <T> ObjectBuilder<T> start(ResultSet resultSet, Class<T> target, List<PropertyMetaData> propertyMetaDataList, boolean isSingle)
    {
        return new ObjectBuilder<>(resultSet, target, propertyMetaDataList, isSingle);
    }

    private ObjectBuilder(ResultSet resultSet, Class<T> target, List<PropertyMetaData> propertyMetaDataList, boolean isSingle)
    {
        this.resultSet = resultSet;
        this.target = target;
        this.propertyMetaDataList = propertyMetaDataList;
        this.isSingle = isSingle;
    }

    public <Key> Map<Key, T> createMap(String column) throws SQLException, NoSuchFieldException, IllegalAccessException, InvocationTargetException
    {
        FastCreator<T> fastCreator = new FastCreator<>(target);
        Supplier<T> creator = fastCreator.getCreator();
        Map<Key, T> hashMap = new HashMap<>();
        while (resultSet.next())
        {
            T t = creator.get();
            Key key = null;
            for (PropertyMetaData metaData : propertyMetaDataList)
            {
                Object value = convertValue(metaData);
                if (column.equals(metaData.getColumn()))
                {
                    key = (Key) value;
                }
                if (value != null) metaData.getSetter().invoke(t, value);
            }
            if (key != null) hashMap.put(key, t);
        }
        return hashMap;
    }

    public <Key> Map<Key, List<T>> createMapList(String keyColumn) throws SQLException, NoSuchFieldException, IllegalAccessException, InvocationTargetException
    {
        FastCreator<T> fastCreator = new FastCreator<>(target);
        Supplier<T> creator = fastCreator.getCreator();
        Map<Key, List<T>> hashMap = new HashMap<>();
        while (resultSet.next())
        {
            T t = creator.get();
            Key key = null;
            for (PropertyMetaData metaData : propertyMetaDataList)
            {
                Object value = convertValue(metaData);
                if (keyColumn.equals(metaData.getColumn()))
                {
                    key = (Key) value;
                }
                if (value != null) metaData.getSetter().invoke(t, value);
            }
            if (key != null)
            {
                if (!hashMap.containsKey(key))
                {
                    List<T> tempList = new ArrayList<>();
                    tempList.add(t);
                    hashMap.put(key, tempList);
                }
                else
                {
                    hashMap.get(key).add(t);
                }
            }
        }
        return hashMap;
    }

    public <Key> Map<Key, List<T>> createMapListByAnotherKey(PropertyMetaData anotherKeyColumn) throws SQLException, NoSuchFieldException, IllegalAccessException, InvocationTargetException
    {
        FastCreator<T> fastCreator = new FastCreator<>(target);
        Supplier<T> creator = fastCreator.getCreator();
        Map<Key, List<T>> hashMap = new HashMap<>();
        while (resultSet.next())
        {
            T t = creator.get();
            Key key = resultSet.getObject(anotherKeyColumn.getColumn(), (Class<? extends Key>) upperClass(anotherKeyColumn.getType()));
            for (PropertyMetaData metaData : propertyMetaDataList)
            {
                Object value = convertValue(metaData);
                if (value != null) metaData.getSetter().invoke(t, value);
//                if (anotherKeyColumn.equals(metaData.getColumn()))
//                {
//                    key = (Key) value;
//                }
//                else
//                {
//                    metaData.getSetter().invoke(t, value);
//                }
            }
            if (!hashMap.containsKey(key))
            {
                List<T> tempList = new ArrayList<>();
                tempList.add(t);
                hashMap.put(key, tempList);
            }
            else
            {
                hashMap.get(key).add(t);
            }
        }
        return hashMap;
    }

    public List<T> createList() throws SQLException, NoSuchFieldException, IllegalAccessException, InvocationTargetException
    {
        if (isSingle)
        {
            return getSingleList();
        }
        else
        {
            return getClassList();
        }
    }

    private List<T> getSingleList() throws SQLException, NoSuchFieldException, IllegalAccessException
    {
        List<T> list = new ArrayList<>();
        while (resultSet.next())
        {
            T t = defConvertValue();
            list.add(t);
        }
        return list;
    }

    private List<T> getClassList() throws SQLException, NoSuchFieldException, IllegalAccessException, InvocationTargetException
    {
        FastCreator<T> fastCreator = new FastCreator<>(target);
        Supplier<T> creator = fastCreator.getCreator();
        List<T> list = new ArrayList<>();
        while (resultSet.next())
        {
            T t = creator.get();
            for (PropertyMetaData metaData : propertyMetaDataList)
            {
                Object value = convertValue(metaData);
                if (value != null) metaData.getSetter().invoke(t, value);
            }
            list.add(t);
        }
        return list;
    }

    private Object convertValue(PropertyMetaData metaData) throws SQLException, NoSuchFieldException, IllegalAccessException
    {
        if (!metaData.hasConverter())
        {
            Class<?> type = metaData.getType();
            if (type.isEnum())
            {
                String Enum = resultSet.getString(metaData.getColumn());
                return type.getField(Enum).get(null);
            }
            else if (type == char.class || type == Character.class)
            {
                String result = resultSet.getString(metaData.getColumn());
                return (result != null && !result.isEmpty()) ? result.charAt(0) : null;
            }
            else
            {
                return resultSet.getObject(metaData.getColumn(), upperClass(type));
            }
        }
        else
        {
            IConverter<?, ?> converter = metaData.getConverter();
            Object temp = resultSet.getObject(metaData.getColumn(), converter.getDbType());
            return converter.toJava(cast(temp), metaData);
        }
    }

    private T defConvertValue() throws SQLException, NoSuchFieldException, IllegalAccessException
    {
        if (target.isEnum())
        {
            String Enum = resultSet.getString(1);
            return (T) target.getField(Enum).get(null);
        }
        else if (target == Character.class)
        {
            String result = resultSet.getString(1);
            return (result != null && !result.isEmpty()) ? cast(result.charAt(0)) : null;
        }
        else
        {
            return resultSet.getObject(1, target);
        }
    }

    private <R> R cast(Object o)
    {
        return (R) o;
    }

    private Class<?> upperClass(Class<?> c)
    {
        if (c.isPrimitive())
        {
            if (c == Byte.TYPE)
            {
                return Byte.class;
            }
            else if (c == Short.TYPE)
            {
                return Short.class;
            }
            else if (c == Integer.TYPE)
            {
                return Integer.class;
            }
            else if (c == Long.TYPE)
            {
                return Long.class;
            }
            else if (c == Float.TYPE)
            {
                return Float.class;
            }
            else if (c == Double.TYPE)
            {
                return Double.class;
            }
            else if (c == Boolean.TYPE)
            {
                return Boolean.class;
            }
            else
            {
                return Void.class;
            }
        }
        else
        {
            return c;
        }
    }
}
