package io.github.kiryu1223.drink.core.builder;

import io.github.kiryu1223.drink.core.context.SqlContext;
import io.github.kiryu1223.drink.core.context.SqlPropertyContext;

public class IncludeSet
{
    private final SqlPropertyContext propertyContext;
    private final SqlContext cond;

    public IncludeSet(SqlPropertyContext propertyContext, SqlContext cond)
    {
        this.propertyContext = propertyContext;
        this.cond = cond;
    }

    public IncludeSet(SqlPropertyContext propertyContext)
    {
        this(propertyContext,null);
    }

    public SqlPropertyContext getPropertyContext()
    {
        return propertyContext;
    }

    public SqlContext getCond()
    {
        return cond;
    }

    public boolean hasCond()
    {
        return cond != null;
    }
}
