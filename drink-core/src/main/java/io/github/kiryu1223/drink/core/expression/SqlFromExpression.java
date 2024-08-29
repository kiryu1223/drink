package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlFromExpression extends SqlExpression
{
    protected final SqlTableExpression sqlTableExpression;
    protected final int index;

    public SqlFromExpression(SqlTableExpression sqlTableExpression, int index)
    {
        this.sqlTableExpression = sqlTableExpression;
        this.index = index;
    }

    public SqlTableExpression getSqlTableExpression()
    {
        return sqlTableExpression;
    }

    public int getIndex()
    {
        return index;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        if (sqlTableExpression instanceof SqlRealTableExpression)
        {
            return getSql(config);
        }
        else
        {
            return "FROM (" + sqlTableExpression.getSqlAndValue(config, values) + ") AS t" + index;
        }
    }

    @Override
    public String getSql(Config config)
    {
        String sql;
        if (sqlTableExpression instanceof SqlRealTableExpression)
        {
            sql = sqlTableExpression.getSql(config);
        }
        else
        {
            sql = "(" + sqlTableExpression.getSql(config) + ")";
        }
        return "FROM "+ sql + " AS t" + index;
    }
}
