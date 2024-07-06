package io.github.kiryu1223.drink.api.crud.read.group;


import io.github.kiryu1223.drink.exception.SqlFunctionInvokeException;

public interface IAggregation
{
    default long count()
    {
        throw new SqlFunctionInvokeException();
    }

    default long count(int i)
    {
        throw new SqlFunctionInvokeException();
    }
}
