package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SqlSetsExpression extends SqlExpression
{
    private final List<SqlSetExpression> sets = new ArrayList<>();

    SqlSetsExpression()
    {
    }

    public List<SqlSetExpression> getSets()
    {
        return sets;
    }

    public void addSet(SqlSetExpression set)
    {
        sets.add(set);
    }

    public void addSet(Collection<SqlSetExpression> set)
    {
        sets.addAll(set);
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
    public <T extends SqlExpression> T copy(Config config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        SqlSetsExpression newSets = factory.sets();
        for (SqlSetExpression set : sets)
        {
            newSets.addSet((SqlSetExpression) set.copy(config));
        }
        return (T) newSets;
    }
}
