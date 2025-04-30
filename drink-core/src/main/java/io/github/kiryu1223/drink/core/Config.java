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

import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.session.SqlSessionFactory;
import io.github.kiryu1223.drink.base.toBean.Include.IncludeFactory;
import io.github.kiryu1223.drink.base.toBean.beancreator.BeanCreatorFactory;
import io.github.kiryu1223.drink.base.transaction.TransactionManager;
import io.github.kiryu1223.drink.base.transform.DefaultTransformer;
import io.github.kiryu1223.drink.base.transform.Transformer;
import io.github.kiryu1223.drink.base.Filter;
import io.github.kiryu1223.drink.core.dialect.*;
import io.github.kiryu1223.drink.core.expression.h2.H2ExpressionFactory;
import io.github.kiryu1223.drink.core.expression.mysql.MySqlExpressionFactory;
import io.github.kiryu1223.drink.core.expression.oracle.OracleExpressionFactory;
import io.github.kiryu1223.drink.core.expression.pgsql.PostgreSQLExpressionFactory;
import io.github.kiryu1223.drink.core.expression.sqlite.SqliteExpressionFactory;
import io.github.kiryu1223.drink.core.expression.sqlserver.SqlServerExpressionFactory;
import io.github.kiryu1223.drink.core.include.h2.H2IncludeFactory;
import io.github.kiryu1223.drink.core.include.mysql.MySqlIncludeFactory;
import io.github.kiryu1223.drink.core.include.oracle.OracleIncludeFactory;
import io.github.kiryu1223.drink.core.include.sqlserver.SqlServerIncludeFactory;

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
    private final Transformer transformer;
    private IDialect disambiguation;
    private SqlExpressionFactory sqlExpressionFactory;
    private IncludeFactory includeFactory;

    public Config(Option option, DbType dbType, TransactionManager transactionManager, DataSourceManager dataSourceManager, SqlSessionFactory sqlSessionFactory, BeanCreatorFactory beanCreatorFactory) {
        this.option = option;
        this.dbType = dbType;
        this.beanCreatorFactory = beanCreatorFactory;

        this.disambiguation = getIDialectByDbType(dbType);
        this.sqlExpressionFactory = getSqlExpressionFactoryByDbType(dbType);
        this.includeFactory = getIncludeFactoryByDbType(dbType);

        this.transactionManager = transactionManager;
        this.dataSourceManager = dataSourceManager;
        this.sqlSessionFactory = sqlSessionFactory;
        this.transformer = new DefaultTransformer(this);
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

    public void setDbType(DbType dbType) {
        this.dbType = dbType;
        this.disambiguation = getIDialectByDbType(dbType);
        this.sqlExpressionFactory = getSqlExpressionFactoryByDbType(dbType);
        this.includeFactory = getIncludeFactoryByDbType(dbType);
    }

    private IDialect getIDialectByDbType(DbType dbType) {
        switch (dbType) {
            case Any:
                return new DefaultDialect();
            case MySQL:
                return new MySQLDialect();
            case SQLServer:
                return new SqlServerDialect();
            case H2:
                return new H2Dialect();
            case Oracle:
                return new OracleDialect();
            case SQLite:
                return new SQLiteDialect();
            case PostgreSQL:
                return new PostgreSQLDialect();
            default:
                throw new RuntimeException(dbType.name());
        }
    }

    public SqlExpressionFactory getSqlExpressionFactoryByDbType(DbType dbType) {
        switch (dbType) {
            case Any:
            case MySQL:
                return new MySqlExpressionFactory();
            case SQLServer:
                return new SqlServerExpressionFactory();
            case H2:
                return new H2ExpressionFactory();
            case Oracle:
                return new OracleExpressionFactory();
            case SQLite:
                return new SqliteExpressionFactory();
            case PostgreSQL:
                return new PostgreSQLExpressionFactory();
            default:
                throw new RuntimeException(dbType.name());
        }
    }

    public IncludeFactory getIncludeFactoryByDbType(DbType dbType) {
        switch (dbType) {
            case Any:
            case MySQL:
            case SQLite:
            case PostgreSQL:
                return new MySqlIncludeFactory();
            case SQLServer:
                return new SqlServerIncludeFactory();
            case H2:
                return new H2IncludeFactory();
            case Oracle:
                return new OracleIncludeFactory();
            default:
                throw new RuntimeException(dbType.name());
        }
    }
}
