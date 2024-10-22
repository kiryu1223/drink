package io.github.kiryu1223.drink.extensions.expression;

import io.github.kiryu1223.drink.extensions.metaData.PropertyMetaData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class SqlExpressionFactory
{
    public abstract ISqlAsExpression as(ISqlExpression expression, String asName);

    public ISqlColumnExpression column(PropertyMetaData propertyMetaData)
    {
        return column(propertyMetaData, 0);
    }

    public abstract ISqlColumnExpression column(PropertyMetaData propertyMetaData, int tableIndex);

    public abstract ISqlConditionsExpression condition();

    public ISqlFromExpression from(ISqlTableExpression sqlTable)
    {
        return from(sqlTable, 0);
    }

    public abstract ISqlFromExpression from(ISqlTableExpression sqlTable, int index);

    public abstract ISqlGroupByExpression groupBy();

    public SqlGroupByExpression groupBy(LinkedHashMap<String, SqlExpression> columns)
    {
        SqlGroupByExpression groupByExpression = new SqlGroupByExpression();
        groupByExpression.setColumns(columns);
        return groupByExpression;
    }

    public abstract ISqlHavingExpression having(ISqlConditionsExpression conditions);

    public abstract ISqlHavingExpression having();

    public abstract ISqlJoinExpression join(JoinType joinType, ISqlTableExpression joinTable, ISqlExpression conditions, int index);

    public abstract ISqlJoinsExpression Joins();

    public abstract ISqlLimitExpression limit();

    public ISqlLimitExpression limit(long offset, long rows)
    {
        ISqlLimitExpression limit = limit();
        limit.setOffset(offset);
        limit.setRows(rows);
        return limit;
    }

    public abstract ISqlOrderByExpression orderBy();

    public ISqlOrderExpression order(ISqlExpression expression)
    {
        return order(expression, true);
    }

    public abstract ISqlOrderExpression order(ISqlExpression expression, boolean asc);

    public SqlQueryableExpression queryable(Class<?> target)
    {
        return queryable(from(table(target), 0));
    }

    public SqlQueryableExpression queryable(Class<?> target, int offset)
    {
        return queryable(from(table(target), offset));
    }

    public SqlQueryableExpression queryable(ISqlFromExpression from)
    {
        return queryable(select(from.getSqlTableExpression().getTableClass()), from, Joins(), where(), groupBy(), having(), orderBy(), limit());
    }

    public SqlQueryableExpression queryable(ISqlTableExpression table)
    {
        return queryable(from(table));
    }

    public SqlQueryableExpression queryable(SqlSelectExpression select, ISqlFromExpression from, SqlJoinsExpression joins, SqlWhereExpression where, SqlGroupByExpression groupBy, ISqlHavingExpression having, ISqlOrderByExpression orderBy, SqlLimitExpression limit)
    {
        return new SqlQueryableExpression(select, from, joins, where, groupBy, having, orderBy, limit);
    }

    public ISqlRealTableExpression table(Class<?> tableClass)
    {
        return new ISqlRealTableExpression(tableClass);
    }

    public SqlSelectExpression select(Class<?> target)
    {
        return select(getColumnByClass(target), target, false);
    }

    public SqlSelectExpression select(List<SqlExpression> column, Class<?> target)
    {
        return select(column, target, false);
    }

    public abstract ISqlSelectExpression select(List<ISqlExpression> column, Class<?> target, boolean isSingle,boolean isDistinct);

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

    public ISqlValueExpression AnyValue(Object value)
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

    public abstract ISqlSingleValueExpression value(Object value);

    public abstract ISqlCollectedValueExpression value(Collection<Object> value);

    public SqlTemplateExpression template(List<String> templates, List<? extends SqlExpression> expressions)
    {
        return new SqlTemplateExpression(templates, expressions);
    }

    public abstract ISqlBinaryExpression binary(SqlOperator operator, ISqlExpression left, ISqlExpression right);

    public SqlUnaryExpression unary(SqlOperator operator, SqlExpression expression)
    {
        return new SqlUnaryExpression(operator, expression);
    }

    public abstract ISqlParensExpression parens(ISqlExpression expression);

    public abstract SqlTypeExpression type(Class<?> c);

    public abstract ISqlConstStringExpression constString(String s);

    public SqlSetsExpression sets()
    {
        return new SqlSetsExpression();
    }

    private List<SqlExpression> getColumnByClass(Class<?> target)
    {
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
