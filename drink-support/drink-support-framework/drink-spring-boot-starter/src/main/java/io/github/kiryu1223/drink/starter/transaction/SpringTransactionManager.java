package io.github.kiryu1223.drink.starter.transaction;


import io.github.kiryu1223.drink.base.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.base.transaction.DefaultTransactionManager;
import io.github.kiryu1223.drink.base.transaction.Transaction;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class SpringTransactionManager extends DefaultTransactionManager
{
    public SpringTransactionManager(DataSourceManager dataSourceManager)
    {
        super(dataSourceManager);
    }

    @Override
    public Transaction get(Integer isolationLevel)
    {
        if (currentThreadInTransaction())
        {
            throw new RuntimeException("不支持多重事务");
        }
        SpringTransaction springTransaction = new SpringTransaction(isolationLevel, dataSourceManager.getDataSource(), this);
        curTransaction.set(springTransaction);
        return springTransaction;
    }

    @Override
    public boolean currentThreadInTransaction()
    {
        return TransactionSynchronizationManager.isActualTransactionActive() || isOpenTransaction();
    }
}
