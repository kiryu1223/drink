package io.github.kiryu1223.drink.api.crud.builder;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.SqlContext;

import java.util.ArrayList;
import java.util.List;

public class InsertSqlBuilder implements ISqlBuilder
{
    private final Config config;

    public InsertSqlBuilder(Config config)
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

    public Config getConfig()
    {
        return config;
    }
}
