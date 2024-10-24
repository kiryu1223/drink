package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlParensExpression;

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

    @Override
    public String getSqlAndValue(IConfig config, List<Object> values)
    {
        return "(" + getExpression().getSqlAndValue(config, values) + ")";
    }
}
