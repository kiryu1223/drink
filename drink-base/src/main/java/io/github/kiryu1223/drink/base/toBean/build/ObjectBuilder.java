package io.github.kiryu1223.drink.base.toBean.build;


import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ExKey;
import io.github.kiryu1223.drink.base.expression.ExKeys;
import io.github.kiryu1223.drink.base.expression.ExValue;
import io.github.kiryu1223.drink.base.expression.ExValues;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.toBean.beancreator.AbsBeanCreator;
import io.github.kiryu1223.drink.base.toBean.beancreator.ISetterCaller;
import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;
import io.github.kiryu1223.drink.base.toBean.handler.TypeHandlerManager;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Supplier;

public class ObjectBuilder<T> {
    private final ResultSet resultSet;
    private final Class<T> target;
    private final List<FieldMetaData> propertyMetaDataList;
    private final boolean isSingle;
    private final IConfig config;
    private final ITypeHandler<T> singleTypeHandler;

    public static <T> ObjectBuilder<T> start(ResultSet resultSet, Class<T> target, List<FieldMetaData> propertyMetaDataList, boolean isSingle, IConfig config) {
        return start(resultSet, target, propertyMetaDataList, isSingle, config, null);
    }

    public static <T> ObjectBuilder<T> start(ResultSet resultSet, Class<T> target, List<FieldMetaData> propertyMetaDataList, boolean isSingle, IConfig config, ITypeHandler<T> singleTypeHandler) {
        return new ObjectBuilder<>(resultSet, target, propertyMetaDataList, isSingle, config, singleTypeHandler);
    }

    private ObjectBuilder(ResultSet resultSet, Class<T> target, List<FieldMetaData> propertyMetaDataList, boolean isSingle, IConfig config, ITypeHandler<T> singleTypeHandler) {
        this.resultSet = resultSet;
        this.target = target;
        this.propertyMetaDataList = propertyMetaDataList;
        this.isSingle = isSingle;
        this.config = config;
        //this.valueGetter = config.getValueGetter();
        this.singleTypeHandler = singleTypeHandler;
    }

    public <Key> Map<Key, T> createMap(String column) throws SQLException, IllegalAccessException, InvocationTargetException {
        AbsBeanCreator<T> beanCreator = config.getBeanCreatorFactory().get(target, config);
        Supplier<T> creator = beanCreator.getBeanCreator();
        Map<String, Integer> indexMap = getIndexMap();
        Map<Key, T> hashMap = new HashMap<>();
        while (resultSet.next()) {
            T t = creator.get();
            Key key = null;
            for (FieldMetaData metaData : propertyMetaDataList) {
                Object value = convertValue(metaData, indexMap.get(metaData.getColumn()));
                if (column.equals(metaData.getColumn())) {
                    key = (Key) value;
                }
                if (value != null) metaData.getSetter().invoke(t, value);
            }
            if (key != null) hashMap.put(key, t);
        }
        return hashMap;
    }

    public <Key> Map<Key, List<T>> createMapList(String keyColumn) throws SQLException, IllegalAccessException, InvocationTargetException {
        AbsBeanCreator<T> beanCreator = config.getBeanCreatorFactory().get(target, config);
        Supplier<T> creator = beanCreator.getBeanCreator();
        Map<String, Integer> indexMap = getIndexMap();
        // System.out.println(indexMap);
        Map<Key, List<T>> hashMap = new HashMap<>();
        while (resultSet.next()) {
            T t = creator.get();
            Key key = null;
            for (FieldMetaData metaData : propertyMetaDataList) {
                String column = metaData.getColumn();
                //System.out.println(column);
                Object value = convertValue(metaData, indexMap.get(column));
                if (keyColumn.equals(metaData.getColumn())) {
                    key = (Key) value;
                }
                if (value != null) metaData.getSetter().invoke(t, value);
            }
            if (key != null) {
                if (!hashMap.containsKey(key)) {
                    List<T> tempList = new ArrayList<>();
                    tempList.add(t);
                    hashMap.put(key, tempList);
                }
                else {
                    hashMap.get(key).add(t);
                }
            }
        }
        return hashMap;
    }

    public <Key> Map<Key, List<T>> createMapListByAnotherKey(FieldMetaData mappingKey, String mappingKeyName) throws SQLException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        AbsBeanCreator<T> beanCreator = config.getBeanCreatorFactory().get(target, config);
        Supplier<T> creator = beanCreator.getBeanCreator();
        Map<String, Integer> indexMap = getIndexMap();
        int anotherKeyIndex = indexMap.get(mappingKeyName);
        Map<Key, List<T>> map = new HashMap<>();
        while (resultSet.next()) {
            T t = creator.get();
            Key key = (Key) convertValue(mappingKey, anotherKeyIndex);
            foreach(beanCreator, indexMap, t);
            List<T> list = map.computeIfAbsent(key, k -> new ArrayList<>());
            list.add(t);
        }
        return map;
    }

    private void foreach(AbsBeanCreator<T> beanCreator, Map<String, Integer> indexMap, T t) throws SQLException, InvocationTargetException, IllegalAccessException {
        for (FieldMetaData metaData : propertyMetaDataList) {
            Integer index = indexMap.get(metaData.getFieldName());
            if (index != null) {
                Object value = convertValue(metaData, index);
                if (value != null) {
                    ISetterCaller<T> setter = beanCreator.getBeanSetter(metaData.getFieldName());
                    setter.call(t, value);
                }
            }
        }
    }

    public List<T> createList() throws SQLException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        return createList(null, null);
    }

    public List<T> createList(ExValues exValues, ExKeys exKeys) throws SQLException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        if (isSingle) {
            return getSingleList();
        }
        else {
            return getClassList(exValues, exKeys);
        }
    }

    private List<T> getSingleList() throws SQLException, NoSuchFieldException, IllegalAccessException {
        List<T> list = new ArrayList<>();
        ITypeHandler<T> useTypeHandler = singleTypeHandler;
        if (useTypeHandler == null) {
            useTypeHandler = TypeHandlerManager.get(target);
        }
        while (resultSet.next()) {
            T t = useTypeHandler.getValue(resultSet, 1, target);
            list.add(t);
        }
        return list;
    }

    private List<T> getClassList(ExValues exValues, ExKeys exKeys) throws SQLException, IllegalAccessException, InvocationTargetException {
        AbsBeanCreator<T> beanCreator = config.getBeanCreatorFactory().get(target, config);
        Supplier<T> creator = beanCreator.getBeanCreator();
        Map<String, Integer> indexMap = getIndexMap();
        List<T> list = new ArrayList<>();
        while (resultSet.next()) {
            T t = creator.get();
            foreach(beanCreator, indexMap, t);
            if (exValues != null) {
                for (Map.Entry<String, ExValue> entry : exValues.getExValueMap().entrySet()) {
                    String key = entry.getKey();
                    ExValue value = entry.getValue();
                    int index = indexMap.get(key);
                    Object o = convertValue(value.getFieldMetaData(), index);
                    value.addValue(o);
                }
            }
            if (exKeys != null) {
                List<Object> hashValueList = new ArrayList<>();
                for (ExKey exKey : exKeys.getExKeys()) {
                    int index = indexMap.get(exKey.getKeyName());
                    Object o = convertValue(exKey.getFieldMetaData(), index);
                    hashValueList.add(o);
                }
                int hashCode = Arrays.hashCode(hashValueList.toArray());
                exKeys.addValue(hashCode, t);
            }
            list.add(t);
        }
        return list;
    }

    private Map<String, Integer> getIndexMap() throws SQLException {
        Map<String, Integer> indexMap = new HashMap<>();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
            String columnLabel = resultSetMetaData.getColumnLabel(i);
            indexMap.put(columnLabel, i);
        }
        return indexMap;
    }

    private Object convertValue(FieldMetaData metaData, int index) throws SQLException {
        ITypeHandler<?> typeHandler = metaData.getTypeHandler();
        return typeHandler.getValue(resultSet, index, metaData.getGenericType());
    }
}
