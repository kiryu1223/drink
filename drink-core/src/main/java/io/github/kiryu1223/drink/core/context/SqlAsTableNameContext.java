package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlAsTableNameContext extends SqlContext
{
    private final SqlContext context;
    private int index;
    private String firstName = "t";

    public SqlAsTableNameContext(int index,SqlContext context)
    {
        this.context = context;
        this.index = index;
    }
    public SqlAsTableNameContext(int index,SqlContext context,String firstName)
    {
        this(index,context);
        this.firstName = firstName;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public SqlContext getContext()
    {
        return context;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return context.getSqlAndValue(config, values) + " AS " + firstName + index;
    }

    @Override
    public String getSql(Config config)
    {
        return context.getSql(config) + " AS " + firstName + index;
    }
}
