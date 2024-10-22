package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.ArrayList;
import java.util.List;

public class SqlSelectExpression extends SqlExpression
{
    private List<SqlExpression> columns;
    protected boolean distinct = false;
    private Class<?> target;
    private boolean isSingle;

    SqlSelectExpression(List<SqlExpression> columns, Class<?> target, boolean isSingle)
    {
        this.columns = columns;
        this.target = target;
        this.isSingle = isSingle;
    }

    public List<SqlExpression> getColumns()
    {
        return columns;
    }

    public boolean isSingle()
    {
        return isSingle;
    }

    public Class<?> getTarget()
    {
        return target;
    }

    public void setColumns(List<SqlExpression> columns)
    {
        this.columns = columns;
    }

    public void setTarget(Class<?> target)
    {
        this.target = target;
    }

    public void setSingle(boolean single)
    {
        isSingle = single;
    }

    public void setDistinct(boolean distinct)
    {
        this.distinct = distinct;
    }

    public boolean isDistinct()
    {
        return distinct;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        List<String> strings = new ArrayList<>(columns.size());
        for (SqlExpression sqlExpression : columns)
        {
            strings.add(sqlExpression.getSqlAndValue(config, values));
        }
        String col = String.join(",", strings);
        List<String> result = new ArrayList<>();
        result.add("SELECT");
        if (distinct)
        {
            result.add("DISTINCT");
        }
        result.add(col);
        return String.join(" ", result);
    }

    @Override
    public <T extends SqlExpression> T copy(Config config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<SqlExpression> newColumns = new ArrayList<>(columns.size());
        for (SqlExpression column : columns)
        {
            newColumns.add(column.copy(config));
        }
        return (T) factory.select(newColumns, target, isDistinct());
    }
}
