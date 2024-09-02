package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SqlGroupByExpression extends SqlExpression
{
    private LinkedHashMap<String, SqlExpression> columns;

    public SqlGroupByExpression(LinkedHashMap<String, SqlExpression> columns)
    {
        this.columns = columns;
    }

    public void setColumns(LinkedHashMap<String, SqlExpression> columns)
    {
        this.columns = columns;
    }

    public LinkedHashMap<String, SqlExpression> getColumns()
    {
        return columns;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        if (columns.isEmpty()) return "";
        List<String> strings = new ArrayList<>();
        for (SqlExpression column : columns.values())
        {
            strings.add(column.getSqlAndValue(config, values));
        }
        return "GROUP BY " + String.join(",", strings);
    }

    @Override
    public String getSql(Config config)
    {
        if (columns.isEmpty()) return "";
        List<String> strings = new ArrayList<>();
        for (SqlExpression column : columns.values())
        {
            strings.add(column.getSql(config));
        }
        return "GROUP BY " + String.join(",", strings);
    }
}
