package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlAsNameContext extends SqlContext
{
    private final String asName;
    private final SqlContext context;

    public SqlAsNameContext(String asName, SqlContext context)
    {
        this.asName = asName;
        this.context = context;
    }

    public String getAsName()
    {
        return asName;
    }

    public SqlContext getContext()
    {
        return context;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return context.getSqlAndValue(config, values) + " AS " + asName;
    }

    @Override
    public String getSql(Config config)
    {
        return context.getSql(config) + " AS " + asName;
    }
}
