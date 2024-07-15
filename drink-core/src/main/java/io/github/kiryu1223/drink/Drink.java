package io.github.kiryu1223.drink;

import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.config.Config;

import javax.sql.DataSource;

public class Drink
{
    private Drink()
    {
    }

    private Config config;

    public DrinkClient build()
    {
        return new DrinkClient(config);
    }

    public Drink addDataSource(String key, DataSource dataSource)
    {
        config.getDataSourceManager().addDataSource(key, dataSource);
        return this;
    }

    public static Drink bootStrap(Config config)
    {
        Drink drink = new Drink();
        drink.config = config;
        return drink;
    }
}
