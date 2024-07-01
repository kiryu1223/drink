package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.api.crud.base.CRUD;
import io.github.kiryu1223.drink.api.crud.builder.QuerySqlBuilder;

public abstract class QueryBase implements CRUD
{
    private final QuerySqlBuilder sqlBuilder;

    protected QueryBase(Class<?> c)
    {
        sqlBuilder = new QuerySqlBuilder(c);
    }

    @Override
    public QuerySqlBuilder getSqlBuilder()
    {
        return sqlBuilder;
    }
}
