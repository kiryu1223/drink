package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.ArrayList;
import java.util.List;

public class SqlConditionsExpression extends SqlExpression
{
    private final List<SqlExpression> conditions=new ArrayList<>();

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
    public String getSql(Config config)
    {
        List<String> whereStr = new ArrayList<>(conditions.size());
        for (SqlExpression expression : conditions)
        {
            whereStr.add(expression.getSql(config));
        }
        return String.join(" AND ", whereStr);
    }
}
