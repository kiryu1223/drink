package io.github.kiryu1223.drink.test;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.api.Drink;
import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.ext.DbType;

import javax.sql.DataSource;

public class BaseTest
{
    public final DrinkClient client;
    public final DataSource dataSource;

    BaseTest()
    {
        HikariDataSource dataSource = new HikariDataSource();
        this.dataSource=dataSource;
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/employees?rewriteBatchedStatements=true&transactionIsolation=TRANSACTION_READ_COMMITTED");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        Config config = new Config(DbType.MySQL);
        client = Drink.bootStrap(dataSource).setDataSource("2024", dataSource).setConfig(config).build();
    }
}
