package io.github.kiryu1223.drink.api;

import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.builder.DataSourcesManager;

import javax.sql.DataSource;

public class Drink
{
    private Drink()
    {
    }

    private Config config = new Config();

    public Drink setConfig(Config config)
    {
        this.config = config;
        return this;
    }

    public DrinkClient build()
    {
        return new DrinkClient(config);
    }

    public Drink setDataSource(String key, DataSource dataSource)
    {
        DataSourcesManager.addDataSource(key, dataSource);
        return this;
    }

    public static Drink boot(DataSource dataSource)
    {
        DataSourcesManager.setDeflateDataSource(dataSource);
        return new Drink();
    }
}
