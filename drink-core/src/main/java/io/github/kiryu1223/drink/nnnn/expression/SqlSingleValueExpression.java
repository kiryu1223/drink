package io.github.kiryu1223.drink.nnnn.expression;

import io.github.kiryu1223.drink.base.expression.ISqlSingleValueExpression;

import java.util.List;

public class SqlSingleValueExpression extends SqlValueExpression implements ISqlSingleValueExpression
{
    private final Object value;

    SqlSingleValueExpression(Object value)
    {
        this.value = value;
    }

    public Object getValue()
    {
        return value;
    }
}
