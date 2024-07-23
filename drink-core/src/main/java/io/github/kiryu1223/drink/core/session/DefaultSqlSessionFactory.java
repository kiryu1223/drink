package io.github.kiryu1223.drink.core.session;

import io.github.kiryu1223.drink.api.transaction.TransactionManager;
import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;

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
