/*
 * Copyright 2017-2024 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.kiryu1223.drink.core.api.crud.create;

import io.github.kiryu1223.drink.base.Aop;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.LogicColumn;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.toBean.beancreator.AbsBeanCreator;
import io.github.kiryu1223.drink.base.toBean.beancreator.IGetterCaller;
import io.github.kiryu1223.drink.base.toBean.beancreator.ISetterCaller;
import io.github.kiryu1223.drink.base.toBean.executor.JdbcExecutor;
import io.github.kiryu1223.drink.base.toBean.executor.JdbcInsertResultSet;
import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;
import io.github.kiryu1223.drink.core.api.crud.CRUD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author kiryu1223
 * @since 3.0
 */
public abstract class InsertBase<C, R> extends CRUD<C> {
    public final static Logger log = LoggerFactory.getLogger(InsertBase.class);

    private final IConfig config;

    public InsertBase(IConfig config) {
        this.config = config;
    }

    protected IConfig getConfig() {
        return config;
    }

    /**
     * 执行插入
     * @param autoIncrement 是否回填id
     */
    public long executeRows(boolean autoIncrement) {
        try {
            return executeInsert(autoIncrement);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new DrinkException(e);
        }
    }

    public long executeRows() {
        return executeRows(false);
    }

    public String toSql() {
        List<R> objects = getObjects();
        if (!objects.isEmpty()) {
            try {
                MetaData metaData = config.getMetaData(getTableType());
                List<FieldMetaData> notIgnoreFields = metaData.getNotIgnoreAndNavigateFields();
                return objectsToSqlAndValues(objects, notIgnoreFields, null);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new DrinkException(e);
            }
        }
        else {
            return "";
        }
    }

    protected abstract List<R> getObjects();

    protected abstract Class<R> getTableType();

    @Override
    public C DisableFilter(String filterId) {
        return (C) this;
    }

    @Override
    public C DisableFilterAll(boolean condition) {
        return (C) this;
    }

    @Override
    public C DisableFilterAll() {
        return (C) this;
    }

    private long executeInsert(boolean autoIncrement) throws InvocationTargetException, IllegalAccessException {
        List<R> objects = getObjects();
        if (objects.isEmpty()) return 0;
        MetaData metaData = config.getMetaData(getTableType());
        List<FieldMetaData> notIgnoreFields = metaData.getNotIgnoreAndNavigateFields();
        List<List<SqlValue>> sqlValues = new ArrayList<>();
        String sql = objectsToSqlAndValues(objects, notIgnoreFields, sqlValues);
        try (JdbcInsertResultSet jdbcInsertResultSet = JdbcExecutor.executeInsert(config, sql, sqlValues, autoIncrement)) {
            if (autoIncrement) {
                ResultSet resultSet = jdbcInsertResultSet.getRs();
                FieldMetaData generatedPrimaryKey = metaData.getGeneratedPrimaryKey();
                AbsBeanCreator<R> beanCreator = config.getBeanCreatorFactory().get(getTableType());
                int index = 0;
                while (resultSet.next()) {
                    R r = objects.get(index++);
                    ITypeHandler<?> typeHandler = generatedPrimaryKey.getTypeHandler();
                    Object value = typeHandler.getValue(resultSet, 1, generatedPrimaryKey.getGenericType());
                    if (value != null) {
                        ISetterCaller<R> beanSetter = beanCreator.getBeanSetter(generatedPrimaryKey.getFieldName());
                        beanSetter.call(r, value);
                    }
                }
            }
            return jdbcInsertResultSet.getRow();
        } catch (SQLException e) {
            throw new DrinkException(e);
        }
    }

    private String objectsToSqlAndValues(List<R> objects, List<FieldMetaData> notIgnoreFields, List<List<SqlValue>> sqlValues) throws InvocationTargetException, IllegalAccessException {
        IConfig config = getConfig();
        Aop aop = config.getAop();
        IDialect dialect = config.getDisambiguation();
        MetaData metaData = config.getMetaData(getTableType());
        AbsBeanCreator<R> beanCreator = config.getBeanCreatorFactory().get(getTableType());
        List<String> fields = new ArrayList<>();
        List<String> tableValues = new ArrayList<>();
        for (FieldMetaData fieldMetaData : notIgnoreFields) {
            // 如果不是数据库生成策略，则添加
            if (fieldMetaData.isGeneratedKey()) continue;
            fields.add(dialect.disambiguation(fieldMetaData.getColumn()));
            if (fieldMetaData.hasLogicColumn()) {
                LogicColumn logicColumn = fieldMetaData.getLogicColumn();
                tableValues.add(logicColumn.onWrite(config));
            }
            else {
                tableValues.add("?");
            }
        }
        if (sqlValues != null) {
            List<SqlValue> values = new ArrayList<>();
            for (R object : objects) {
                aop.callOnInsert(object);
                for (FieldMetaData fieldMetaData : notIgnoreFields) {
                    // 如果是数据库生成策略，则跳过
                    if (fieldMetaData.isGeneratedKey()) continue;
                    IGetterCaller<R, ?> beanGetter = beanCreator.getBeanGetter(fieldMetaData.getFieldName());
                    Object value = beanGetter.apply(object);
                    ITypeHandler<?> typeHandler = fieldMetaData.getTypeHandler();
                    // 值为空同时设置了notNull且没有默认值注解的情况
                    if (value == null && fieldMetaData.isNotNull()) {
                        throw new DrinkException(String.format("%s类的%s字段被设置为notnull，但是字段值为空且没有设置默认值注解", fieldMetaData.getParentType(), fieldMetaData.getFieldName()));
                    }
                    values.add(new SqlValue(value, typeHandler));
                }
            }
            sqlValues.add(values);
        }

        return "INSERT INTO " + dialect.disambiguationTableName(metaData.getTableName()) + "(" + String.join(",", fields) + ") VALUES(" + String.join(",", tableValues) + ")";
    }

//    private long executeInsertOrUpdate() {
//
//    }
}
