package io.github.kiryu1223.drink.nnnn.expression;

import io.github.kiryu1223.drink.base.expression.ISqlAsExpression;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.base.IDialect;

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
}
