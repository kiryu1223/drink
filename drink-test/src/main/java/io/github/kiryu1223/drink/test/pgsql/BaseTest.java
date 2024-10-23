package io.github.kiryu1223.drink.test.pgsql;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.Drink;
import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.core.dataSource.DefaultDataSourceManager;

abstract class BaseTest
{
    protected static final DrinkClient client;
    protected static final HikariDataSource dataSource;

    static
    {
        dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/");
        dataSource.setUsername("postgres");
        dataSource.setPassword("root");
        DataSourceManager dataSourceManager = new DefaultDataSourceManager(dataSource);
        client = Drink.bootStrap()
                .setDataSourceManager(dataSourceManager)
                .setDbType(DbType.PostgreSQL)
                .build();
    }

//    public BaseTest()
//    {
//        this.client = initMSSql();
//    }
//
//    private DrinkClient initMSSql()
//    {
//        sqlserverDataSource = new HikariDataSource();
//        sqlserverDataSource.setJdbcUrl("jdbc:sqlserver://localhost:1433;database=DrinkDb;encrypt=true;trustServerCertificate=true");
//        sqlserverDataSource.setUsername("sa");
//        sqlserverDataSource.setPassword("root");
//        sqlserverDataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        DataSourceManager dataSourceManager = new DefaultDataSourceManager(sqlserverDataSource);
//        return Drink.bootStrap()
//                .setDataSourceManager(dataSourceManager)
//                .setDbType(DbType.SQLServer)
//                .build();
//    }
}
