package io.github.kiryu1223.drink.api.crud.update;

import io.github.kiryu1223.drink.api.crud.base.CRUD;
import io.github.kiryu1223.drink.api.crud.builder.UpdateSqlBuilder;

public class UpdateBase extends CRUD
{
    private final UpdateSqlBuilder sqlBuilder = new UpdateSqlBuilder();

    public UpdateSqlBuilder getSqlBuilder()
    {
        return sqlBuilder;
    }
}
