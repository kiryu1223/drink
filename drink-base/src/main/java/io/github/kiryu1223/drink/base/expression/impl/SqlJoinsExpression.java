package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlJoinExpression;
import io.github.kiryu1223.drink.base.expression.ISqlJoinsExpression;

import java.util.ArrayList;
import java.util.List;

public class SqlJoinsExpression implements ISqlJoinsExpression
{
    private final List<ISqlJoinExpression> joins = new ArrayList<>();

    @Override
    public void addJoin(ISqlJoinExpression join)
    {
        joins.add(join);
    }

    public List<ISqlJoinExpression> getJoins()
    {
        return joins;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<Object> values)
    {
        if (getJoins().isEmpty()) return "";
        List<String> strings = new ArrayList<>(getJoins().size());
        for (ISqlJoinExpression join : getJoins())
        {
            strings.add(join.getSqlAndValue(config,values));
        }
        return String.join(" ", strings);
    }
}
