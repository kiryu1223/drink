package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlAsTableNameContext extends SqlContext
{
    private final SqlContext context;
    private int index;

    public SqlAsTableNameContext(int index,SqlContext context)
    {
        this.context = context;
        this.index = index;
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

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return context.getSqlAndValue(config, values) + " AS " + "t" + index;
    }

    @Override
    public String getSql(Config config)
    {
        return context.getSql(config) + " AS " + "t" + index;
    }
}
