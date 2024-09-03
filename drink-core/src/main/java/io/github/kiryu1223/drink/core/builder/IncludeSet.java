package io.github.kiryu1223.drink.core.builder;


import io.github.kiryu1223.drink.core.expression.SqlColumnExpression;
import io.github.kiryu1223.drink.core.expression.SqlExpression;

import java.util.ArrayList;
import java.util.List;

public class IncludeSet
{
    private final SqlColumnExpression columnExpression;
    private final SqlExpression cond;
    private final List<IncludeSet> includeSets = new ArrayList<>();

    public IncludeSet(SqlColumnExpression columnExpression, SqlExpression cond)
    {
        this.columnExpression = columnExpression;
        this.cond = cond;
    }

    public IncludeSet(SqlColumnExpression columnExpression)
    {
        this(columnExpression,null);
    }

    public SqlColumnExpression getColumnExpression()
    {
        return columnExpression;
    }

    public SqlExpression getCond()
    {
        return cond;
    }

    public boolean hasCond()
    {
        return cond != null;
    }

    public List<IncludeSet> getIncludeSets()
    {
        return includeSets;
    }

    public IncludeSet getLastIncludeSet()
    {
        return includeSets.get(includeSets.size() - 1);
    }
}
