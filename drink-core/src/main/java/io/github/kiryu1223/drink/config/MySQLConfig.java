package io.github.kiryu1223.drink.config;

import io.github.kiryu1223.drink.config.inter.IDBConfig;

public class MySQLConfig implements IDBConfig
{
    @Override
    public String propertyDisambiguation(String property)
    {
        return "`" + property + "`";
    }
}
