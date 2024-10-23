package io.github.kiryu1223.drink.nnnn.expression;

import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlGroupByExpression;
import io.github.kiryu1223.drink.config.Config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlGroupByExpression implements ISqlGroupByExpression
{
    protected final LinkedHashMap<String, ISqlExpression> columns = new LinkedHashMap<>();

    public void setColumns(LinkedHashMap<String, ISqlExpression> columns)
    {
        this.columns.putAll(columns);
    }

    public LinkedHashMap<String, ISqlExpression> getColumns()
    {
        return columns;
    }

}
