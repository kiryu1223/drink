package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlOrderExpression;

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

    @Override
    public String getSqlAndValue(IConfig config, List<Object> values)
    {
        return getExpression().getSqlAndValue(config, values) + " " + (isAsc() ? "ASC" : "DESC");
    }

}
