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

import io.github.kiryu1223.drink.base.DataBaseMetaData;
import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IDbSupport;
import io.github.kiryu1223.drink.base.converter.NameConverter;
import io.github.kiryu1223.drink.base.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.base.dataSource.DefaultDataSourceManager;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.core.log.DefaultSqlLogger;
import io.github.kiryu1223.drink.base.log.ISqlLogger;
import io.github.kiryu1223.drink.base.page.DefaultPager;
import io.github.kiryu1223.drink.base.page.Pager;
import io.github.kiryu1223.drink.base.session.DefaultSqlSessionFactory;
import io.github.kiryu1223.drink.base.session.SqlSessionFactory;
import io.github.kiryu1223.drink.base.transaction.DefaultTransactionManager;
import io.github.kiryu1223.drink.base.transaction.TransactionManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ServiceLoader;

/**
 * @author kiryu1223
 * @since 3.0
 */
public class SqlBuilder {

    public static SqlBuilder bootStrap() {
        return new SqlBuilder();
    }

    private DbType dbType = DbType.MySQL;
    /**
     * 配置
     */
    private Option option = new Option();
    /**
     * 数据源管理器
     */
    private DataSourceManager dataSourceManager;
    /**
     * 事务管理器
     */
    private TransactionManager transactionManager;
    /**
     * 会话工厂
     */
    private SqlSessionFactory sqlSessionFactory;

    private NameConverter nameConverter;

    private Pager pager;

    private IDbSupport dbSupport;

    /**
     * 构建Client对象
     */
    public SqlClient build() {
        if (dataSourceManager == null) {
            throw new NullPointerException("dataSourceManager is null");
        }
        if (transactionManager == null) {
            transactionManager = new DefaultTransactionManager(dataSourceManager);
        }
        if (sqlSessionFactory == null) {
            sqlSessionFactory = new DefaultSqlSessionFactory(dataSourceManager, transactionManager);
        }
        if (nameConverter == null) {
            nameConverter = new NameConverter();
        }
        if (pager == null) {
            pager = new DefaultPager();
        }
        if (dbSupport == null) {
            dbSupport = getSpi();
        }
        DataBaseMetaData dataBaseMetaData = tryGetDbMetadate(dataSourceManager);
        Config config = new Config(option, dbType, transactionManager, dataSourceManager, sqlSessionFactory, dbSupport, nameConverter, dataBaseMetaData, pager);
        return new SqlClient(config);
    }

    private DataBaseMetaData tryGetDbMetadate(DataSourceManager dataSourceManager) {
        try (Connection connection = dataSourceManager.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            DataBaseMetaData dataBaseMetaData = new DataBaseMetaData();
            dataBaseMetaData.setProductName(metaData.getDatabaseProductName());
            dataBaseMetaData.setProductVersion(metaData.getDatabaseProductVersion());
            dataBaseMetaData.setDriverName(metaData.getDriverName());
            dataBaseMetaData.setDriverVersion(metaData.getDriverVersion());
            return dataBaseMetaData;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private IDbSupport getSpi() {
        ServiceLoader<IDbSupport> load = ServiceLoader.load(IDbSupport.class);
        for (IDbSupport support : load) {
            if (support.getDbType() == dbType) {
                return support;
            }
        }
        throw new DrinkException(String.format("找不到%s数据库支持", dbType));
    }

    public void setDbSupport(IDbSupport dbSupport) {
        this.dbSupport = dbSupport;
    }

    /**
     * 设置数据源管理器
     */
    public SqlBuilder setDataSourceManager(DataSourceManager dataSourceManager) {
        this.dataSourceManager = dataSourceManager;
        return this;
    }

    public SqlBuilder setDataSource(DataSource dataSource) {
        this.dataSourceManager = new DefaultDataSourceManager(dataSource);
        return this;
    }

    /**
     * 设置事务管理器
     */
    public SqlBuilder setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        return this;
    }

    /**
     * 设置会话工厂
     */
    public SqlBuilder setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        return this;
    }

    /**
     * 设置配置
     */
    public SqlBuilder setOption(Option option) {
        this.option = option;
        return this;
    }

    /**
     * 设置数据库类型
     */
    public SqlBuilder setDbType(DbType dbType) {
        this.dbType = dbType;
        return this;
    }

    public SqlBuilder setNameConverter(NameConverter nameConverter) {
        this.nameConverter = nameConverter;
        return this;
    }

    public SqlBuilder setPager(Pager pager) {
        this.pager = pager;
        return this;
    }
}
