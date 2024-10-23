package io.github.kiryu1223.drink.nnnn.expression;

import io.github.kiryu1223.drink.base.expression.ISqlConstStringExpression;

public class SqlConstStringExpression implements ISqlConstStringExpression
{
    private final String string;

    public SqlConstStringExpression(String string)
    {
        this.string = string;
    }

    public String getString()
    {
        return string;
    }
}
