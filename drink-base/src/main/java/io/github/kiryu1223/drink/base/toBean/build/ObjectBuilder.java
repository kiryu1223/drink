package io.github.kiryu1223.drink.base.toBean.build;


import io.github.kiryu1223.drink.base.IConfig;
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
    private final List<FieldMetaData> fieldMetaDataList;
    private final boolean isSingle;
    private final IConfig config;
    private final ITypeHandler<T> singleTypeHandler;
    private final int fetchSize;

    public static <T> ObjectBuilder<T> start(ResultSet resultSet, Class<T> target, List<FieldMetaData> propertyMetaDataList, boolean isSingle, IConfig config) {
        return start(resultSet, target, propertyMetaDataList, isSingle, config, null);
    }

    public static <T> ObjectBuilder<T> start(ResultSet resultSet, Class<T> target, List<FieldMetaData> propertyMetaDataList, boolean isSingle, IConfig config, ITypeHandler<T> singleTypeHandler) {
        return start(resultSet, target, propertyMetaDataList, isSingle, config, singleTypeHandler, 0);
    }

    public static <T> ObjectBuilder<T> start(ResultSet resultSet, Class<T> target, List<FieldMetaData> propertyMetaDataList, boolean isSingle, IConfig config, ITypeHandler<T> singleTypeHandler, int fetchSize) {
        return new ObjectBuilder<>(resultSet, target, propertyMetaDataList, isSingle, config, singleTypeHandler, fetchSize);
    }

    private ObjectBuilder(ResultSet resultSet, Class<T> target, List<FieldMetaData> fieldMetaDataList, boolean isSingle, IConfig config, ITypeHandler<T> singleTypeHandler, int fetchSize) {
        this.resultSet = resultSet;
        this.target = target;
        this.fieldMetaDataList = fieldMetaDataList;
        this.isSingle = isSingle;
        this.config = config;
        //this.valueGetter = config.getValueGetter();
        this.singleTypeHandler = singleTypeHandler;
        this.fetchSize = fetchSize;
    }

    public <Key> Map<Key, T> createMap(String column) throws SQLException, IllegalAccessException, InvocationTargetException {
        AbsBeanCreator<T> beanCreator = config.getBeanCreatorFactory().get(target);
        Supplier<T> creator = beanCreator.getBeanCreator();
        Map<String, Integer> indexMap = getIndexMap();
        Map<Key, T> hashMap = new HashMap<>();
        while (resultSet.next()) {
            T t = creator.get();
            Key key = null;
            for (FieldMetaData metaData : fieldMetaDataList) {
                Object value = convertValue(metaData, indexMap.get(metaData.getColumnName()));
                if (column.equals(metaData.getColumnName())) {
                    key = (Key) value;
                }
                if (value != null) metaData.getSetter().invoke(t, value);
            }
            if (key != null) hashMap.put(key, t);
        }
        return hashMap;
    }

    public <Key> Map<Key, List<T>> createMapList(String keyColumn) throws SQLException, IllegalAccessException, InvocationTargetException {
        AbsBeanCreator<T> beanCreator = config.getBeanCreatorFactory().get(target);
        Supplier<T> creator = beanCreator.getBeanCreator();
        Map<String, Integer> indexMap = getIndexMap();
        // System.out.println(indexMap);
        Map<Key, List<T>> hashMap = new HashMap<>();
        while (resultSet.next()) {
            T t = creator.get();
            Key key = null;
            for (FieldMetaData metaData : fieldMetaDataList) {
                String column = metaData.getColumnName();
                //System.out.println(column);
                Object value = convertValue(metaData, indexMap.get(column));
                if (keyColumn.equals(metaData.getColumnName())) {
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
        AbsBeanCreator<T> beanCreator = config.getBeanCreatorFactory().get(target);
        Supplier<T> creator = beanCreator.getBeanCreator();
        Map<String, Integer> indexMap = getIndexMap();
        int[] indexes = getIndexes(indexMap);
        int anotherKeyIndex = indexMap.get(mappingKeyName);
        Map<Key, List<T>> map = new HashMap<>();
        while (resultSet.next()) {
            T t = creator.get();
            Key key = (Key) convertValue(mappingKey, anotherKeyIndex);
            foreach(beanCreator, indexes, t);
            List<T> list = map.computeIfAbsent(key, k -> new ArrayList<>());
            list.add(t);
        }
        return map;
    }

    private void foreach(AbsBeanCreator<T> beanCreator, int[] indexes, T t) throws SQLException, InvocationTargetException, IllegalAccessException {
        int offset = 0;
        for (FieldMetaData metaData : fieldMetaDataList) {
            int index = indexes[offset++];
            if (index > 0) {
                Object value = convertValue(metaData, index);
                if (value != null) {
                    ISetterCaller<T> setter = beanCreator.getBeanSetter(metaData.getFieldName());
                    setter.call(t, value);
                }
            }
        }
    }

    public JdbcResult<T> createList() throws SQLException, NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        return createList(null);
    }

    public JdbcResult<T> createList(ExValues exValues) throws SQLException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        if (isSingle) {
            return getSingleList();
        }
        else {
            return getClassList(exValues);
        }
    }

    private JdbcResult<T> getSingleList() throws SQLException, NoSuchFieldException, IllegalAccessException {
        JdbcResult<T> result = new JdbcResult<>();
        ITypeHandler<T> useTypeHandler = singleTypeHandler;
        if (useTypeHandler == null) {
            useTypeHandler = TypeHandlerManager.get(target);
        }
        while (resultSet.next()) {
            T t = useTypeHandler.getValue(resultSet, 1, target);
            result.addResult(t);
        }
        return result;
    }

    private JdbcResult<T> getClassList(ExValues exValues) throws SQLException, IllegalAccessException, InvocationTargetException {
        AbsBeanCreator<T> beanCreator = config.getBeanCreatorFactory().get(target);
        Supplier<T> creator = beanCreator.getBeanCreator();
        Map<String, Integer> indexMap = getIndexMap();
        int[] indexes = getIndexes(indexMap);
        JdbcResult<T> jdbcResult = new JdbcResult<>();
        if (exValues == null) {
            if (fetchSize > 0) {
                for (int i = 0; i < fetchSize; i++) {
                    onNotHasExValues(creator, beanCreator, indexes, jdbcResult);
                }
            }
            else {
                onNotHasExValues(creator, beanCreator, indexes, jdbcResult);
            }
        }
        else {
            if (fetchSize > 0) {
                for (int i = 0; i < fetchSize; i++) {
                    onHasExValues(exValues, jdbcResult, creator, beanCreator, indexMap,indexes);
                }
            }
            else {
                onHasExValues(exValues, jdbcResult, creator, beanCreator, indexMap,indexes);
            }
        }
        return jdbcResult;
    }

    private void onHasExValues(ExValues exValues, JdbcResult<T> jdbcResult, Supplier<T> creator, AbsBeanCreator<T> beanCreator, Map<String, Integer> indexMap,int[] indexes) throws SQLException, InvocationTargetException, IllegalAccessException {
        List<ExtensionObject> extensionValueResult = jdbcResult.getExtensionValueResult();
        List<ExtensionObject> extensionKeyResult = jdbcResult.getExtensionKeyResult();
        while (resultSet.next()) {
            T t = creator.get();
            foreach(beanCreator, indexes, t);
            Map<String, ExtensionField> extensionValueFieldMap = new HashMap<>();
            Map<String, ExtensionField> extensionKeyFieldMap = new HashMap<>();
            for (Map.Entry<String, FieldMetaData> entry : exValues.getExValueMap().entrySet()) {
                String key = entry.getKey();
                FieldMetaData fieldMetaData = entry.getValue();
                Object o = convertValue(fieldMetaData, indexMap.get(key));
                extensionValueFieldMap.put(key, new ExtensionField(fieldMetaData, o));
            }
            for (Map.Entry<String, FieldMetaData> entry : exValues.getExKeyMap().entrySet()) {
                String key = entry.getKey();
                FieldMetaData fieldMetaData = entry.getValue();
                Object o = convertValue(fieldMetaData, indexMap.get(key));
                extensionKeyFieldMap.put(key, new ExtensionField(fieldMetaData, o));
            }

            // region [value:?:xxx]
            Optional<ExtensionObject> optValue = extensionValueResult.stream()
                    .filter(e -> e.getExtensionFieldMap().equals(extensionValueFieldMap))
                    .findAny();
            ExtensionObject valueEx;
            if (optValue.isPresent()) {
                valueEx = optValue.get();
            }
            else {
                valueEx = new ExtensionObject(extensionValueFieldMap);
                extensionValueResult.add(valueEx);
            }
            valueEx.addObject(t);
            // endregion

            // region [value(key):?:xxx]
            Optional<ExtensionObject> optKey = extensionKeyResult.stream()
                    .filter(e -> e.getExtensionFieldMap().equals(extensionKeyFieldMap))
                    .findAny();
            ExtensionObject keyEx;
            if (optKey.isPresent()) {
                keyEx = optKey.get();
            }
            else {
                keyEx = new ExtensionObject(extensionKeyFieldMap);
                extensionKeyResult.add(keyEx);
            }
            keyEx.addObject(t);
            // endregion
        }
    }

    private void onNotHasExValues(Supplier<T> creator, AbsBeanCreator<T> beanCreator, int[] indexes, JdbcResult<T> jdbcResult) throws SQLException, InvocationTargetException, IllegalAccessException {
        while (resultSet.next()) {
            T t = creator.get();
            foreach(beanCreator, indexes, t);
            jdbcResult.addResult(t);
        }
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

    private int[] getIndexes(Map<String, Integer> indexMap) throws SQLException {
        int[] indexes = new int[fieldMetaDataList.size()];
        int offset = 0;
        for (FieldMetaData fieldMetaData : fieldMetaDataList) {
            Integer index = indexMap.get(fieldMetaData.getColumnName());
            if (index != null) {
                indexes[offset++] = index;
            }
            else {
                indexes[offset++] = 0;
            }
        }
        return indexes;
    }

    private Object convertValue(FieldMetaData metaData, int index) throws SQLException {
        ITypeHandler<?> typeHandler = metaData.getTypeHandler();
        return typeHandler.getValue(resultSet, index, metaData.getGenericType());
    }
}
