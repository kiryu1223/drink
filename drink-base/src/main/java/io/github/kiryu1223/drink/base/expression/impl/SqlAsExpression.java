package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.expression.ISqlAsExpression;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;

import java.util.List;

public class SqlAsExpression implements ISqlAsExpression
{
    private final ISqlExpression expression;
    private final String asName;

    public SqlAsExpression(ISqlExpression expression, String asName)
    {
        this.expression = expression;
        this.asName = asName;
    }

    @Override
    public ISqlExpression getExpression()
    {
        return expression;
    }

    @Override
    public String getAsName()
    {
        return asName;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<Object> values)
    {
        IDialect dialect = config.getDisambiguation();
        return getExpression().getSqlAndValue(config, values) + " AS " + dialect.disambiguation(getAsName());
    }
}
