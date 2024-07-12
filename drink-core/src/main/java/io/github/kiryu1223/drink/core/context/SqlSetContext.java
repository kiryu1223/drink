package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlSetContext extends SqlContext
{
    private final SqlPropertyContext propertyContext;
    private final SqlContext value;

    public SqlSetContext(SqlPropertyContext propertyContext, SqlContext value)
    {
        this.propertyContext = propertyContext;
        this.value = value;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return propertyContext.getSqlAndValue(config, values) + " = " + value.getSqlAndValue(config, values);
    }

    @Override
    public String getSql(Config config)
    {
        return propertyContext.getSql(config) + " = " + value.getSql(config);
    }
}
