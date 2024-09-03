package io.github.kiryu1223.drink.config;

import io.github.kiryu1223.drink.api.transaction.TransactionManager;
import io.github.kiryu1223.drink.config.dialect.*;
import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.core.expression.ext.h2.H2ExpressionFactory;
import io.github.kiryu1223.drink.core.expression.ext.mysql.MySqlExpressionFactory;
import io.github.kiryu1223.drink.core.expression.ext.oracle.OracleSqlExpressionFactory;
import io.github.kiryu1223.drink.core.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.core.expression.ext.sqlserver.SqlServerExpressionFactory;
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

    public Config(Option option, DbType dbType, TransactionManager transactionManager, DataSourceManager dataSourceManager, SqlSessionFactory sqlSessionFactory)
    {
        this.option = option;
        this.dbType = dbType;
        switch (dbType)
        {
            case MySQL:
                disambiguation = new MySQLDialect();
                sqlExpressionFactory = new MySqlExpressionFactory(this);
                break;
            case SqlServer:
                disambiguation = new SqlServerDialect();
                sqlExpressionFactory = new SqlServerExpressionFactory(this);
                break;
            case Oracle:
                disambiguation = new OracleDialect();
                sqlExpressionFactory = new OracleSqlExpressionFactory(this);
                break;
            case H2:
            default:
                disambiguation = new DefaultDialect();
                sqlExpressionFactory = new H2ExpressionFactory(this);
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

    public SqlExpressionFactory getSqlExpressionFactory()
    {
        return sqlExpressionFactory;
    }
}
