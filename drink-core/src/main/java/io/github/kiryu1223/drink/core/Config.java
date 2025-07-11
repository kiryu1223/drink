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
package io.github.kiryu1223.drink.core;

import io.github.kiryu1223.drink.base.*;
import io.github.kiryu1223.drink.base.converter.NameConverter;
import io.github.kiryu1223.drink.base.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.log.ISqlLogger;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.page.Pager;
import io.github.kiryu1223.drink.base.toBean.beancreator.BeanCreatorFactory;
import io.github.kiryu1223.drink.base.transaction.TransactionManager;
import io.github.kiryu1223.drink.base.transform.Transformer;
import io.github.kiryu1223.drink.core.log.DefaultSqlLogger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kiryu1223
 * @since 3.0
 */
class Config implements IConfig {
    private final Option option;
    private final DbType dbType;
    private final TransactionManager transactionManager;
    private final DataSourceManager dataSourceManager;
    private final BeanCreatorFactory beanCreatorFactory;
    private final Filter filter = new Filter();
    private final Aop aop = new Aop();
    private final IDialect disambiguation;
    private final Transformer transformer;
    private final SqlExpressionFactory sqlExpressionFactory;
    private final NameConverter nameConverter;
    private final IInsertOrUpdate insertOrUpdate;
    private final DataBaseMetaData dataBaseMetaData;
    private final ISqlLogger sqlLogger;
    private Pager pager;

    Config(Option option, DbType dbType, TransactionManager transactionManager, DataSourceManager dataSourceManager, IDbSupport dbSupport, NameConverter nameConverter, DataBaseMetaData dataBaseMetaData, Pager pager) {
        this.option = option;
        this.dbType = dbType;
        this.dataBaseMetaData = dataBaseMetaData;
        this.sqlLogger = new DefaultSqlLogger(this);
        this.beanCreatorFactory = new BeanCreatorFactory(this);

        this.transactionManager = transactionManager;
        this.dataSourceManager = dataSourceManager;

        this.disambiguation = dbSupport.getIDialect();
        this.sqlExpressionFactory = dbSupport.getSqlExpressionFactory(this);
        this.transformer = dbSupport.getTransformer(this);
        this.insertOrUpdate = dbSupport.getInsertOrUpdate(this);
        this.nameConverter = nameConverter;
        this.pager = pager;
    }

    public DataSourceManager getDataSourceManager() {
        return dataSourceManager;
    }

    public IDialect getDisambiguation() {
        return disambiguation;
    }

    public DbType getDbType() {
        return dbType;
    }

    public Filter getFilter() {
        return filter;
    }

    @Override
    public Aop getAop() {
        return aop;
    }

    public boolean isIgnoreUpdateNoWhere() {
        return option.isIgnoreUpdateNoWhere();
    }

    public boolean isIgnoreDeleteNoWhere() {
        return option.isIgnoreDeleteNoWhere();
    }

    public boolean isPrintSql() {
        return option.isPrintSql();
    }

    @Override
    public boolean isPrintValues()
    {
        return option.isPrintValues();
    }

    @Override
    public boolean isPrintTotal()
    {
        return option.isPrintTotal();
    }

    @Override
    public boolean isPrintUpdate() {
        return option.isPrintUpdate();
    }

    @Override
    public boolean isPrintTime() {
        return option.isPrintTime();
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public SqlExpressionFactory getSqlExpressionFactory() {
        return sqlExpressionFactory;
    }

    public BeanCreatorFactory getBeanCreatorFactory() {
        return beanCreatorFactory;
    }

    @Override
    public Transformer getTransformer() {
        return transformer;
    }

    @Override
    public Pager getPager() {
        return pager;
    }

    @Override
    public void setPager(Pager pager) {
        this.pager = pager;
    }

    @Override
    public IInsertOrUpdate getInsertOrUpdate() {
        return insertOrUpdate;
    }

    @Override
    public NameConverter getNameConverter() {
        return nameConverter;
    }

    private static final Map<Class<?>, MetaData> metaDataCache = new ConcurrentHashMap<>();

    public MetaData getMetaData(Class<?> c) {
        return metaDataCache.computeIfAbsent(c, clazz -> new MetaData(clazz, this));
    }

    @Override
    public DataBaseMetaData getDataBaseMetaData() {
        return dataBaseMetaData;
    }

    @Override
    public ISqlLogger getSqlLogger()
    {
        return sqlLogger;
    }
}
