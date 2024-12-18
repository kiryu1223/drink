package io.github.kiryu1223.plugin.transaction;

import io.github.kiryu1223.drink.base.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.base.transaction.DefaultTransactionManager;
import io.github.kiryu1223.drink.base.transaction.Transaction;
import org.noear.solon.data.tran.TranUtils;

public class SolonTransactionManager extends DefaultTransactionManager
{
    public SolonTransactionManager(DataSourceManager dataSourceManager)
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
        SolonTransaction solonTransaction = new SolonTransaction(isolationLevel, dataSourceManager.getDataSource(), this);
        curTransaction.set(solonTransaction);
        return solonTransaction;
    }

    @Override
    public boolean currentThreadInTransaction()
    {
        return TranUtils.inTrans() || isOpenTransaction();
    }
}
