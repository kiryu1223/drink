package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlParensContext extends SqlContext
{
    private final SqlContext context;

    public SqlParensContext(SqlContext context)
    {
        this.context = context;
    }

    public SqlContext getContext()
    {
        return context;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return "(" + context.getSqlAndValue(config, values) + ")";
    }

    @Override
    public String getSql(Config config)
    {
        return "(" + context.getSql(config) + ")";
    }
}
