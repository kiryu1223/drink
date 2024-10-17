package io.github.kiryu1223.drink.core.builder;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;
import io.github.kiryu1223.drink.ext.IConverter;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.upperClass;

public class ObjectBuilder<T>
{
    private final ResultSet resultSet;
    private final Class<T> target;
    private final List<PropertyMetaData> propertyMetaDataList;
    private final boolean isSingle;
    private final Config config;
    private final IResultSetValueGetter valueGetter;

    public static <T> ObjectBuilder<T> start(ResultSet resultSet, Class<T> target, List<PropertyMetaData> propertyMetaDataList, boolean isSingle, Config config)
    {
        return new ObjectBuilder<>(resultSet, target, propertyMetaDataList, isSingle, config);
    }

    private ObjectBuilder(ResultSet resultSet, Class<T> target, List<PropertyMetaData> propertyMetaDataList, boolean isSingle, Config config)
    {
        this.resultSet = resultSet;
        this.target = target;
        this.propertyMetaDataList = propertyMetaDataList;
        this.isSingle = isSingle;
        this.config = config;
        this.valueGetter = config.getValueGetter();
    }

    public <Key> Map<Key, T> createMap(String column) throws SQLException, NoSuchFieldException, IllegalAccessException, InvocationTargetException
    {
        AbsBeanCreator<T> beanCreator = config.getFastCreatorFactory().get(target);
        Supplier<T> creator = beanCreator.getBeanCreator();
        Map<String, Integer> indexMap = getIndexMap();
        Map<Key, T> hashMap = new HashMap<>();
        while (resultSet.next())
        {
            T t = creator.get();
            Key key = null;
            for (PropertyMetaData metaData : propertyMetaDataList)
            {
                Object value = convertValue(metaData, indexMap.get(metaData.getColumn()));
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
        AbsBeanCreator<T> beanCreator = config.getFastCreatorFactory().get(target);
        Supplier<T> creator = beanCreator.getBeanCreator();
        Map<String, Integer> indexMap = getIndexMap();
        // System.out.println(indexMap);
        Map<Key, List<T>> hashMap = new HashMap<>();
        while (resultSet.next())
        {
            T t = creator.get();
            Key key = null;
            for (PropertyMetaData metaData : propertyMetaDataList)
            {
                String column = metaData.getColumn();
                //System.out.println(column);
                Object value = convertValue(metaData, indexMap.get(column));
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
        AbsBeanCreator<T> beanCreator = config.getFastCreatorFactory().get(target);
        Supplier<T> creator = beanCreator.getBeanCreator();
        Map<String, Integer> indexMap = getIndexMap();
        int anotherKeyIndex = indexMap.get(anotherKeyColumn.getColumn());
        Map<Key, List<T>> hashMap = new HashMap<>();
        while (resultSet.next())
        {
            T t = creator.get();
            //Key key = resultSet.getObject(anotherKeyColumn.getColumn(), (Class<? extends Key>) upperClass(anotherKeyColumn.getType()));
            Key key = (Key) convertValue(anotherKeyColumn, anotherKeyIndex);
            for (PropertyMetaData metaData : propertyMetaDataList)
            {
                Object value = convertValue(metaData, indexMap.get(metaData.getColumn()));
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
            T t = (T) valueGetter.getValueByIndex(resultSet, 1, target);
            list.add(t);
        }
        return list;
    }

    private List<T> getClassList() throws SQLException, NoSuchFieldException, IllegalAccessException, InvocationTargetException
    {
        AbsBeanCreator<T> beanCreator = config.getFastCreatorFactory().get(target);
        Supplier<T> creator = beanCreator.getBeanCreator();
        Map<String, Integer> indexMap = getIndexMap();
        List<T> list = new ArrayList<>();
        while (resultSet.next())
        {
            T t = creator.get();
            for (PropertyMetaData metaData : propertyMetaDataList)
            {
                Object value = convertValue(metaData, indexMap.get(metaData.getColumn()));
                if (value != null)
                {
                    ISetterCaller<T> beanSetter = beanCreator.getBeanSetter(metaData.getProperty());
                    beanSetter.call(t, value);
                    //metaData.getSetter().invoke(t, value);
                }
            }
            list.add(t);
        }

        return list;
    }

    private Map<String, Integer> getIndexMap() throws SQLException
    {
        Map<String, Integer> indexMap = new HashMap<>();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++)
        {
            String columnLabel = resultSetMetaData.getColumnLabel(i);
            indexMap.put(columnLabel, i);
        }
        return indexMap;
    }

    private Object convertValue(PropertyMetaData metaData, int index) throws SQLException, NoSuchFieldException, IllegalAccessException
    {
        Class<?> type;
        if (metaData.hasConverter())
        {
            type = metaData.getConverter().getDbType();
            Object value = valueGetter.getValueByIndex(resultSet, index, type);
            IConverter<?, ?> converter = metaData.getConverter();
            return converter.toJava(cast(value), metaData);
        }
        else
        {
            type = metaData.getType();
            return valueGetter.getValueByIndex(resultSet, index, type);
        }
    }

//    private T defConvertValue() throws SQLException, NoSuchFieldException, IllegalAccessException
//    {
//        if (target.isEnum())
//        {
//            String Enum = resultSet.getString(1);
//            return (T) target.getField(Enum).get(null);
//        }
//        else if (target == Character.class)
//        {
//            String result = resultSet.getString(1);
//            return (result != null && !result.isEmpty()) ? cast(result.charAt(0)) : null;
//        }
//        else
//        {
//            return resultSet.getObject(1, target);
//        }
//    }

    private <R> R cast(Object o)
    {
        return (R) o;
    }
}
