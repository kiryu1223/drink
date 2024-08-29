package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlOrderExpression extends SqlExpression
{
    private final boolean asc;
    private final SqlExpression expression;

    public SqlOrderExpression(boolean asc, SqlExpression expression)
    {
        this.asc = asc;
        this.expression = expression;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return expression.getSqlAndValue(config, values) + " " + (asc ? "ASC" : "DESC");
    }

    @Override
    public String getSql(Config config)
    {
        return expression.getSql(config) + " " + (asc ? "ASC" : "DESC");
    }
}
