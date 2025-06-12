package io.github.kiryu1223.drink.core.api.crud.create;

import io.github.kiryu1223.drink.base.Aop;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.session.SqlSession;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.toBean.beancreator.AbsBeanCreator;
import io.github.kiryu1223.drink.base.toBean.beancreator.IGetterCaller;
import io.github.kiryu1223.drink.base.toBean.beancreator.ISetterCaller;
import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectInsertOrUpdate<T> {
    private final IConfig config;
    private final List<T> ts;

    public ObjectInsertOrUpdate(IConfig config, List<T> ts) {
        this.config = config;
        this.ts = ts;
    }

    public long executeRows(boolean autoIncrement) throws InvocationTargetException, IllegalAccessException {
        if (ts.isEmpty()) return 0;
        IDialect dialect = config.getDisambiguation();
        T t = ts.get(0);
        Class<T> tClass = (Class<T>) t.getClass();
        MetaData metaData = config.getMetaData(tClass);
        List<FieldMetaData> notIgnoreAndNavigateFields = metaData.getNotIgnoreAndNavigateFields();
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ");
        builder.append(dialect.disambiguationTableName(metaData.getTableName()));
        builder.append(" (");
        List<String> columnNames = notIgnoreAndNavigateFields
                .stream()
                .map(fm -> dialect.disambiguation(fm.getColumn()))
                .collect(Collectors.toList());
        List<String> notIgnoreAndNavigateAndPrimaryFields = notIgnoreAndNavigateFields
                .stream()
                .filter(fm -> !fm.isPrimaryKey())
                .map(fm -> dialect.disambiguation(fm.getColumn()))
                .collect(Collectors.toList());
        builder.append(String.join(",", columnNames));
        builder.append(") VALUES (");
        builder.append(notIgnoreAndNavigateAndPrimaryFields.stream().map(e -> "?").collect(Collectors.joining(",")));
        builder.append(") AS ");
        String asNew = dialect.disambiguationTableName("new");
        builder.append(asNew);
        builder.append(" ON DUPLICATE KEY UPDATE ");
        builder.append(notIgnoreAndNavigateAndPrimaryFields.stream()
                .map(e -> dialect.disambiguation(e) + " = " + asNew + "." + dialect.disambiguation(e))
                .collect(Collectors.joining(","))
        );

        Aop aop = config.getAop();
        AbsBeanCreator<T> beanCreator = config.getBeanCreatorFactory().get(tClass);
        List<SqlValue> sqlValues = new ArrayList<>();
        for (T object : ts) {
            aop.callOnInsert(object);
            for (FieldMetaData fieldMetaData : notIgnoreAndNavigateFields) {
                IGetterCaller<T, ?> beanGetter = beanCreator.getBeanGetter(fieldMetaData.getFieldName());
                Object value = beanGetter.apply(object);
                ITypeHandler<?> typeHandler = fieldMetaData.getTypeHandler();
                if (value == null && fieldMetaData.isNotNull()) {
                    throw new DrinkException(String.format("%s类的%s字段被设置为notnull，但是字段值为空且没有设置默认值注解", fieldMetaData.getParentType(), fieldMetaData.getFieldName()));
                }
                sqlValues.add(new SqlValue(value, typeHandler));
            }
        }

        SqlSession session = config.getSqlSessionFactory().getSession();

        return session.executeInsert(
                resultSet -> {
                    if (!autoIncrement) return;
                    List<FieldMetaData> primaryList = metaData.getPrimaryList();
                    Map<String, Integer> indexMap = new HashMap<>();
                    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                    for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                        String columnLabel = resultSetMetaData.getColumnLabel(i);
                        indexMap.put(columnLabel, i);
                    }
                    int index = 0;
                    while (resultSet.next()) {
                        T r = ts.get(index++);
                        for (FieldMetaData fieldMetaData : primaryList) {
                            ITypeHandler<?> typeHandler = fieldMetaData.getTypeHandler();
                            Object value = typeHandler.getValue(resultSet, indexMap.get(fieldMetaData.getColumn()), fieldMetaData.getGenericType());
                            if (value != null) {
                                ISetterCaller<T> beanSetter = beanCreator.getBeanSetter(fieldMetaData.getFieldName());
                                beanSetter.call(r, value);
                            }
                        }
                    }
                },
                builder.toString(),
                sqlValues,
                notIgnoreAndNavigateFields.size()
        );
    }
}
