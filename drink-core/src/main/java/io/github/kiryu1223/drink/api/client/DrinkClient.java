package io.github.kiryu1223.drink.api.client;


import io.github.kiryu1223.drink.api.crud.create.ObjectInsert;
import io.github.kiryu1223.drink.api.crud.delete.LDelete;
import io.github.kiryu1223.drink.api.crud.read.EmptyQuery;
import io.github.kiryu1223.drink.api.crud.read.LQuery;
import io.github.kiryu1223.drink.api.crud.update.LUpdate;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.transaction.Transaction;
import io.github.kiryu1223.expressionTree.expressions.annos.Recode;

import java.util.Collection;

public class DrinkClient
{
    private final IConfig config;

    public DrinkClient(IConfig config)
    {
        this.config = config;
    }

    public void useDs(String key)
    {
        config.getDataSourceManager().useDs(key);
    }

    public void useDefDs()
    {
        config.getDataSourceManager().useDefDs();
    }

    public Transaction beginTransaction(Integer isolationLevel)
    {
        return config.getTransactionManager().get(isolationLevel);
    }

    public Transaction beginTransaction()
    {
        return beginTransaction(null);
    }

    public <T> LQuery<T> query(@Recode Class<T> c)
    {
        return new LQuery<>(config, c);
    }

    public EmptyQuery queryEmptyTable()
    {
        return new EmptyQuery(config);
    }

    public <T> ObjectInsert<T> insert(@Recode T t)
    {
        ObjectInsert<T> objectInsert = new ObjectInsert<>(config, (Class<T>) t.getClass());
        return objectInsert.insert(t);
    }

    public <T> ObjectInsert<T> insert(@Recode Collection<T> ts)
    {
        ObjectInsert<T> objectInsert = new ObjectInsert<>(config, getType(ts));
        return objectInsert.insert(ts);
    }

    public <T> LUpdate<T> update(@Recode Class<T> c)
    {
        return new LUpdate<>(config, c);
    }

    public <T> LDelete<T> delete(@Recode Class<T> c)
    {
        return new LDelete<>(config, c);
    }

    private <T> Class<T> getType(Collection<T> ts)
    {
        for (T t : ts)
        {
            return (Class<T>) t.getClass();
        }
        throw new RuntimeException();
    }

    public IConfig getConfig()
    {
        return config;
    }
}
