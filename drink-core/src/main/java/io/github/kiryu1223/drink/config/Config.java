package io.github.kiryu1223.drink.config;

import io.github.kiryu1223.drink.api.crud.transaction.TransactionManager;
import io.github.kiryu1223.drink.config.disambiguation.DefaultDisambiguation;
import io.github.kiryu1223.drink.config.disambiguation.IDisambiguation;
import io.github.kiryu1223.drink.config.disambiguation.MySQLDisambiguation;
import io.github.kiryu1223.drink.config.disambiguation.SqlServerDisambiguation;
import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.core.session.SqlSessionFactory;
import io.github.kiryu1223.drink.ext.DbType;

public class Config
{
    private boolean ignoreUpdateNoWhere = false;
    private boolean ignoreDeleteNoWhere = false;
    private boolean printSql = true;
    private boolean printUseDs = true;
    private boolean printBatch = true;
    private final DbType dbType;
    private final IDisambiguation disambiguation;
    private final TransactionManager transactionManager;
    private final DataSourceManager dataSourceManager;
    private final SqlSessionFactory sqlSessionFactory;

    public Config(DbType dbType, TransactionManager transactionManager, DataSourceManager dataSourceManager, SqlSessionFactory sqlSessionFactory)
    {
        this.dbType = dbType;
        switch (dbType)
        {
            case MySQL:
                disambiguation = new MySQLDisambiguation();
                break;
            case SqlServer:
                disambiguation = new SqlServerDisambiguation();
                break;
            case H2:
            default:
                disambiguation = new DefaultDisambiguation();
                break;
        }
        this.transactionManager = transactionManager;
        this.dataSourceManager = dataSourceManager;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public DataSourceManager getDataSourceManager()
    {
        return dataSourceManager;
    }

    public IDisambiguation getDisambiguation()
    {
        return disambiguation;
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

    public boolean isPrintUseDs()
    {
        return printUseDs;
    }

    public void setPrintUseDs(boolean printUseDs)
    {
        this.printUseDs = printUseDs;
    }

    public boolean isPrintBatch()
    {
        return printBatch;
    }

    public void setPrintBatch(boolean printBatch)
    {
        this.printBatch = printBatch;
    }
}
