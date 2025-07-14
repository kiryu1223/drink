package io.github.kiryu1223.drink.core.api.crud.update;

import io.github.kiryu1223.drink.base.Aop;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.toBean.beancreator.AbsBeanCreator;
import io.github.kiryu1223.drink.base.toBean.beancreator.IGetterCaller;
import io.github.kiryu1223.drink.base.toBean.executor.JdbcExecutor;
import io.github.kiryu1223.drink.base.toBean.executor.JdbcUpdateResultSet;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ObjectUpdate<T> {
    private final IConfig config;
    private final Class<T> target;
    private final List<T> objects;

    public ObjectUpdate(IConfig config, Class<T> target, List<T> objects) {
        this.config = config;
        this.target = target;
        this.objects = objects;
    }

    public long executeRows() {
        if (objects == null || objects.isEmpty()) {
            return 0;
        }
        try {
            String sql = toSql();
            List<List<SqlValue>> values = toValues();
            try (JdbcUpdateResultSet jdbcUpdateResultSet = JdbcExecutor.executeInsert(config, sql, values,false)) {
                return jdbcUpdateResultSet.getRow();
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String toSql() {
        IDialect dialect = config.getDisambiguation();
        MetaData metaData = config.getMetaData(target);
        List<FieldMetaData> notIgnoreAndNavigateFields = metaData.getNotIgnoreAndNavigateFields();
        List<FieldMetaData> primaryList = metaData.getPrimaryList();
        List<String> setList = new ArrayList<>(notIgnoreAndNavigateFields.size());
        List<String> whereList = new ArrayList<>(primaryList.size());
        for (FieldMetaData field : notIgnoreAndNavigateFields) {
            if (field.isPrimaryKey()) continue;
            String columnName = field.getColumnName();
            setList.add(dialect.disambiguation(columnName) + " = ?");
        }
        for (FieldMetaData field : primaryList) {
            String columnName = field.getColumnName();
            whereList.add(dialect.disambiguation(columnName) + " = ?");
        }
        return "UPDATE " + dialect.disambiguationTableName(metaData.getTableName())
               + " SET " + String.join(",", setList)
               + " WHERE " + String.join(" AND ", whereList);
    }

    private List<List<SqlValue>> toValues() throws InvocationTargetException, IllegalAccessException {
        Aop aop = config.getAop();
        MetaData metaData = config.getMetaData(target);
        AbsBeanCreator<T> beanCreator = config.getBeanCreatorFactory().get(target);
        List<FieldMetaData> notIgnoreAndNavigateFields = metaData.getNotIgnoreAndNavigateFields();
        List<FieldMetaData> primaryList = metaData.getPrimaryList();
        List<List<SqlValue>> sqlValues = new ArrayList<>(objects.size());
        for (T object : objects) {
            aop.callOnUpdate(object);
            List<SqlValue> values = new ArrayList<>(notIgnoreAndNavigateFields.size() + primaryList.size());
            for (FieldMetaData field : notIgnoreAndNavigateFields) {
                if (field.isPrimaryKey()) continue;
                IGetterCaller<T, ?> beanGetter = beanCreator.getBeanGetter(field.getFieldName());
                Object value = beanGetter.apply(object);
                values.add(new SqlValue(value, field.getTypeHandler()));
            }
            for (FieldMetaData field : primaryList) {
                IGetterCaller<T, ?> beanGetter = beanCreator.getBeanGetter(field.getFieldName());
                Object value = beanGetter.apply(object);
                if (value == null) {
                    throw new DrinkException(String.format("批量更新但是对象主键为空:%s", object));
                }
                values.add(new SqlValue(value, field.getTypeHandler()));
            }
            sqlValues.add(values);
        }
        return sqlValues;
    }
}
