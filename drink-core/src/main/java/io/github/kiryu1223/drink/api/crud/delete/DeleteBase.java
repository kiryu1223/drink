package io.github.kiryu1223.drink.api.crud.delete;

import io.github.kiryu1223.drink.api.crud.base.CRUD;
import io.github.kiryu1223.drink.api.crud.builder.DeleteSqlBuilder;
import io.github.kiryu1223.drink.api.crud.builder.ISqlBuilder;

public abstract class DeleteBase extends CRUD
{
    private final DeleteSqlBuilder sqlBuilder = new DeleteSqlBuilder();

    public DeleteSqlBuilder getSqlBuilder()
    {
        return sqlBuilder;
    }
}
