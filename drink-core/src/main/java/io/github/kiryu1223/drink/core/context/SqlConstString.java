package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlConstString extends SqlContext
{
    private final String string;

    public SqlConstString(String string)
    {
        this.string = string;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return string;
    }

    @Override
    public String getSql(Config config)
    {
        return string;
    }
}
