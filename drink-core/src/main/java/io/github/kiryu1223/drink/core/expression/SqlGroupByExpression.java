package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.ArrayList;
import java.util.List;

public class SqlGroupByExpression extends SqlExpression
{
    private final List<SqlColumnExpression> columns;

    public SqlGroupByExpression(List<SqlColumnExpression> columns)
    {
        this.columns = columns;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        List<String> strings = new ArrayList<>();
        for (SqlColumnExpression column : columns)
        {
            strings.add(column.getSqlAndValue(config, values));
        }
        return "GROUP BY " + String.join(",", strings);
    }

    @Override
    public String getSql(Config config)
    {
        List<String> strings = new ArrayList<>();
        for (SqlColumnExpression column : columns)
        {
            strings.add(column.getSql(config));
        }
        return "GROUP BY " + String.join(",", strings);
    }
}
