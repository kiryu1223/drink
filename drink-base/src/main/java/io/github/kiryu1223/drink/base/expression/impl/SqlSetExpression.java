package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlSetExpression;
import io.github.kiryu1223.drink.base.expression.ISqlSingleValueExpression;
import io.github.kiryu1223.drink.base.metaData.PropertyMetaData;

import java.util.List;

public class SqlSetExpression implements ISqlSetExpression
{
    private final ISqlColumnExpression column;
    private final ISqlExpression value;

    SqlSetExpression(ISqlColumnExpression column, ISqlExpression value)
    {
        this.column = column;
        this.value = value;
    }

    @Override
    public ISqlColumnExpression getColumn()
    {
        return column;
    }

    @Override
    public ISqlExpression getValue()
    {
        return value;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<Object> values)
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
}
