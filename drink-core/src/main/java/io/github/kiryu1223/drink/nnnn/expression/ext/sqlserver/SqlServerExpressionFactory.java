package io.github.kiryu1223.drink.nnnn.expression.ext.sqlserver;

import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.core.expression.*;
import io.github.kiryu1223.drink.nnnn.expression.*;

public class SqlServerExpressionFactory extends DefaultSqlExpressionFactory
{
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
