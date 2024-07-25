package io.github.kiryu1223.drink.core.context;

public abstract class SqlTableContext extends SqlContext
{
    public abstract Class<?> getTableClass();
}
