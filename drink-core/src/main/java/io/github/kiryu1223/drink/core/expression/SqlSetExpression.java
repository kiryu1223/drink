package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;

import java.util.List;

public class SqlSetExpression extends SqlExpression
{
    private final SqlColumnExpression column;
    private final SqlExpression value;

    public SqlSetExpression(SqlColumnExpression column, SqlExpression value)
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
    public String getSql(Config config)
    {
        return column.getSql(config) + " = " + value.getSql(config);
    }
}
