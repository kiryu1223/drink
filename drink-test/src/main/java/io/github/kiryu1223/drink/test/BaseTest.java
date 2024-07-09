package io.github.kiryu1223.drink.test;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.api.Drink;
import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.ext.DbType;

import java.lang.invoke.MethodHandles;

public class BaseTest
{
    protected final DrinkClient client;

    public BaseTest()
    {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/employees");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        Config config = new Config(DbType.MySQL, MethodHandles.lookup());
        client = Drink.bootStrap(dataSource).setConfig(config).build();
    }
}
