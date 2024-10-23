package io.github.kiryu1223.drink.nnnn.expression;

import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlOrderExpression;
import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlOrderExpression implements ISqlOrderExpression
{
    private final ISqlExpression expression;
    private final boolean asc;

    public SqlOrderExpression(ISqlExpression expression, boolean asc)
    {
        this.expression = expression;
        this.asc = asc;
    }

    @Override
    public ISqlExpression getExpression()
    {
        return expression;
    }

    @Override
    public boolean isAsc()
    {
        return asc;
    }
}
