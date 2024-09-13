package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;
import io.github.kiryu1223.drink.ext.IConverter;
import io.github.kiryu1223.drink.ext.ISqlKeywords;

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
        else if (value instanceof ISqlKeywords)
        {
            ISqlKeywords keywords = (ISqlKeywords) value;
            return keywords.getKeyword(config);
        }
        else
        {
            if (values != null) values.add(value);
            return "?";
        }
    }

    public String getSqlAndValue(Config config, List<Object> values, IConverter<?, ?> converter, PropertyMetaData propertyMetaData)
    {
        if (value == null)
        {
            return "NULL";
        }
        else
        {
            if (values != null) values.add(converter.toDb(cast(value), propertyMetaData));
            return "?";
        }
    }

    @Override
    public <T extends SqlExpression> T copy(Config config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return (T) factory.value(value);
    }
}
