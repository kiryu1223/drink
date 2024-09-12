package io.github.kiryu1223.drink.test.oracle;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.Drink;
import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.core.dataSource.DefaultDataSourceManager;
import io.github.kiryu1223.drink.ext.DbType;

abstract class BaseTest
{
    protected final DrinkClient client;
    protected HikariDataSource oracleDataSource;

    public BaseTest()
    {
        this.client = initOracle();
    }

    private DrinkClient initOracle()
    {
        oracleDataSource = new HikariDataSource();
        oracleDataSource.setJdbcUrl("jdbc:oracle:thin:@localhost:1521/XEPDB1");
        oracleDataSource.setUsername("TESTUSER");
        oracleDataSource.setPassword("root");
        oracleDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        DataSourceManager dataSourceManager = new DefaultDataSourceManager(oracleDataSource);
        return Drink.bootStrap()
                .setDataSourceManager(dataSourceManager)
                .setDbType(DbType.Oracle)
                .build();
    }
}
