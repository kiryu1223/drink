package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class SqlExpressionFactory
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

    public SqlConditionsExpression condition()
    {
        return new SqlConditionsExpression();
    }

    public SqlFromExpression from(SqlTableExpression sqlTable)
    {
        return from(sqlTable, 0);
    }

    public SqlFromExpression from(SqlTableExpression sqlTable, int index)
    {
        return new SqlFromExpression(sqlTable, index);
    }

    public SqlGroupByExpression groupBy(LinkedHashMap<String, SqlExpression> columns)
    {
        return new SqlGroupByExpression(columns);
    }

    public SqlHavingExpression having(SqlConditionsExpression condition)
    {
        return new SqlHavingExpression(condition);
    }

    public SqlJoinExpression join(JoinType joinType, SqlTableExpression joinTable, SqlExpression conditions, int index)
    {
        return new SqlJoinExpression(joinType, joinTable, conditions, index);
    }

    public SqlJoinsExpression Joins()
    {
        return new SqlJoinsExpression();
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

    public SqlOrderByExpression orderBy()
    {
        return new SqlOrderByExpression();
    }

    public SqlOrderExpression order(SqlExpression expression)
    {
        return order(expression, true);
    }

    public SqlOrderExpression order(SqlExpression expression, boolean asc)
    {
        return new SqlOrderExpression(expression, asc);
    }

    public SqlQueryableExpression queryable(Class<?> target)
    {
        return queryable(from(table(target), 0));
    }

    public SqlQueryableExpression queryable(Class<?> target, int offset)
    {
        return queryable(from(table(target), offset));
    }

    public SqlQueryableExpression queryable(SqlFromExpression from)
    {
        return new SqlQueryableExpression(config, from);
    }

    public SqlQueryableExpression queryable(SqlQueryableExpression queryable)
    {
        return queryable(from(queryable));
    }

    public SqlRealTableExpression table(Class<?> tableClass)
    {
        return new SqlRealTableExpression(tableClass);
    }

    public SqlSelectExpression select(Class<?> target)
    {
        return select(getColumnByClass(target), target, false);
    }

    public SqlSelectExpression select(List<SqlExpression> column, Class<?> target)
    {
        return select(column, target, false);
    }

    public SqlSelectExpression select(List<SqlExpression> column, Class<?> target, boolean isSingle)
    {
        return new SqlSelectExpression(column, target, isSingle);
    }

    public SqlWhereExpression where()
    {
        return new SqlWhereExpression(condition());
    }

    public SqlWhereExpression where(SqlConditionsExpression conditions)
    {
        return new SqlWhereExpression(conditions);
    }

    public SqlSetExpression set(SqlColumnExpression column, SqlExpression value)
    {
        return new SqlSetExpression(column, value);
    }

    public SqlValueExpression AnyValue(Object value)
    {
        if (value instanceof Collection<?>)
        {
            Collection<?> objects = (Collection<?>) value;
            return value(objects);
        }
        else
        {
            return value(value);
        }
    }

    public SqlSingleValueExpression value(Object value)
    {
        return new SqlSingleValueExpression(value);
    }

    public SqlCollectedValueExpression value(Collection<Object> value)
    {
        return new SqlCollectedValueExpression(value);
    }

    public SqlFunctionExpression function(List<String> functions, List<? extends SqlExpression> expressions)
    {
        return new SqlFunctionExpression(functions, expressions);
    }

    public SqlBinaryExpression binary(SqlOperator operator, SqlExpression left, SqlExpression right)
    {
        return new SqlBinaryExpression(operator, left, right);
    }

    public SqlBinaryExpression between(SqlExpression expression, SqlExpression min, SqlExpression max)
    {
        return binary(SqlOperator.BETWEEN, expression, binary(SqlOperator.AND, min, max));
    }

    public SqlUnaryExpression unary(SqlOperator operator, SqlExpression expression)
    {
        return new SqlUnaryExpression(operator, expression);
    }

    public SqlParensExpression parens(SqlExpression expression)
    {
        return new SqlParensExpression(expression);
    }

    public SqlTypeExpression type(Class<?> c)
    {
        return new SqlTypeExpression(c);
    }

    public SqlConstStringExpression constString(String s)
    {
        return new SqlConstStringExpression(s);
    }

    public SqlSetsExpression sets()
    {
        return new SqlSetsExpression();
    }

    private List<SqlExpression> getColumnByClass(Class<?> target)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        MetaData metaData = MetaDataCache.getMetaData(target);
        List<PropertyMetaData> property = metaData.getNotIgnorePropertys();
        List<SqlExpression> columns = new ArrayList<>(property.size());
        for (PropertyMetaData data : property)
        {
            columns.add(factory.column(data, 0));
        }
        return columns;
    }
}
