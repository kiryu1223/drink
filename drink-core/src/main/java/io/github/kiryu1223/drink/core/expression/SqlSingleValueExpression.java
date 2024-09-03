package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;
import io.github.kiryu1223.drink.ext.IConverter;

import java.util.List;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.cast;

public class SqlSingleValueExpression extends SqlValueExpression
{
    private final Object value;

    SqlSingleValueExpression(Object value)
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

    public String getSqlAndValue(Config config, List<Object> values, IConverter<?,?> converter, PropertyMetaData propertyMetaData)
    {
        if (value == null)
        {
            return "NULL";
        }
        else
        {
            values.add(converter.toDb(cast(value),propertyMetaData));
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
