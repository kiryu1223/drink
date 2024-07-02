package io.github.kiryu1223.drink.config;

import io.github.kiryu1223.drink.config.def.DefaultDBConfig;
import io.github.kiryu1223.drink.config.inter.IDBConfig;

public class Config
{
    private IDBConfig dbConfig = new DefaultDBConfig();

    public IDBConfig getDbConfig()
    {
        return dbConfig;
    }

    public void setDbConfig(IDBConfig dbConfig)
    {
        this.dbConfig = dbConfig;
    }
}
