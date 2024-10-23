package io.github.kiryu1223.drink.nnnn.expression;

import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlSetExpression;

import java.util.List;

public class SqlSetExpression implements ISqlSetExpression
{
    private final ISqlColumnExpression column;
    private final ISqlExpression value;

    SqlSetExpression(ISqlColumnExpression column, ISqlExpression value)
    {
        this.column = column;
        this.value = value;
    }

    @Override
    public ISqlColumnExpression getColumn()
    {
        return column;
    }

    @Override
    public ISqlExpression getValue()
    {
        return value;
    }
}
