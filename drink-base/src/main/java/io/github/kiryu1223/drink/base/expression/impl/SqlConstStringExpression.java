package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlConstStringExpression;

import java.util.List;

public class SqlConstStringExpression implements ISqlConstStringExpression
{
    private final String string;

    public SqlConstStringExpression(String string)
    {
        this.string = string;
    }

    public String getString()
    {
        return string;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<Object> values)
    {
        return getString();
    }
}
