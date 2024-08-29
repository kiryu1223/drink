package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SqlValueExpression extends SqlExpression
{
    private final Object value;

    public SqlValueExpression(Object value)
    {
        this.value = value;
    }

    public Object getValue()
    {
        return value;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        if (value == null)
        {
            return "NULL";
        }
        else
        {
            values.add(value);
            return "?";
        }
    }

    @Override
    public String getSql(Config config)
    {
        if (value == null)
        {
            return "NULL";
        }
        else
        {
            return "?";
        }
    }
}
