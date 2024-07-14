package io.github.kiryu1223.drink.api.client;


import io.github.kiryu1223.drink.api.crud.create.ObjectInsert;
import io.github.kiryu1223.drink.api.crud.delete.LDelete;
import io.github.kiryu1223.drink.api.crud.read.LQuery;
import io.github.kiryu1223.drink.api.crud.transaction.Transaction;
import io.github.kiryu1223.drink.api.crud.update.LUpdate;
import io.github.kiryu1223.drink.config.Config;

import javax.sql.DataSource;
import java.util.Collection;

public class DrinkClient
{
    private final Config config;

    public DrinkClient(Config config)
    {
        this.config = config;
    }

    public void useDs(String key)
    {
        config.useDs(key);
    }

    public void useDefDs()
    {
        config.useDefDs();
    }

    public Transaction beginTransaction(Integer isolationLevel)
    {
        Transaction transaction = Transaction.curTransaction.get();
        if (transaction != null)
        {
            throw new RuntimeException("事务不能嵌套");
        }
        return new Transaction(isolationLevel, config.getDataSource());
    }

    public Transaction beginTransaction()
    {
        return beginTransaction(null);
    }

    public <T> LQuery<T> query(Class<T> c)
    {
        return new LQuery<>(config, c);
    }

    public <T> ObjectInsert<T> insert(T t)
    {
        ObjectInsert<T> objectInsert = new ObjectInsert<>(config, (Class<T>) t.getClass());
        return objectInsert.insert(t);
    }

    public <T> ObjectInsert<T> insert(Collection<T> ts)
    {
        ObjectInsert<T> objectInsert = new ObjectInsert<>(config, getType(ts));
        return objectInsert.insert(ts);
    }

    public <T> LUpdate<T> update(Class<T> c)
    {
        return new LUpdate<>(config, c);
    }

    public <T> LDelete<T> delete(Class<T> c)
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
}
