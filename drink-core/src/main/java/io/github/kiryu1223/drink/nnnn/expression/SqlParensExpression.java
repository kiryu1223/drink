package io.github.kiryu1223.drink.nnnn.expression;

import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlParensExpression;
import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlParensExpression implements ISqlParensExpression
{
    private final ISqlExpression expression;

    public SqlParensExpression(ISqlExpression expression)
    {
        this.expression = expression;
    }

    public ISqlExpression getExpression()
    {
        return expression;
    }
}
