package io.github.kiryu1223.drink.api.crud.update;

import io.github.kiryu1223.drink.api.crud.base.CRUD;
import io.github.kiryu1223.drink.api.crud.builder.ISqlBuilder;
import io.github.kiryu1223.drink.api.crud.builder.UpdateSqlBuilder;

public class UpdateBase implements CRUD
{
    private final UpdateSqlBuilder sqlBuilder=new UpdateSqlBuilder();

    @Override
    public UpdateSqlBuilder getSqlBuilder()
    {
        return sqlBuilder;
    }
}
