package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.sqlBuilder.QuerySqlBuilder;

import java.util.List;

public class EndQuery<T> extends QueryBase
{
    public EndQuery(QuerySqlBuilder sqlBuilder)
    {
        super(sqlBuilder);
    }

    @Override
    public boolean any()
    {
        return super.any();
    }

    @Override
    public List<T> toList()
    {
        return super.toList();
    }

    @Override
    public T first()
    {
        return super.first();
    }
}
