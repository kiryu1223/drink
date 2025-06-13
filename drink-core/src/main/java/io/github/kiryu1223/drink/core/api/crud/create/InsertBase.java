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
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.session.SqlSession;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.toBean.beancreator.AbsBeanCreator;
import io.github.kiryu1223.drink.base.toBean.beancreator.IGetterCaller;
import io.github.kiryu1223.drink.base.toBean.beancreator.ISetterCaller;
import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;
import io.github.kiryu1223.drink.core.api.crud.CRUD;
import io.github.kiryu1223.drink.core.sqlBuilder.InsertSqlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author kiryu1223
 * @since 3.0
 */
public abstract class InsertBase<C, R> extends CRUD<C> {
    public final static Logger log = LoggerFactory.getLogger(InsertBase.class);

    private final InsertSqlBuilder sqlBuilder;

    protected InsertSqlBuilder getSqlBuilder() {
        return sqlBuilder;
    }

    protected IConfig getConfig() {
        return sqlBuilder.getConfig();
    }

    public InsertBase(IConfig c) {
        this.sqlBuilder = new InsertSqlBuilder(c);
    }

    /**
     * 执行插入
     * @param autoIncrement 是否回填id
     */
    public long executeRows(boolean autoIncrement) {
        List<R> objects = getObjects();
        if (!objects.isEmpty()) {
            try {
                return objectsExecuteRows(autoIncrement);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            return 0;
        }
    }

    public long executeRows() {
        return executeRows(false);
    }

    public String toSql() {
        if (!getObjects().isEmpty()) {
            try {
                return makeByObjects(getConfig().getMetaData(getTableType()).getNotIgnoreAndNavigateFields(), null);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            return "";
        }
    }

    protected abstract List<R> getObjects();

    protected abstract Class<R> getTableType();

    private long objectsExecuteRows(boolean autoIncrement) throws InvocationTargetException, IllegalAccessException {
        MetaData metaData = getConfig().getMetaData(getTableType());
        List<FieldMetaData> notIgnoreFields = metaData.getNotIgnoreAndNavigateFields();
        IConfig config = getConfig();
        List<SqlValue> sqlValues = new ArrayList<>();
        String sql = makeByObjects(notIgnoreFields, sqlValues);
        //tryPrintUseDs(log,config.getDataSourceManager().getDsKey());
        tryPrintSql(log, sql);
        SqlSession session = config.getSqlSessionFactory().getSession();

        List<R> objectList = getObjects();
        if (objectList.size() > 1) {
            tryPrintBatch(log, objectList.size());
        }
        else {
            tryPrintNoBatch(log, objectList.size());
        }

        return session.executeInsert(resultSet -> {
            if (!autoIncrement) return;
            FieldMetaData generatedKey = metaData.getGeneratedKey();
            AbsBeanCreator<R> beanCreator = config.getBeanCreatorFactory().get(getTableType());
            int index = 0;
            while (resultSet.next()) {
                R r = objectList.get(index++);
                ITypeHandler<?> typeHandler = generatedKey.getTypeHandler();
                Object value = typeHandler.getValue(resultSet, 1, generatedKey.getGenericType());
                if (value != null) {
                    ISetterCaller<R> beanSetter = beanCreator.getBeanSetter(generatedKey.getFieldName());
                    beanSetter.call(r, value);
                }
            }
        }, sql, sqlValues, (int) notIgnoreFields.stream().filter(fieldMetaData -> !fieldMetaData.isGeneratedKey()).count(), autoIncrement);
    }

    private String makeByObjects(List<FieldMetaData> notIgnoreFields, List<SqlValue> sqlValues) throws InvocationTargetException, IllegalAccessException {
        IConfig config = getConfig();
        Aop aop = config.getAop();
        IDialect disambiguation = config.getDisambiguation();
        MetaData metaData = config.getMetaData(getTableType());
        AbsBeanCreator<R> beanCreator = config.getBeanCreatorFactory().get(getTableType());
        List<String> tableFields = new ArrayList<>();
        List<String> tableValues = new ArrayList<>();
        for (FieldMetaData fieldMetaData : notIgnoreFields) {
            // 如果不是数据库生成策略，则添加
            if (fieldMetaData.isGeneratedKey()) continue;
            tableFields.add(disambiguation.disambiguation(fieldMetaData.getColumn()));
            tableValues.add("?");
        }
        if (sqlValues != null) {
            for (R object : getObjects()) {
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
                    sqlValues.add(new SqlValue(value, typeHandler));
                }
            }
        }
        IDialect dialect = getSqlBuilder().getConfig().getDisambiguation();
        return "INSERT INTO " + dialect.disambiguationTableName(metaData.getTableName()) + "(" + String.join(",", tableFields) + ") VALUES(" + String.join(",", tableValues) + ")";
    }
}
