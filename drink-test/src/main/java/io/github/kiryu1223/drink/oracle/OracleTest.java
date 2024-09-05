package io.github.kiryu1223.drink.oracle;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.Drink;
import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.core.dataSource.DefaultDataSourceManager;
import io.github.kiryu1223.drink.ext.DbType;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public abstract class OracleTest
{
    protected final DrinkClient client;
    protected final HikariDataSource dataSource;
    OracleTest()
    {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:oracle:thin:@localhost:1521/XEPDB1");
        dataSource.setUsername("TESTUSER");
//        dataSource.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:XEPDB1");
//        dataSource.setUsername("TESTUSER");
        dataSource.setPassword("root");
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        DataSourceManager dataSourceManager = new DefaultDataSourceManager(dataSource);
        client = Drink.bootStrap()
                .setDataSourceManager(dataSourceManager)
                .setDbType(DbType.Oracle)
                .build();
    }
}
