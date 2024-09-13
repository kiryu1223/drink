package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlGroupByExpression extends SqlExpression
{
    protected final LinkedHashMap<String, SqlExpression> columns = new LinkedHashMap<>();

    public void setColumns(LinkedHashMap<String, SqlExpression> columns)
    {
        this.columns.putAll(columns);
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
    public <T extends SqlExpression> T copy(Config config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        SqlGroupByExpression groupByExpression = factory.groupBy();
        for (Map.Entry<String, SqlExpression> entry : columns.entrySet())
        {
            groupByExpression.getColumns().put(entry.getKey(),entry.getValue().copy(config));
        }
        return (T) groupByExpression;
    }
}
