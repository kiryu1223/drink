package io.github.kiryu1223.drink.core.api.crud.read;

import io.github.kiryu1223.drink.base.exception.Winner;
import io.github.kiryu1223.drink.base.sqlExt.IAggregation;
import io.github.kiryu1223.drink.base.sqlExt.SqlExtensionExpression;
import io.github.kiryu1223.expressionTree.delegate.Func1;

import java.math.BigDecimal;

public final class Aggregate<T> implements IAggregation
{
    public T table;

    @SqlExtensionExpression(template = "COUNT({r})")
    public <R> long count(Func1<T,R> r) {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "SUM({r})")
    public <R> R sum(Func1<T,R> r) {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "AVG({r})")
    public BigDecimal avg(Func1<T,Number> r) {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "MAX({r})")
    public <R> R max(Func1<T,R> r) {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "MIN({r})")
    public <R> R min(Func1<T,R> r) {
        return Winner.error();
    }
}
