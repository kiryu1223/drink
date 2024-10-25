package io.github.kiryu1223.drink.core.api.crud.read.group;

import io.github.kiryu1223.drink.core.exception.SqlFunctionInvokeException;
import io.github.kiryu1223.expressionTree.delegate.Func1;

import java.math.BigDecimal;

public abstract class SqlAggregation<T> implements IAggregation
{

    public <R> BigDecimal sum(Func1<T, R> expr)
    {
        throw new SqlFunctionInvokeException();
    }

    public <R> BigDecimal avg(Func1<T, R> expr)
    {
        throw new SqlFunctionInvokeException();
    }

    public <R> R max(Func1<T, R> expr)
    {
        throw new SqlFunctionInvokeException();
    }

    public <R> R min(Func1<T, R> expr)
    {
        throw new SqlFunctionInvokeException();
    }

    public <R> long count(Func1<T, R> expr)
    {
        throw new SqlFunctionInvokeException();
    }

}
