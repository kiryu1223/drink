package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlParensExpression extends SqlExpression
{
    private final SqlExpression expression;

    SqlParensExpression(SqlExpression expression)
    {
        this.expression = expression;
    }

    public SqlExpression getExpression()
    {
        return expression;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return "(" + expression.getSqlAndValue(config, values) + ")";
    }

    @Override
    public String getSql(Config config)
    {
        return "(" + expression.getSql(config) + ")";
    }
}
