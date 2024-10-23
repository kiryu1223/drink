package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.metaData.MetaDataCache;
import io.github.kiryu1223.drink.base.metaData.PropertyMetaData;

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

    public ISqlGroupByExpression groupBy(LinkedHashMap<String, ISqlExpression> columns)
    {
        ISqlGroupByExpression groupByExpression = groupBy();
        groupByExpression.setColumns(columns);
        return groupByExpression;
    }

    public ISqlHavingExpression having(ISqlConditionsExpression conditions)
    {
        ISqlHavingExpression having = having();
        having.addCond(conditions);
        return having;
    }

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

    public ISqlQueryableExpression queryable(Class<?> target)
    {
        return queryable(from(table(target), 0));
    }

    public ISqlQueryableExpression queryable(Class<?> target, int offset)
    {
        return queryable(from(table(target), offset));
    }

    public ISqlQueryableExpression queryable(ISqlFromExpression from)
    {
        return queryable(select(from.getSqlTableExpression().getTableClass()), from, Joins(), where(), groupBy(), having(), orderBy(), limit());
    }

    public ISqlQueryableExpression queryable(ISqlTableExpression table)
    {
        return queryable(from(table));
    }

    public abstract ISqlQueryableExpression queryable(ISqlSelectExpression select, ISqlFromExpression from, ISqlJoinsExpression joins, ISqlWhereExpression where, ISqlGroupByExpression groupBy, ISqlHavingExpression having, ISqlOrderByExpression orderBy, ISqlLimitExpression limit);

    public abstract ISqlRealTableExpression table(Class<?> tableClass);

    public ISqlSelectExpression select(Class<?> target)
    {
        return select(getColumnByClass(target), target, false, false);
    }

    public ISqlSelectExpression select(List<ISqlExpression> column, Class<?> target)
    {
        return select(column, target, false, false);
    }

    public abstract ISqlSelectExpression select(List<ISqlExpression> column, Class<?> target, boolean isSingle, boolean isDistinct);

    public ISqlWhereExpression where()
    {
        return where(condition());
    }

    public abstract ISqlWhereExpression where(ISqlConditionsExpression conditions);

    public abstract ISqlSetExpression set(ISqlColumnExpression column, ISqlExpression value);

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

    public abstract ISqlTemplateExpression template(List<String> templates, List<? extends ISqlExpression> expressions);

    public abstract ISqlBinaryExpression binary(SqlOperator operator, ISqlExpression left, ISqlExpression right);

    public abstract ISqlUnaryExpression unary(SqlOperator operator, ISqlExpression expression);

    public abstract ISqlParensExpression parens(ISqlExpression expression);

    public abstract ISqlConstStringExpression constString(String s);

    public abstract ISqlSetsExpression sets();

    private List<ISqlExpression> getColumnByClass(Class<?> target)
    {
        MetaData metaData = MetaDataCache.getMetaData(target);
        List<PropertyMetaData> property = metaData.getNotIgnorePropertys();
        List<ISqlExpression> columns = new ArrayList<>(property.size());
        for (PropertyMetaData data : property)
        {
            columns.add(column(data, 0));
        }
        return columns;
    }
}
