package io.github.kiryu1223.drink.core.expression.ext.oracle;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.*;

public class OracleSqlExpressionFactory extends SqlExpressionFactory
{
    public OracleSqlExpressionFactory(Config config)
    {
        super(config);
    }

    @Override
    public SqlFromExpression from(SqlTableExpression sqlTable, int index)
    {
        return new OracleSqlFromExpression(sqlTable, index);
    }

    @Override
    public SqlTypeExpression type(Class<?> c)
    {
        return new OracleTypeExpression(c);
    }

    @Override
    public SqlJoinExpression join(JoinType joinType, SqlTableExpression joinTable, SqlExpression conditions, int index)
    {
        return new OracleJoinExpression(joinType, joinTable, conditions, index);
    }

    @Override
    public SqlQueryableExpression queryable(SqlSelectExpression select, SqlFromExpression from, SqlJoinsExpression joins, SqlWhereExpression where, SqlGroupByExpression groupBy, SqlHavingExpression having, SqlOrderByExpression orderBy, SqlLimitExpression limit)
    {
        return new OracleQueryableExpression(select, from, joins, where, groupBy, having, orderBy, limit);
    }
}
