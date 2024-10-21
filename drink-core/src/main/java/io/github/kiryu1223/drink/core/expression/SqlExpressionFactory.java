package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class SqlExpressionFactory
{
    public SqlAsExpression as(SqlExpression expression, String asName)
    {
        return new SqlAsExpression(expression, asName);
    }

    public SqlColumnExpression column(PropertyMetaData propertyMetaData)
    {
        return column(propertyMetaData, 0);
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

    public SqlGroupByExpression groupBy()
    {
        return new SqlGroupByExpression();
    }

    public SqlGroupByExpression groupBy(LinkedHashMap<String, SqlExpression> columns)
    {
        SqlGroupByExpression groupByExpression = new SqlGroupByExpression();
        groupByExpression.setColumns(columns);
        return groupByExpression;
    }

    public SqlHavingExpression having(SqlConditionsExpression conditions)
    {
        return new SqlHavingExpression(conditions);
    }

    public SqlHavingExpression having()
    {
        return new SqlHavingExpression(condition());
    }

    public SqlJoinExpression join(JoinType joinType, SqlTableExpression joinTable, SqlExpression conditions, int index)
    {
        return new SqlJoinExpression(joinType, joinTable, conditions, index);
    }

    public SqlJoinsExpression Joins()
    {
        return new SqlJoinsExpression();
    }

    public SqlLimitExpression limit()
    {
        return new SqlLimitExpression();
    }

    public SqlLimitExpression limit(long offset, long rows)
    {
        SqlLimitExpression limit = limit();
        limit.setOffset(offset);
        limit.setRows(rows);
        return limit;
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
        return queryable(select(from.getSqlTableExpression().getTableClass()), from, Joins(), where(), groupBy(), having(), orderBy(), limit());
    }

    public SqlQueryableExpression queryable(SqlTableExpression table)
    {
        return queryable(from(table));
    }

    public SqlQueryableExpression queryable(SqlSelectExpression select, SqlFromExpression from, SqlJoinsExpression joins, SqlWhereExpression where, SqlGroupByExpression groupBy, SqlHavingExpression having, SqlOrderByExpression orderBy, SqlLimitExpression limit)
    {
        return new SqlQueryableExpression(select, from, joins, where, groupBy, having, orderBy, limit);
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
            Collection<Object> objects = (Collection<Object>) value;
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

    public SqlTemplateExpression template(List<String> templates, List<? extends SqlExpression> expressions)
    {
        return new SqlTemplateExpression(templates, expressions);
    }

    public SqlBinaryExpression binary(SqlOperator operator, SqlExpression left, SqlExpression right)
    {
        return new SqlBinaryExpression(operator, left, right);
    }

    public SqlUnaryExpression unary(SqlOperator operator, SqlExpression expression)
    {
        return new SqlUnaryExpression(operator, expression);
    }

    public SqlParensExpression parens(SqlExpression expression)
    {
        return new SqlParensExpression(expression);
    }

    public abstract SqlTypeExpression type(Class<?> c);

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
        //SqlExpressionFactory factory = config.getSqlExpressionFactory();
        MetaData metaData = MetaDataCache.getMetaData(target);
        List<PropertyMetaData> property = metaData.getNotIgnorePropertys();
        List<SqlExpression> columns = new ArrayList<>(property.size());
        for (PropertyMetaData data : property)
        {
            columns.add(column(data, 0));
        }
        return columns;
    }
}
