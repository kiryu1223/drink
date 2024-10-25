package io.github.kiryu1223.drink.core.sqlBuilder;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.List;

public class InsertSqlBuilder implements ISqlBuilder
{
    private final IConfig config;

    public InsertSqlBuilder(IConfig config)
    {
        this.config = config;
    }


    @Override
    public String getSql()
    {
        return "";
    }

    @Override
    public String getSqlAndValue(List<Object> values)
    {
        return "";
    }

    public IConfig getConfig()
    {
        return config;
    }
}
