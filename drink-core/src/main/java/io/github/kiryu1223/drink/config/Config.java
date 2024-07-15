package io.github.kiryu1223.drink.config;

import io.github.kiryu1223.drink.api.crud.transaction.DefaultTransactionManager;
import io.github.kiryu1223.drink.api.crud.transaction.TransactionManager;
import io.github.kiryu1223.drink.config.def.DefaultDBConfig;
import io.github.kiryu1223.drink.config.def.MySQLConfig;
import io.github.kiryu1223.drink.config.inter.IDBConfig;
import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.core.dataSource.DefaultDataSourceManager;
import io.github.kiryu1223.drink.core.session.DefaultSqlSessionFactory;
import io.github.kiryu1223.drink.core.session.SqlSessionFactory;
import io.github.kiryu1223.drink.ext.DbType;

import javax.sql.DataSource;

public class Config
{
    private DbType dbType;
    private IDBConfig dbConfig;
    private boolean ignoreUpdateNoWhere = false;
    private boolean ignoreDeleteNoWhere = false;
    private boolean printSql = true;
    private final TransactionManager transactionManager;
    private final DataSourceManager dataSourceManager;
    private final SqlSessionFactory sqlSessionFactory;

    public Config(DbType dbType, DataSource dataSource)
    {
        typeOf(dbType);
        this.dataSourceManager = new DefaultDataSourceManager(dataSource);
        this.transactionManager = new DefaultTransactionManager(dataSourceManager);
        this.sqlSessionFactory = new DefaultSqlSessionFactory(dataSourceManager, transactionManager);
    }

    public Config(DbType dbType, TransactionManager transactionManager, DataSourceManager dataSourceManager, SqlSessionFactory sqlSessionFactory)
    {
        typeOf(dbType);
        this.transactionManager = transactionManager;
        this.dataSourceManager = dataSourceManager;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    private void typeOf(DbType dbType)
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
    }

    public DataSourceManager getDataSourceManager()
    {
        return dataSourceManager;
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


    public boolean isIgnoreUpdateNoWhere()
    {
        return ignoreUpdateNoWhere;
    }

    public boolean isIgnoreDeleteNoWhere()
    {
        return ignoreDeleteNoWhere;
    }

    public void setIgnoreUpdateNoWhere(boolean ignoreUpdateNoWhere)
    {
        this.ignoreUpdateNoWhere = ignoreUpdateNoWhere;
    }

    public void setIgnoreDeleteNoWhere(boolean ignoreDeleteNoWhere)
    {
        this.ignoreDeleteNoWhere = ignoreDeleteNoWhere;
    }

    public boolean isPrintSql()
    {
        return printSql;
    }

    public void setPrintSql(boolean printSql)
    {
        this.printSql = printSql;
    }

    public TransactionManager getTransactionManager()
    {
        return transactionManager;
    }

    public SqlSessionFactory getSqlSessionFactory()
    {
        return sqlSessionFactory;
    }

}
