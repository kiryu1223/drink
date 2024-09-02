package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlConstStringExpression extends SqlExpression
{
    private final String string;

    public SqlConstStringExpression(String string)
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
