package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;

import java.util.ArrayList;
import java.util.List;

public class SqlSelectExpression extends SqlExpression
{
    private List<SqlExpression> columns;
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

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        List<String> strings = new ArrayList<>(columns.size());
        for (SqlExpression sqlExpression : columns)
        {
            strings.add(sqlExpression.getSqlAndValue(config, values));
        }
        return "SELECT " + String.join(",", strings);
    }

    @Override
    public String getSql(Config config)
    {
        List<String> strings = new ArrayList<>(columns.size());
        for (SqlExpression sqlExpression : columns)
        {
            strings.add(sqlExpression.getSql(config));
        }
        return "SELECT " + String.join(",", strings);
    }

}
