package io.github.kiryu1223.drink.base.session;


import io.github.kiryu1223.drink.base.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.base.transaction.TransactionManager;

public class DefaultSqlSessionFactory implements SqlSessionFactory
{
    protected final DataSourceManager dataSourceManager;
    protected final TransactionManager transactionManager;

    public DefaultSqlSessionFactory(DataSourceManager dataSourceManager, TransactionManager transactionManager)
    {
        this.dataSourceManager = dataSourceManager;
        this.transactionManager = transactionManager;
    }

    @Override
    public SqlSession getSession()
    {
        return new DefaultSqlSession(dataSourceManager, transactionManager);
    }
}
