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
import io.github.kiryu1223.drink.base.IDbSupport;
import io.github.kiryu1223.drink.base.converter.NameConverter;
import io.github.kiryu1223.drink.base.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.base.dataSource.DefaultDataSourceManager;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.page.DefaultPager;
import io.github.kiryu1223.drink.base.page.Pager;
import io.github.kiryu1223.drink.base.session.DefaultSqlSessionFactory;
import io.github.kiryu1223.drink.base.session.SqlSessionFactory;
import io.github.kiryu1223.drink.base.toBean.beancreator.BeanCreatorFactory;
import io.github.kiryu1223.drink.base.transaction.DefaultTransactionManager;
import io.github.kiryu1223.drink.base.transaction.TransactionManager;

import javax.sql.DataSource;
import java.util.ServiceLoader;

/**
 * @author kiryu1223
 * @since 3.0
 */
public class Builder {

    public static Builder bootStrap() {
        return new Builder();
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
    /**
     * 对象创建器工厂
     */
    private BeanCreatorFactory beanCreatorFactory;

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
        if (beanCreatorFactory == null) {
            beanCreatorFactory = new BeanCreatorFactory();
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
        Config config = new Config(option, dbType, transactionManager, dataSourceManager, sqlSessionFactory, beanCreatorFactory, dbSupport, nameConverter, pager);
        return new SqlClient(config);
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

    public void setDbSupport(IDbSupport dbSupport)
    {
        this.dbSupport = dbSupport;
    }

    /**
     * 设置数据源管理器
     */
    public Builder setDataSourceManager(DataSourceManager dataSourceManager) {
        this.dataSourceManager = dataSourceManager;
        return this;
    }

    public Builder setDataSource(DataSource dataSource) {
        this.dataSourceManager = new DefaultDataSourceManager(dataSource);
        return this;
    }

    /**
     * 设置事务管理器
     */
    public Builder setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        return this;
    }

    /**
     * 设置会话工厂
     */
    public Builder setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        return this;
    }

    /**
     * 设置配置
     */
    public Builder setOption(Option option) {
        this.option = option;
        return this;
    }

    /**
     * 设置对象创建器工厂
     */
    public Builder setBeanCreatorFactory(BeanCreatorFactory beanCreatorFactory) {
        this.beanCreatorFactory = beanCreatorFactory;
        return this;
    }

    /**
     * 设置数据库类型
     */
    public Builder setDbType(DbType dbType) {
        this.dbType = dbType;
        return this;
    }

    public Builder setNameConverter(NameConverter nameConverter) {
        this.nameConverter = nameConverter;
        return this;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }
}
