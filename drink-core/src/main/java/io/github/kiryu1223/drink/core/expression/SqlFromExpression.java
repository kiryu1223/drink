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

    public boolean isEmptyTable()
    {
        return sqlTableExpression.getTableClass() == Empty.class;
    }

    public int getIndex()
    {
        return index;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        if (isEmptyTable() && config.getDbType() != DbType.Oracle)
        {
            return "";
        }
        String sql;
        if (sqlTableExpression instanceof SqlRealTableExpression)
        {
            sql = sqlTableExpression.getSqlAndValue(config, values);
        }
        else
        {
            sql = "(" + sqlTableExpression.getSqlAndValue(config, values) + ")";
        }
        return "FROM " + sql + " AS t" + index;
    }

    @Override
    public <T extends SqlExpression> T copy(Config config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return (T) factory.from(sqlTableExpression.copy(config), index);
    }
}
