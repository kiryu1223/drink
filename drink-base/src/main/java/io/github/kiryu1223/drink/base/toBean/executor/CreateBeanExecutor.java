package io.github.kiryu1223.drink.base.toBean.executor;


import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.expression.ExValues;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.toBean.beancreator.AbsBeanCreator;
import io.github.kiryu1223.drink.base.toBean.beancreator.ISetterCaller;
import io.github.kiryu1223.drink.base.toBean.build.ExtensionField;
import io.github.kiryu1223.drink.base.toBean.build.ExtensionObject;
import io.github.kiryu1223.drink.base.toBean.build.JdbcResult;
import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * 创建bean执行器
 */
public class CreateBeanExecutor {

    /**
     * 单列
     */
    public static <T> List<T> singleList(
            JdbcQueryResultSet jdbcQueryResultSet,
            ITypeHandler<T> typeHandler,
            Class<T> clazz
    ) {
        return singleList(jdbcQueryResultSet, typeHandler, clazz, 0);
    }

    /**
     * 单列
     */
    public static <T> List<T> singleList(
            JdbcQueryResultSet jdbcQueryResultSet,
            ITypeHandler<T> typeHandler,
            Class<T> clazz,
            long fetchSize
    ) {
        boolean isChunk = fetchSize > 0;
        try {
            ResultSet rs = jdbcQueryResultSet.getRs();
            List<T> list = new ArrayList<>();
            while (rs.next()) {
                T value = typeHandler.getValue(rs, 1, clazz);
                list.add(value);
                if (isChunk && list.size() >= fetchSize) {
                    break;
                }
            }
            return list;
        } catch (SQLException e) {
            throw new DrinkException(e);
        } finally {
            if (!isChunk) {
                jdbcQueryResultSet.close();
            }
        }
    }

    /**
     * 对象(多列)
     */
    public static <T> JdbcResult<T> classList(
            IConfig config,
            JdbcQueryResultSet jdbcQueryResultSet,
            Class<T> clazz
    ) {
        return classList(config, jdbcQueryResultSet, clazz, null);
    }

    /**
     * 对象(多列)
     */
    public static <T> JdbcResult<T> classList(
            IConfig config,
            JdbcQueryResultSet jdbcQueryResultSet,
            Class<T> clazz,
            ExValues exValues
    ) {
        return classList(config, jdbcQueryResultSet, clazz, exValues, 0);
    }

    /**
     * 对象(多列)
     */
    public static <T> JdbcResult<T> classList(
            IConfig config,
            JdbcQueryResultSet jdbcQueryResultSet,
            Class<T> clazz,
            ExValues exValues,
            long fetchSize
    )  {
        boolean isChunk = fetchSize > 0;
        try {
            // 获取除导航属性以外的字段
            List<FieldMetaData> notNavigateFields = config.getMetaData(clazz).getNotNavigateFields();
            AbsBeanCreator<T> beanCreator = config.getBeanCreatorFactory().get(clazz);
            ResultSet rs = jdbcQueryResultSet.getRs();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            int[] indexes = getIndexes(resultSetMetaData, columnCount, notNavigateFields);
            JdbcResult<T> jdbcResult = new JdbcResult<>();
            if (exValues == null) {
                create(
                        jdbcResult, rs, notNavigateFields,
                        indexes, beanCreator, isChunk, fetchSize
                );
            }
            else {
                exCreate(
                        jdbcResult, rs, resultSetMetaData,
                        columnCount, notNavigateFields, indexes,
                        beanCreator, exValues, isChunk, fetchSize
                );
            }
            return jdbcResult;
        } catch (SQLException | InvocationTargetException | IllegalAccessException e) {
            throw new DrinkException(e);
        } finally {
            if (!isChunk) {
                jdbcQueryResultSet.close();
            }
        }
    }

    private static <T> void create(
            JdbcResult<T> jdbcResult, ResultSet rs,
            List<FieldMetaData> notNavigateFields, int[] indexes,
            AbsBeanCreator<T> beanCreator, boolean isChunk, long fetchSize
            ) throws SQLException, InvocationTargetException, IllegalAccessException {
        long index = 0;
        while (rs.next()) {
            T bean = createBean(rs, notNavigateFields, indexes, beanCreator);
            jdbcResult.addResult(bean);
            index++;
            if (isChunk && index >= fetchSize) {
                break;
            }
        }
    }

    private static <T> void exCreate(
            JdbcResult<T> jdbcResult, ResultSet rs, ResultSetMetaData resultSetMetaData,
            int columnCount, List<FieldMetaData> notNavigateFields, int[] indexes,
            AbsBeanCreator<T> beanCreator, ExValues exValues, boolean isChunk, long fetchSize
    ) throws SQLException, InvocationTargetException, IllegalAccessException {
        List<ExtensionObject> extensionValueResult = jdbcResult.getExtensionValueResult();
        List<ExtensionObject> extensionKeyResult = jdbcResult.getExtensionKeyResult();
        int[] exValueIndexes = getIndexes(resultSetMetaData, columnCount, exValues.getExValueMap().values());
        int[] exKeyIndexes = getIndexes(resultSetMetaData, columnCount, exValues.getExKeyMap().values());
        int index = 0;
        while (rs.next()) {
            T bean = createBean(rs, notNavigateFields, indexes, beanCreator);
            Map<String, ExtensionField> extensionValueFieldMap = new HashMap<>();
            Map<String, ExtensionField> extensionKeyFieldMap = new HashMap<>();
            int vOffset = 0;
            for (Map.Entry<String, FieldMetaData> entry : exValues.getExValueMap().entrySet()) {
                String key = entry.getKey();
                FieldMetaData fieldMetaData = entry.getValue();
                Object o = convertValue(rs, fieldMetaData.getTypeHandler(), fieldMetaData.getGenericType(), exValueIndexes[vOffset++]);
                extensionValueFieldMap.put(key, new ExtensionField(fieldMetaData, o));
            }
            int kOffset = 0;
            for (Map.Entry<String, FieldMetaData> entry : exValues.getExKeyMap().entrySet()) {
                String key = entry.getKey();
                FieldMetaData fieldMetaData = entry.getValue();
                Object o = convertValue(rs, fieldMetaData.getTypeHandler(), fieldMetaData.getGenericType(), exKeyIndexes[kOffset++]);
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
            valueEx.addObject(bean);
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
            keyEx.addObject(bean);
            // endregion

            index++;

            if (isChunk && index >= fetchSize) {
                break;
            }
        }
    }

    public static <Key, T> Map<Key, List<T>> classListAnotherKeyMap(
            IConfig config,
            JdbcQueryResultSet jdbcQueryResultSet,
            Class<T> clazz,
            FieldMetaData anotherKey,
            String mappingKeyName
    ) {
        try {
            // 获取除导航属性以外的字段
            List<FieldMetaData> notNavigateFields = config.getMetaData(clazz).getNotNavigateFields();
            AbsBeanCreator<T> beanCreator = config.getBeanCreatorFactory().get(clazz);
            ResultSet rs = jdbcQueryResultSet.getRs();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            int[] indexes = getIndexes(resultSetMetaData, columnCount, notNavigateFields);
            int anotherKeyIndex = getIndex(resultSetMetaData, columnCount, mappingKeyName);
            Map<Key, List<T>> map = new HashMap<>();
            ITypeHandler<Key> anotherKeyTypeHandler = (ITypeHandler<Key>) anotherKey.getTypeHandler();
            while (rs.next()) {
                T bean = createBean(rs, notNavigateFields, indexes, beanCreator);
                Key key = anotherKeyTypeHandler.getValue(rs, anotherKeyIndex, anotherKey.getGenericType());
                List<T> list = map.computeIfAbsent(key, k -> new ArrayList<>());
                list.add(bean);
            }
            return map;
        } catch (SQLException | InvocationTargetException | IllegalAccessException e) {
            throw new DrinkException(e);
        } finally {
            jdbcQueryResultSet.close();
        }
    }

    private static <T> T createBean(
            ResultSet rs, List<FieldMetaData> notNavigateFields,
            int[] indexes, AbsBeanCreator<T> beanCreator
    ) throws SQLException, IllegalAccessException, InvocationTargetException {
        T bean = beanCreator.getBeanCreator().get();
        int offset = 0;
        for (FieldMetaData fieldMetaData : notNavigateFields) {
            int index = indexes[offset++];
            if (index > 0) {
                Object value = convertValue(rs, fieldMetaData.getTypeHandler(), fieldMetaData.getGenericType(), index);
                if (value != null) {
                    ISetterCaller<T> setter = beanCreator.getBeanSetter(fieldMetaData.getFieldName());
                    setter.call(bean, value);
                }
            }
        }
        return bean;
    }

    private static int[] getIndexes(
            ResultSetMetaData resultSetMetaData,
            int columnCount, Collection<FieldMetaData> fieldMetaDataList
    ) throws SQLException {
        Map<String, Integer> indexMap = new HashMap<>();
        for (int i = 1; i <= columnCount; i++) {
            String columnLabel = resultSetMetaData.getColumnLabel(i);
            indexMap.put(columnLabel, i);
        }
        int[] indexes = new int[fieldMetaDataList.size()];
        int offset = 0;
        for (FieldMetaData fieldMetaData : fieldMetaDataList) {
            Integer index = indexMap.get(fieldMetaData.getColumn());
            if (index != null) {
                indexes[offset++] = index;
            }
            else {
                indexes[offset++] = 0;
            }
        }
        return indexes;
    }

    private static int getIndex(ResultSetMetaData resultSetMetaData, int columnCount, String columnName) throws SQLException {
        for (int i = 1; i <= columnCount; i++) {
            String columnLabel = resultSetMetaData.getColumnLabel(i);
            if (columnLabel.equals(columnName)) {
                return i;
            }
        }
        throw new DrinkException(String.format("columnName not found:%s", columnName));
    }

    private static Object convertValue(ResultSet resultSet, ITypeHandler<?> typeHandler, Type type, int index) throws SQLException {
        return typeHandler.getValue(resultSet, index, type);
    }
}
