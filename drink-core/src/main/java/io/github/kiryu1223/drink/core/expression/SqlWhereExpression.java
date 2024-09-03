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
    public String getSql(Config config)
    {
        if (conditions.isEmpty()) return "";
        return "WHERE " + conditions.getSql(config);
    }
}
