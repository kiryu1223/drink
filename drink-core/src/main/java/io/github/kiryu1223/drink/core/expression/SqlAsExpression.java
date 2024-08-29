package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlAsExpression extends SqlExpression
{
    private final SqlExpression expression;
    private final String asName;

    public SqlAsExpression(SqlExpression expression, String asName)
    {
        this.expression = expression;
        this.asName = asName;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return expression.getSqlAndValue(config, values) + " AS " + asName;
    }

    @Override
    public String getSql(Config config)
    {
        return expression.getSql(config) + " AS " + asName;
    }
}
