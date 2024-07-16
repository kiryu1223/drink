package io.github.kiryu1223.drink;

import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.api.crud.transaction.TransactionManager;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.core.session.SqlSessionFactory;
import io.github.kiryu1223.drink.ext.DbType;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class Drink
{
    private Drink()
    {
    }

    private DbType dbType;
    private DataSourceManager dataSourceManager;
    private TransactionManager transactionManager;
    private SqlSessionFactory sqlSessionFactory;

    private Map<String, DataSource> map = new HashMap<>();

    public DrinkClient build()
    {
        for (Map.Entry<String, DataSource> entry : map.entrySet())
        {
            dataSourceManager.addDataSource(entry.getKey(), entry.getValue());
        }
        Config config = new Config(dbType, transactionManager, dataSourceManager, sqlSessionFactory);
        return new DrinkClient(config);
    }

    public Drink addDataSource(String key, DataSource dataSource)
    {
        map.put(key, dataSource);
        return this;
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
}
