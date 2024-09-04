package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.api.crud.read.Empty;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.ext.DbType;

import java.util.List;

public class SqlFromExpression extends SqlExpression
{
    protected final SqlTableExpression sqlTableExpression;
    protected final int index;

    protected SqlFromExpression(SqlTableExpression sqlTableExpression, int index)
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
        if (checkIsEmptyTable() && config.getDbType() != DbType.Oracle)
        {
            return "";
        }
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
        if (checkIsEmptyTable() && config.getDbType() != DbType.Oracle)
        {
            return "";
        }
        String sql;
        if (sqlTableExpression instanceof SqlRealTableExpression)
        {
            sql = sqlTableExpression.getSql(config);
        }
        else
        {
            sql = "(" + sqlTableExpression.getSql(config) + ")";
        }
        return "FROM " + sql + " AS t" + index;
    }

    private boolean checkIsEmptyTable()
    {
        return sqlTableExpression.getTableClass() == Empty.class;
    }
}
