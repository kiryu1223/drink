package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.config.Config;

import java.util.ArrayList;
import java.util.List;

public class SqlSelectorContext extends SqlContext
{
    private final List<SqlContext> sqlContexts;

    public SqlSelectorContext(List<SqlContext> sqlContexts)
    {
        this.sqlContexts = sqlContexts;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        List<String> strings = new ArrayList<>();
        for (SqlContext context : sqlContexts)
        {
            strings.add(context.getSqlAndValue(config, values));
        }
        return String.join(",", strings);
    }

    @Override
    public String getSql(Config config)
    {
        List<String> strings = new ArrayList<>();
        for (SqlContext context : sqlContexts)
        {
            strings.add(context.getSql(config));
        }
        return String.join(",", strings);
    }
}
