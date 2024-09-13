package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.ArrayList;
import java.util.List;

public class SqlConditionsExpression extends SqlExpression
{
    private final List<SqlExpression> conditions = new ArrayList<>();

    SqlConditionsExpression()
    {
    }

    public List<SqlExpression> getConditions()
    {
        return conditions;
    }

    public boolean isEmpty()
    {
        return conditions.isEmpty();
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        List<String> whereStr = new ArrayList<>(conditions.size());
        for (SqlExpression expression : conditions)
        {
            whereStr.add(expression.getSqlAndValue(config, values));
        }
        return String.join(" AND ", whereStr);
    }

    @Override
    public <T extends SqlExpression> T copy(Config config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        SqlConditionsExpression newConditions = factory.condition();
        for (SqlExpression condition : conditions)
        {
            newConditions.getConditions().add(condition.copy(config));
        }
        return (T) newConditions;
    }
}
