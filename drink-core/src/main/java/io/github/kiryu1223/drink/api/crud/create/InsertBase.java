package io.github.kiryu1223.drink.api.crud.create;

import io.github.kiryu1223.drink.api.crud.base.CRUD;
import io.github.kiryu1223.drink.api.crud.builder.ISqlBuilder;
import io.github.kiryu1223.drink.api.crud.builder.InsertSqlBuilder;

public abstract class InsertBase implements CRUD
{
    private final InsertSqlBuilder sqlBuilder = new InsertSqlBuilder();

    @Override
    public InsertSqlBuilder getSqlBuilder()
    {
        return sqlBuilder;
    }
}
