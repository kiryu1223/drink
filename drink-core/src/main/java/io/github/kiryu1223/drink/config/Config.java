package io.github.kiryu1223.drink.config;

import io.github.kiryu1223.drink.config.inter.IDBConfig;

public class Config
{
    private IDBConfig dbConfig;

    public IDBConfig getDbConfig()
    {
        return dbConfig;
    }

    public void setDbConfig(IDBConfig dbConfig)
    {
        this.dbConfig = dbConfig;
    }
}
