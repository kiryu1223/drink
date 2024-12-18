package io.github.kiryu1223.drink.core;

import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;
import io.github.kiryu1223.drink.base.toBean.handler.TypeHandlerManager;
import io.github.kiryu1223.drink.core.api.client.DrinkClient;
import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.transaction.DefaultTransactionManager;
import io.github.kiryu1223.drink.base.transaction.TransactionManager;
import io.github.kiryu1223.drink.base.toBean.beancreator.BeanCreatorFactory;
import io.github.kiryu1223.drink.base.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.base.session.DefaultSqlSessionFactory;
import io.github.kiryu1223.drink.base.session.SqlSessionFactory;

public class Drink
{
    private Drink()
    {
    }

    private DbType dbType = DbType.MySQL;
    private Option option = new Option();
    private DataSourceManager dataSourceManager;
    private TransactionManager transactionManager;
    private SqlSessionFactory sqlSessionFactory;
    private BeanCreatorFactory beanCreatorFactory;

    public DrinkClient build()
    {
        if (dataSourceManager == null)
        {
            throw new NullPointerException("dataSourceManager is null");
        }
        if (transactionManager == null)
        {
            transactionManager = new DefaultTransactionManager(dataSourceManager);
        }
        if (sqlSessionFactory == null)
        {
            sqlSessionFactory = new DefaultSqlSessionFactory(dataSourceManager, transactionManager);
        }
        if (beanCreatorFactory == null)
        {
            beanCreatorFactory = new BeanCreatorFactory();
        }
        Config config = new Config(option, dbType, transactionManager, dataSourceManager, sqlSessionFactory, beanCreatorFactory);
        return new DrinkClient(config);
    }

    public static Drink bootStrap()
    {
        return new Drink();
    }

    public Drink setDataSourceManager(DataSourceManager dataSourceManager)
    {
        this.dataSourceManager = dataSourceManager;
        return this;
    }

    public Drink setTransactionManager(TransactionManager transactionManager)
    {
        this.transactionManager = transactionManager;
        return this;
    }

    public Drink setSqlSessionFactory(SqlSessionFactory sqlSessionFactory)
    {
        this.sqlSessionFactory = sqlSessionFactory;
        return this;
    }

    public Drink setDbType(DbType dbType)
    {
        this.dbType = dbType;
        return this;
    }

    public Drink setOption(Option option)
    {
        this.option = option;
        return this;
    }

    public Drink setFastCreatorFactory(BeanCreatorFactory beanCreatorFactory)
    {
        this.beanCreatorFactory = beanCreatorFactory;
        return this;
    }

    public Drink addTypeHandler(ITypeHandler<?> iTypeHandler)
    {
        TypeHandlerManager.set(iTypeHandler);
        return this;
    }
}
