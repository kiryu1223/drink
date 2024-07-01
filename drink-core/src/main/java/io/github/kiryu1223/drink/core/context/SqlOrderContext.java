package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlOrderContext extends SqlContext
{
    private final boolean asc;
    private final SqlContext context;

    public SqlOrderContext(boolean asc, SqlContext context)
    {
        this.asc = asc;
        this.context = context;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return context.getSqlAndValue(config, values) + (asc ? " ASC" : " DESC");
    }

    @Override
    public String getSql(Config config)
    {
        return context.getSql(config) + (asc ? " ASC" : " DESC");
    }
}
