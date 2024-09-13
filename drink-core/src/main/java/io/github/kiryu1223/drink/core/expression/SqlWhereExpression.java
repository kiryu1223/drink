package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlWhereExpression extends SqlExpression
{
    private final SqlConditionsExpression conditions;

    SqlWhereExpression(SqlConditionsExpression conditions)
    {
        this.conditions = conditions;
    }

    public void addCond(SqlExpression condition)
    {
        conditions.getConditions().add(condition);
    }

    public boolean isEmpty()
    {
        return conditions.isEmpty();
    }

    public SqlConditionsExpression getConditions()
    {
        return conditions;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        if (conditions.isEmpty()) return "";
        return "WHERE " + conditions.getSqlAndValue(config, values);
    }

    @Override
    public <T extends SqlExpression> T copy(Config config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return (T) factory.where(conditions.copy(config));
    }
}
