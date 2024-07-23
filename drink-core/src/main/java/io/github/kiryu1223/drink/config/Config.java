package io.github.kiryu1223.drink.config;

import io.github.kiryu1223.drink.api.transaction.TransactionManager;
import io.github.kiryu1223.drink.config.dialect.DefaultDialect;
import io.github.kiryu1223.drink.config.dialect.IDialect;
import io.github.kiryu1223.drink.config.dialect.MySQLDialect;
import io.github.kiryu1223.drink.config.dialect.SqlServerDialect;
import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.core.session.SqlSessionFactory;
import io.github.kiryu1223.drink.ext.DbType;

public class Config
{
    private final Option option;
    private final DbType dbType;
    private final IDialect disambiguation;
    private final TransactionManager transactionManager;
    private final DataSourceManager dataSourceManager;
    private final SqlSessionFactory sqlSessionFactory;

    public Config(Option option, DbType dbType, TransactionManager transactionManager, DataSourceManager dataSourceManager, SqlSessionFactory sqlSessionFactory)
    {
        this.option = option;
        this.dbType = dbType;
        switch (dbType)
        {
            case MySQL:
                disambiguation = new MySQLDialect();
                break;
            case SqlServer:
                disambiguation = new SqlServerDialect();
                break;
            case H2:
            default:
                disambiguation = new DefaultDialect();
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

    public IDialect getDisambiguation()
    {
        return disambiguation;
    }

    public DbType getDbType()
    {
        return dbType;
    }

    public boolean isIgnoreUpdateNoWhere()
    {
        return option.isIgnoreUpdateNoWhere();
    }

    public boolean isIgnoreDeleteNoWhere()
    {
        return option.isIgnoreDeleteNoWhere();
    }

    public boolean isPrintSql()
    {
        return option.isPrintSql();
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
        return option.isPrintUseDs();
    }

    public boolean isPrintBatch()
    {
        return option.isPrintBatch();
    }
}
