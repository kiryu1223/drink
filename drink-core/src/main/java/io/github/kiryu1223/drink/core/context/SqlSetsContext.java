package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.config.Config;

import java.util.ArrayList;
import java.util.List;

public class SqlSetsContext extends SqlContext
{
    private final List<SqlSetContext> setContexts;

    public SqlSetsContext(List<SqlSetContext> setContexts)
    {
        this.setContexts = setContexts;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        List<String> strings = new ArrayList<>(setContexts.size());
        for (SqlSetContext setContext : setContexts)
        {
            strings.add(setContext.getSqlAndValue(config, values));
        }
        return String.join(",", strings);
    }

    @Override
    public String getSql(Config config)
    {
        List<String> strings = new ArrayList<>(setContexts.size());
        for (SqlSetContext setContext : setContexts)
        {
            strings.add(setContext.getSql(config));
        }
        return String.join(",", strings);
    }
}
