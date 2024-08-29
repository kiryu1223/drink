package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlHavingExpression extends SqlExpression
{
    private final SqlConditionsExpression condition;

    public SqlHavingExpression(SqlConditionsExpression condition)
    {
        this.condition = condition;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return "HAVING " + condition.getSqlAndValue(config, values);
    }

    @Override
    public String getSql(Config config)
    {
        return "HAVING " + condition.getSql(config);
    }
}
