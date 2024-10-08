package io.github.kiryu1223.drink.api.transaction;

public interface TransactionManager
{
    Transaction get(Integer isolationLevel);

    void remove();

    Transaction getCurTransaction();

    boolean currentThreadInTransaction();

    boolean isOpenTransaction();
}
