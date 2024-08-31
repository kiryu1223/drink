package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.ArrayList;
import java.util.List;

public class SqlSetsExpression extends SqlExpression
{
    private final List<SqlSetExpression> sets;

    public SqlSetsExpression(List<SqlSetExpression> sets)
    {
        this.sets = sets;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        List<String> strings = new ArrayList<>(sets.size());
        for (SqlSetExpression expression : sets)
        {
            strings.add(expression.getSqlAndValue(config, values));
        }
        return "SET " + String.join(",", strings);
    }

    @Override
    public String getSql(Config config)
    {
        List<String> strings = new ArrayList<>(sets.size());
        for (SqlSetExpression expression : sets)
        {
            strings.add(expression.getSql(config));
        }
        return "SET " + String.join(",", strings);
    }
}
