package io.github.kiryu1223.drink.nnnn.expression;

import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.metaData.PropertyMetaData;

import java.util.List;

public class SqlColumnExpression implements ISqlColumnExpression
{
    private final PropertyMetaData propertyMetaData;
    private final int tableIndex;

    public SqlColumnExpression(PropertyMetaData propertyMetaData, int tableIndex)
    {
        this.propertyMetaData = propertyMetaData;
        this.tableIndex = tableIndex;
    }

    @Override
    public PropertyMetaData getPropertyMetaData()
    {
        return propertyMetaData;
    }

    @Override
    public int getTableIndex()
    {
        return tableIndex;
    }
}
