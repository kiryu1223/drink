package io.github.kiryu1223.drink.config;

import io.github.kiryu1223.drink.config.def.DefaultDBConfig;
import io.github.kiryu1223.drink.config.inter.IDBConfig;
import io.github.kiryu1223.drink.ext.DbType;

import java.lang.invoke.MethodHandles;

public class Config
{
    private DbType dbType;
    private IDBConfig dbConfig;
    private  MethodHandles.Lookup lookup;

    public Config(DbType dbType, MethodHandles.Lookup lookup)
    {
        this.lookup = lookup;
        switch (dbType)
        {
            case MySQL:
                useMySQL();
                break;
            case SqlServer:
            case H2:
            default:
                useDefault();
                break;
        }
    }

    private void useMySQL()
    {
        dbConfig = new MySQLConfig();
        dbType = DbType.MySQL;
    }

    private void useDefault()
    {
        dbConfig = new DefaultDBConfig();
        dbType = DbType.Other;
    }

    public IDBConfig getDbConfig()
    {
        return dbConfig;
    }

    public void setDbConfig(IDBConfig dbConfig)
    {
        this.dbConfig = dbConfig;
    }

    public DbType getDbType()
    {
        return dbType;
    }

    public MethodHandles.Lookup getLookup()
    {
        return lookup;
    }

    public void setLookup(MethodHandles.Lookup lookup)
    {
        this.lookup = lookup;
    }
}
