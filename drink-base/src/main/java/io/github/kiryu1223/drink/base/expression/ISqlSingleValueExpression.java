package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.sqlext.ISqlKeywords;
import io.github.kiryu1223.drink.base.metaData.IConverter;
import io.github.kiryu1223.drink.base.metaData.PropertyMetaData;

import java.util.List;

import static com.sun.jmx.mbeanserver.Util.cast;

public interface ISqlSingleValueExpression extends ISqlValueExpression
{
    Object getValue();

    @Override
    default String getSqlAndValue(IConfig config, List<Object> values)
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

    default String getSqlAndValue(IConfig config, List<Object> values, IConverter<?, ?> converter, PropertyMetaData propertyMetaData)
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

    @Override
    default ISqlSingleValueExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.value(getValue());
    }
}
