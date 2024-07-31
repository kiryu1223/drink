package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.config.Config;

import java.util.ArrayList;
import java.util.List;

public class SqlConditionsContext extends SqlContext
{
    private final List<SqlContext> conditions;

    public SqlConditionsContext(List<SqlContext> conditions)
    {
        this.conditions = conditions;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        List<String> whereStr = new ArrayList<>(conditions.size());
        for (SqlContext context : conditions)
        {
            whereStr.add(context.getSqlAndValue(config, values));
        }
        return String.join(" AND ", whereStr);
    }

    @Override
    public String getSql(Config config)
    {
        List<String> whereStr = new ArrayList<>(conditions.size());
        for (SqlContext context : conditions)
        {
            whereStr.add(context.getSql(config));
        }
        return String.join(" AND ", whereStr);
    }
}
