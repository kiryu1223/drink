package io.github.kiryu1223.drink.nnnn.expression;

import io.github.kiryu1223.drink.base.expression.ISqlSetExpression;
import io.github.kiryu1223.drink.base.expression.ISqlSetsExpression;
import io.github.kiryu1223.drink.config.Config;

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
}
