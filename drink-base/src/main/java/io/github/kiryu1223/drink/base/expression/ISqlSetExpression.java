package io.github.kiryu1223.drink.base.expression;


import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.metaData.PropertyMetaData;

import java.util.List;

public interface ISqlSetExpression extends ISqlExpression
{
    ISqlColumnExpression getColumn();

    ISqlExpression getValue();

    @Override
    default String getSqlAndValue(IConfig config, List<Object> values)
    {
        String set = getColumn().getSqlAndValue(config, values) + " = ";
        PropertyMetaData propertyMetaData = getColumn().getPropertyMetaData();
        if (propertyMetaData.hasConverter() && getValue() instanceof ISqlSingleValueExpression)
        {
            ISqlSingleValueExpression sqlSingleValueExpression = (ISqlSingleValueExpression) getValue();
            return set + sqlSingleValueExpression.getSqlAndValue(config, values, propertyMetaData.getConverter(), propertyMetaData);
        }
        else
        {
            return set + getValue().getSqlAndValue(config, values);
        }
    }

    @Override
    default ISqlSetExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.set(getColumn().copy(config), getValue().copy(config));
    }
}
