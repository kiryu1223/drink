package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.ArrayList;
import java.util.List;

public class SqlJoinsExpression extends SqlExpression
{
    private final List<SqlJoinExpression> joins;

    public SqlJoinsExpression(List<SqlJoinExpression> joins)
    {
        this.joins = joins;
    }

    public void addJoin(SqlJoinExpression join)
    {
        joins.add(join);
    }

    public List<SqlJoinExpression> getJoins()
    {
        return joins;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        if (joins.isEmpty()) return "";
        List<String> strings = new ArrayList<>(joins.size());
        for (SqlJoinExpression join : joins)
        {
            strings.add(join.getSqlAndValue(config,values));
        }
        return String.join(" ", strings);
    }

    @Override
    public String getSql(Config config)
    {
        if (joins.isEmpty()) return "";
        List<String> strings = new ArrayList<>(joins.size());
        for (SqlJoinExpression join : joins)
        {
            strings.add(join.getSql(config));
        }
        return String.join(" ", strings);
    }
}
