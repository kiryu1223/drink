package io.github.kiryu1223.drink.extensions.expression;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;

import java.util.List;

public class SqlSetExpression extends SqlExpression
{
    private final SqlColumnExpression column;
    private final SqlExpression value;

    SqlSetExpression(SqlColumnExpression column, SqlExpression value)
    {
        this.column = column;
        this.value = value;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        String set = column.getSqlAndValue(config, values) + " = ";
        PropertyMetaData propertyMetaData = column.getPropertyMetaData();
        if (propertyMetaData.hasConverter() && value instanceof SqlSingleValueExpression)
        {
            SqlSingleValueExpression sqlSingleValueExpression = (SqlSingleValueExpression) value;
            return set + sqlSingleValueExpression.getSqlAndValue(config, values, propertyMetaData.getConverter(), propertyMetaData);
        }
        else
        {
            return set + value.getSqlAndValue(config, values);
        }
    }

    @Override
    public <T extends SqlExpression> T copy(Config config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return (T) factory.set(column.copy(config), value.copy(config));
    }
}
