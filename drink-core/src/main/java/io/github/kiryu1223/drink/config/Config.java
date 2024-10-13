package io.github.kiryu1223.drink.config;

import io.github.kiryu1223.drink.api.transaction.TransactionManager;
import io.github.kiryu1223.drink.config.dialect.IDialect;
import io.github.kiryu1223.drink.core.builder.FastCreatorFactory;
import io.github.kiryu1223.drink.core.builder.IResultSetValueGetter;
import io.github.kiryu1223.drink.core.builder.IncludeFactory;
import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.core.expression.SqlExpressionFactory;
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
    private final SqlExpressionFactory sqlExpressionFactory;
    private final IncludeFactory includeFactory;
    private final FastCreatorFactory fastCreatorFactory;
    private final IResultSetValueGetter valueGetter;

    public Config(Option option, DbType dbType, TransactionManager transactionManager, DataSourceManager dataSourceManager, SqlSessionFactory sqlSessionFactory, FastCreatorFactory fastCreatorFactory)
    {
        this.option = option;
        this.dbType = dbType;
        this.fastCreatorFactory = fastCreatorFactory;

        this.disambiguation = dbType.getDialect();
        this.sqlExpressionFactory = dbType.getSqlExpressionFactory();
        this.includeFactory = dbType.getIncludeFactory();
        this.valueGetter = dbType.getValueGetter();
//        switch (dbType)
//        {
//            case MySQL:
//                disambiguation = new MySQLDialect();
//                sqlExpressionFactory = new MySqlExpressionFactory();
//                includeFactory = new MySqlIncludeFactory();
//                break;
//            case SQLServer:
//                disambiguation = new SqlServerDialect();
//                sqlExpressionFactory = new SqlServerExpressionFactory();
//                includeFactory = new SqlServerIncludeFactory();
//                break;
//            case Oracle:
//                disambiguation = new OracleDialect();
//                sqlExpressionFactory = new OracleExpressionFactory();
//                includeFactory = new OracleIncludeFactory();
//                break;
//            case H2:
//            default:
//                disambiguation = new DefaultDialect();
//                sqlExpressionFactory = new H2ExpressionFactory();
//                includeFactory = new H2IncludeFactory();
//                break;
//        }
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

    public SqlExpressionFactory getSqlExpressionFactory()
    {
        return sqlExpressionFactory;
    }

    public IncludeFactory getIncludeFactory()
    {
        return includeFactory;
    }

    public FastCreatorFactory getFastCreatorFactory()
    {
        return fastCreatorFactory;
    }

    public IResultSetValueGetter getValueGetter()
    {
        return valueGetter;
    }
}
