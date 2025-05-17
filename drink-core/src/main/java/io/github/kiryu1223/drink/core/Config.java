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
import io.github.kiryu1223.drink.base.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.session.SqlSessionFactory;
import io.github.kiryu1223.drink.base.toBean.Include.IncludeFactory;
import io.github.kiryu1223.drink.base.toBean.beancreator.BeanCreatorFactory;
import io.github.kiryu1223.drink.base.transaction.TransactionManager;
import io.github.kiryu1223.drink.base.transform.Transformer;

/**
 * @author kiryu1223
 * @since 3.0
 */
class Config implements IConfig {
    private final Option option;
    private DbType dbType;
    private final TransactionManager transactionManager;
    private final DataSourceManager dataSourceManager;
    private final SqlSessionFactory sqlSessionFactory;
    private final BeanCreatorFactory beanCreatorFactory;
    private final Filter filter = new Filter();
    private final IDialect disambiguation;
    private final Transformer transformer;
    private final SqlExpressionFactory sqlExpressionFactory;
    private final IncludeFactory includeFactory;

    public Config(Option option, DbType dbType, TransactionManager transactionManager, DataSourceManager dataSourceManager, SqlSessionFactory sqlSessionFactory, BeanCreatorFactory beanCreatorFactory,IDbSupport dbSupport) {
        this.option = option;
        this.dbType = dbType;
        this.beanCreatorFactory = beanCreatorFactory;

        this.transactionManager = transactionManager;
        this.dataSourceManager = dataSourceManager;
        this.sqlSessionFactory = sqlSessionFactory;

        this.disambiguation=dbSupport.getIDialect();
        this.sqlExpressionFactory=dbSupport.getSqlExpressionFactory();
        this.transformer=dbSupport.getTransformer(this);
        this.includeFactory=dbSupport.getIncludeFactory();
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

    public boolean isIgnoreUpdateNoWhere() {
        return option.isIgnoreUpdateNoWhere();
    }

    public boolean isIgnoreDeleteNoWhere() {
        return option.isIgnoreDeleteNoWhere();
    }

    public boolean isPrintSql() {
        return option.isPrintSql();
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public boolean isPrintBatch() {
        return option.isPrintBatch();
    }

    public SqlExpressionFactory getSqlExpressionFactory() {
        return sqlExpressionFactory;
    }

    public IncludeFactory getIncludeFactory() {
        return includeFactory;
    }

    public BeanCreatorFactory getBeanCreatorFactory() {
        return beanCreatorFactory;
    }

    @Override
    public Transformer getTransformer() {
        return transformer;
    }
}
