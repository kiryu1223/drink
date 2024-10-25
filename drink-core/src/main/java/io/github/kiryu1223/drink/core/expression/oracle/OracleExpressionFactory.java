package io.github.kiryu1223.drink.core.expression.oracle;

import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.expression.impl.DefaultSqlExpressionFactory;

public class OracleExpressionFactory extends DefaultSqlExpressionFactory
{
    @Override
    public ISqlFromExpression from(ISqlTableExpression sqlTable, int index)
    {
        return new OracleFromExpression(sqlTable, index);
    }

    @Override
    public ISqlJoinExpression join(JoinType joinType, ISqlTableExpression joinTable, ISqlExpression conditions, int index)
    {
        return new OracleJoinExpression(joinType, joinTable, conditions, index);
    }

    @Override
    public ISqlQueryableExpression queryable(ISqlSelectExpression select, ISqlFromExpression from, ISqlJoinsExpression joins, ISqlWhereExpression where, ISqlGroupByExpression groupBy, ISqlHavingExpression having, ISqlOrderByExpression orderBy, ISqlLimitExpression limit)
    {
        return new OracleQueryableExpression(select, from, joins, where, groupBy, having, orderBy, limit);
    }

    @Override
    public ISqlLimitExpression limit()
    {
        return new OracleLimitExpression();
    }
}
