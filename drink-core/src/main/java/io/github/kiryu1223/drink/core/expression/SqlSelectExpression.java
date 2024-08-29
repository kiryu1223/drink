package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.ArrayList;
import java.util.List;

public class SqlSelectExpression extends SqlExpression
{
    private final List<SqlExpression> column;
    private final boolean isSingle;

    public SqlSelectExpression(List<SqlExpression> column)
    {
        this.column = column;
        this.isSingle = false;
    }

    public SqlSelectExpression(List<SqlExpression> column, boolean isSingle)
    {
        this.column = column;
        this.isSingle = isSingle;
    }

    public List<SqlExpression> getColumn()
    {
        return column;
    }

    public boolean isSingle()
    {
        return isSingle;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        List<String> strings = new ArrayList<>(column.size());
        for (SqlExpression sqlExpression : column)
        {
            strings.add(sqlExpression.getSqlAndValue(config, values));
        }
        return "SELECT " + String.join(",", strings);
    }

    @Override
    public String getSql(Config config)
    {
        List<String> strings = new ArrayList<>(column.size());
        for (SqlExpression sqlExpression : column)
        {
            strings.add(sqlExpression.getSql(config));
        }
        return "SELECT " + String.join(",", strings);
    }
}
