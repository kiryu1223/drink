package io.github.kiryu1223.drink.nnnn.expression;


import io.github.kiryu1223.drink.base.expression.ISqlTableExpression;

public abstract class SqlTableExpression implements ISqlTableExpression
{
    public abstract Class<?> getTableClass();
}
