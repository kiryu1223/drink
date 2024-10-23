package io.github.kiryu1223.drink.test.oracle;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.Drink;
import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.core.dataSource.DefaultDataSourceManager;

abstract class BaseTest
{
    protected static final DrinkClient client;
    protected static final HikariDataSource oracleDataSource;

    static
    {
        oracleDataSource = new HikariDataSource();
        oracleDataSource.setJdbcUrl("jdbc:oracle:thin:@localhost:1521/XEPDB1");
        oracleDataSource.setUsername("TESTUSER");
        oracleDataSource.setPassword("root");
        oracleDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        DataSourceManager dataSourceManager = new DefaultDataSourceManager(oracleDataSource);
        client = Drink.bootStrap()
                .setDataSourceManager(dataSourceManager)
                .setDbType(DbType.Oracle)
                .build();
    }

//    public BaseTest()
//    {
//        this.client = initOracle();
//    }
//
//    private DrinkClient initOracle()
//    {
//        oracleDataSource = new HikariDataSource();
//        oracleDataSource.setJdbcUrl("jdbc:oracle:thin:@localhost:1521/XEPDB1");
//        oracleDataSource.setUsername("TESTUSER");
//        oracleDataSource.setPassword("root");
//        oracleDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
//        DataSourceManager dataSourceManager = new DefaultDataSourceManager(oracleDataSource);
//        return Drink.bootStrap()
//                .setDataSourceManager(dataSourceManager)
//                .setDbType(DbType.Oracle)
//                .build();
//    }
}
