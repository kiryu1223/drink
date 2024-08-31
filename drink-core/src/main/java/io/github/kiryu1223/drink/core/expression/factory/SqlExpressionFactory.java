package io.github.kiryu1223.drink.core.expression.factory;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.JoinType;
import io.github.kiryu1223.drink.core.expression.*;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;

import java.util.List;

public class SqlExpressionFactory
{
    protected final Config config;

    public SqlExpressionFactory(Config config)
    {
        this.config = config;
    }

    public SqlAsExpression as(SqlExpression expression, String asName)
    {
        return new SqlAsExpression(expression, asName);
    }

    public SqlColumnExpression column(PropertyMetaData propertyMetaData, int tableIndex)
    {
        return new SqlColumnExpression(propertyMetaData, tableIndex);
    }

    public SqlConditionsExpression Condition(List<SqlExpression> conditions)
    {
        return new SqlConditionsExpression(conditions);
    }

    public SqlFromExpression from(SqlTableExpression sqlTable)
    {
        return from(sqlTable, 0);
    }

    public SqlFromExpression from(SqlTableExpression sqlTable, int index)
    {
        return new SqlFromExpression(sqlTable, index);
    }

    public SqlGroupByExpression groupBy(List<SqlColumnExpression> columns)
    {
        return new SqlGroupByExpression(columns);
    }

    public SqlHavingExpression having(SqlConditionsExpression condition)
    {
        return new SqlHavingExpression(condition);
    }

    public SqlJoinExpression join(JoinType joinType, SqlTableExpression joinTable, SqlConditionsExpression conditions, int index)
    {
        return new SqlJoinExpression(joinType,joinTable,conditions,index);
    }

    public SqlJoinsExpression Joins(List<SqlJoinExpression> joins)
    {
        return new SqlJoinsExpression(joins);
    }

    public SqlLimitExpression limit(long offset, long rows)
    {
        return new SqlLimitExpression(offset, rows);
    }

    public SqlLimitExpression limit()
    {
        return new SqlLimitExpression(0);
    }

    public SqlLimitExpression limit(long rows)
    {
        return new SqlLimitExpression(rows);
    }

    public SqlOrderByExpression orderBy(List<SqlOrderExpression> sqlOrders)
    {
        return new SqlOrderByExpression(sqlOrders);
    }

    public SqlQueryableExpression queryable(Class<?> target)
    {
        return new SqlQueryableExpression(config, from(table(target), 0));
    }

    public SqlQueryableExpression queryable(SqlFromExpression from)
    {
        return new SqlQueryableExpression(config, from);
    }

    public SqlRealTableExpression table(Class<?> tableClass)
    {
        return new SqlRealTableExpression(tableClass);
    }

    public SqlSelectExpression select(Class<?> target)
    {
        return new SqlSelectExpression(target);
    }

    public SqlSelectExpression select(List<SqlExpression> column, Class<?> target)
    {
        return new SqlSelectExpression(column, target);
    }

    public SqlSelectExpression select(List<SqlExpression> column, Class<?> target, boolean isSingle)
    {
        return new SqlSelectExpression(column, target, isSingle);
    }

    public SqlWhereExpression where(SqlConditionsExpression conditions)
    {
        return new SqlWhereExpression(conditions);
    }
}
