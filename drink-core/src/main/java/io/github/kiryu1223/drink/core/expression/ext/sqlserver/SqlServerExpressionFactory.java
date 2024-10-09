package io.github.kiryu1223.drink.core.expression.ext.sqlserver;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.*;

public class SqlServerExpressionFactory extends SqlExpressionFactory
{
    @Override
    public SqlTypeExpression type(Class<?> c)
    {
        return new SqlServerTypeExpression(c);
    }

    @Override
    public SqlLimitExpression limit()
    {
        return new SqlServerLimitExpression();
    }

    @Override
    public SqlQueryableExpression queryable(SqlSelectExpression select, SqlFromExpression from, SqlJoinsExpression joins, SqlWhereExpression where, SqlGroupByExpression groupBy, SqlHavingExpression having, SqlOrderByExpression orderBy, SqlLimitExpression limit)
    {
        return new SqlServerQueryableExpression(select, from, joins, where, groupBy, having, orderBy, limit);
    }
}
