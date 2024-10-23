package io.github.kiryu1223.drink.test.sqlite;

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
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setJdbcUrl("jdbc:sqlite:D:/sqlite3/test.db");
        DataSourceManager dataSourceManager = new DefaultDataSourceManager(dataSource);
        client = Drink.bootStrap()
                .setDataSourceManager(dataSourceManager)
                .setDbType(DbType.SQLite)
                .build();
    }
}
