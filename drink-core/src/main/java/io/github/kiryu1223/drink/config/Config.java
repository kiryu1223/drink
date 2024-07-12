package io.github.kiryu1223.drink.config;

import io.github.kiryu1223.drink.config.def.DefaultDBConfig;
import io.github.kiryu1223.drink.config.inter.IDBConfig;
import io.github.kiryu1223.drink.ext.DbType;

public class Config
{
    private DbType dbType;
    private IDBConfig dbConfig;
    private boolean ignoreUpdateNoWhere = false;
    private boolean ignoreDeleteNoWhere = false;
    public final ThreadLocal<String> dsKey = new ThreadLocal<>();

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
       dsKey.set(key);
    }

    public void useDefDs()
    {
        dsKey.remove();
    }

    public String getDsKey()
    {
        return dsKey.get();
    }


}
