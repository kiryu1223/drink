package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlSingleValueExpression;
import io.github.kiryu1223.drink.base.metaData.IConverter;
import io.github.kiryu1223.drink.base.metaData.PropertyMetaData;
import io.github.kiryu1223.drink.base.sqlExt.ISqlKeywords;

import java.util.List;

import static com.sun.jmx.mbeanserver.Util.cast;

public class SqlSingleValueExpression extends SqlValueExpression implements ISqlSingleValueExpression
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
    public String getSqlAndValue(IConfig config, List<Object> values)
    {
        if (getValue() == null)
        {
            return "NULL";
        }
        else if (getValue() instanceof ISqlKeywords)
        {
            ISqlKeywords keywords = (ISqlKeywords) getValue();
            return keywords.getKeyword(config);
        }
        else
        {
            if (values != null) values.add(getValue());
            return "?";
        }
    }

    @Override
    public String getSqlAndValue(IConfig config, List<Object> values, IConverter<?, ?> converter, PropertyMetaData propertyMetaData)
    {
        if (getValue() == null)
        {
            return "NULL";
        }
        else
        {
            if (values != null) values.add(converter.toDb(cast(getValue()), propertyMetaData));
            return "?";
        }
    }
}
