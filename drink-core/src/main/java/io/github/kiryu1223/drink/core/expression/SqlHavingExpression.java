package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlHavingExpression extends SqlExpression
{
    private final SqlConditionsExpression conditions;

    SqlHavingExpression(SqlConditionsExpression conditions)
    {
        this.conditions = conditions;
    }

    public void addCond(SqlExpression condition)
    {
        conditions.getConditions().add(condition);
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        if (conditions.isEmpty()) return "";
        return "HAVING " + conditions.getSqlAndValue(config, values);
    }

    @Override
    public String getSql(Config config)
    {
        if (conditions.isEmpty()) return "";
        return "HAVING " + conditions.getSql(config);
    }

    @Override
    public <T extends SqlExpression> T copy(Config config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return (T) factory.having(conditions.copy(config));
    }
}
