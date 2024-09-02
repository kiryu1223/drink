package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.factory.SqlExpressionFactory;
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

    public SqlSelectExpression(Class<?> target)
    {
        this.target = target;
        this.isSingle = false;
    }

    public SqlSelectExpression(List<SqlExpression> columns, Class<?> target)
    {
        this.columns = columns;
        this.target = target;
        this.isSingle = false;
    }

    public SqlSelectExpression(List<SqlExpression> columns, Class<?> target, boolean isSingle)
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
        getColumnByClass(config);
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
        getColumnByClass(config);
        List<String> strings = new ArrayList<>(columns.size());
        for (SqlExpression sqlExpression : columns)
        {
            strings.add(sqlExpression.getSql(config));
        }
        return "SELECT " + String.join(",", strings);
    }

    private void getColumnByClass(Config config)
    {
        if (columns == null)
        {
            SqlExpressionFactory factory = config.getSqlExpressionFactory();
            MetaData metaData = MetaDataCache.getMetaData(target);
            List<PropertyMetaData> property = metaData.getNotIgnorePropertys();
            columns = new ArrayList<>(property.size());
            for (PropertyMetaData data : property)
            {
                columns.add(factory.column(data, 0));
            }
        }
    }
}
