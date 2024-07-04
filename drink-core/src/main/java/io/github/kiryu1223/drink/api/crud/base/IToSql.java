package io.github.kiryu1223.drink.api.crud.base;

import io.github.kiryu1223.drink.api.crud.builder.ISqlBuilder;

public interface IToSql
{
    ISqlBuilder getSqlBuilder();

    default String toSql()
    {
        return getSqlBuilder().getSql();
    }
}
