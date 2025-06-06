package io.github.kiryu1223.drink.test.mssql;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.base.dataSource.DefaultDataSourceManager;
import io.github.kiryu1223.drink.core.SqlClient;
import io.github.kiryu1223.drink.core.Builder;

abstract class BaseTest
{
    protected static final SqlClient client;
    protected static final HikariDataSource sqlserverDataSource;

    static
    {
        sqlserverDataSource = new HikariDataSource();
        sqlserverDataSource.setJdbcUrl("jdbc:sqlserver://localhost:1433;database=DrinkDb;encrypt=true;trustServerCertificate=true");
        sqlserverDataSource.setUsername("sa");
        sqlserverDataSource.setPassword("root");
        sqlserverDataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        DataSourceManager dataSourceManager = new DefaultDataSourceManager(sqlserverDataSource);
        client = Builder.bootStrap()
                .setDataSourceManager(dataSourceManager)
                .setDbType(DbType.SQLServer)
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
