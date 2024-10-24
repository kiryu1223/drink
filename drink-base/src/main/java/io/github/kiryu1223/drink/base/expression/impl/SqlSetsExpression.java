package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlSetExpression;
import io.github.kiryu1223.drink.base.expression.ISqlSetsExpression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SqlSetsExpression implements ISqlSetsExpression
{
    private final List<ISqlSetExpression> sets = new ArrayList<>();

    public List<ISqlSetExpression> getSets()
    {
        return sets;
    }

    public void addSet(ISqlSetExpression set)
    {
        sets.add(set);
    }

    public void addSet(Collection<ISqlSetExpression> set)
    {
        sets.addAll(set);
    }

    @Override
    public String getSqlAndValue(IConfig config, List<Object> values)
    {
        List<String> strings = new ArrayList<>(getSets().size());
        for (ISqlSetExpression expression : getSets())
        {
            strings.add(expression.getSqlAndValue(config, values));
        }
        return "SET " + String.join(",", strings);
    }
}
