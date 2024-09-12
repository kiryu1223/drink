package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlOrderExpression extends SqlExpression
{
    private final SqlExpression expression;
    private final boolean asc;

    SqlOrderExpression(SqlExpression expression, boolean asc)
    {
        this.expression = expression;
        this.asc = asc;
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

    @Override
    public <T extends SqlExpression> T copy(Config config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return (T) factory.order(expression.copy(config), asc);
    }
}
