package io.github.kiryu1223.drink.config;

import io.github.kiryu1223.drink.config.def.DefaultDBConfig;
import io.github.kiryu1223.drink.config.inter.IDBConfig;
import io.github.kiryu1223.drink.ext.DbType;

public class Config
{
    private DbType dbType;
    private IDBConfig dbConfig;
    private boolean insertIgnNull = true;
    public String dsKey;

    public Config(DbType dbType)
    {
        //this.lookup = lookup;
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

    public void useDs(String key)
    {
        this.dsKey = key;
    }

    public void useDefDs()
    {
        this.dsKey = null;
    }

    public String getDsKey()
    {
        return dsKey;
    }

    public void setInsertIgnNull(boolean insertIgnNull)
    {
        this.insertIgnNull = insertIgnNull;
    }

    public boolean isInsertIgnNull()
    {
        return insertIgnNull;
    }
}
