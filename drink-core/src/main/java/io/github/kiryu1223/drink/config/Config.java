package io.github.kiryu1223.drink.config;

import io.github.kiryu1223.drink.config.def.DefaultDBConfig;
import io.github.kiryu1223.drink.config.def.MySQLConfig;
import io.github.kiryu1223.drink.config.inter.IDBConfig;
import io.github.kiryu1223.drink.ext.DbType;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class Config
{
    private DbType dbType;
    private IDBConfig dbConfig;
    private boolean ignoreUpdateNoWhere = false;
    private boolean ignoreDeleteNoWhere = false;
    private boolean printSql = true;
    public final ThreadLocal<String> dsKey = new ThreadLocal<>();

    private final Map<String, DataSource> dataSourceMap = new HashMap<>();

    private final DataSource defluteDataSource;

    public Config(DbType dbType, DataSource dataSource)
    {
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
        defluteDataSource = dataSource;
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

    public boolean isIgnoreUpdateNoWhere()
    {
        return ignoreUpdateNoWhere;
    }

    public boolean isIgnoreDeleteNoWhere()
    {
        return ignoreDeleteNoWhere;
    }

    public void addDataSource(String key, DataSource dataSource)
    {
        dataSourceMap.put(key, dataSource);
    }

    public DataSource getDataSource()
    {
        String dsKey1 = getDsKey();
        if (dsKey1 == null)
        {
            return getDefluteDataSource();
        }
        return dataSourceMap.get(dsKey1);
    }

    private DataSource getDefluteDataSource()
    {
        return defluteDataSource;
    }

    public boolean isPrintSql()
    {
        return printSql;
    }

    public void setPrintSql(boolean printSql)
    {
        this.printSql = printSql;
    }
}
