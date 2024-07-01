package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.config.Config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlGroupContext extends SqlContext
{
    private final LinkedHashMap<String,SqlContext> contextMap;

    public SqlGroupContext(LinkedHashMap<String, SqlContext> contexts)
    {
        this.contextMap = contexts;
    }

    public LinkedHashMap<String, SqlContext> getContextMap()
    {
        return contextMap;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        List<String> strings = new ArrayList<>();
        for (SqlContext context : contextMap.values())
        {
            strings.add(context.getSqlAndValue(config, values));
        }
        return String.join(",", strings);
    }

    @Override
    public String getSql(Config config)
    {
        List<String> strings = new ArrayList<>();
        for (SqlContext context : contextMap.values())
        {
            strings.add(context.getSql(config));
        }
        return String.join(",", strings);
    }
}
