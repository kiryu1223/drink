package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SqlQueryableExpression extends SqlTableExpression
{
    private final SqlSelectExpression select;
    private final SqlFromExpression from;
    private final SqlJoinsExpression joins;
    private final SqlWhereExpression where;
    private final SqlGroupByExpression groupBy;
    private final SqlHavingExpression having;
    private final SqlOrderByExpression orderBy;
    private final SqlLimitExpression limit;
    private boolean distinct = false;

    SqlQueryableExpression(Config config, SqlFromExpression from)
    {
        this.from = from;
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        this.select = factory.select(from.getSqlTableExpression().getTableClass());
        this.joins = factory.Joins();
        this.where = factory.where();
        this.groupBy = factory.groupBy(new LinkedHashMap<>());
        this.having = factory.having(factory.condition());
        this.orderBy = factory.orderBy();
        this.limit = factory.limit();
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        List<String> strings = new ArrayList<>();
        strings.add(select.getSqlAndValue(config, values));
        if (distinct)
        {
            strings.add("DISTINCT");
        }
        String fromSqlAndValue = from.getSqlAndValue(config, values);
        if (!fromSqlAndValue.isEmpty()) strings.add(fromSqlAndValue);
        String joinsSqlAndValue = joins.getSqlAndValue(config, values);
        if (!joinsSqlAndValue.isEmpty()) strings.add(joinsSqlAndValue);
        String whereSqlAndValue = where.getSqlAndValue(config, values);
        if (!whereSqlAndValue.isEmpty()) strings.add(whereSqlAndValue);
        String groupBySqlAndValue = groupBy.getSqlAndValue(config, values);
        if (!groupBySqlAndValue.isEmpty()) strings.add(groupBySqlAndValue);
        String havingSqlAndValue = having.getSqlAndValue(config, values);
        if (!havingSqlAndValue.isEmpty()) strings.add(havingSqlAndValue);
        String orderBySqlAndValue = orderBy.getSqlAndValue(config, values);
        if (!orderBySqlAndValue.isEmpty()) strings.add(orderBySqlAndValue);
        String limitSqlAndValue = limit.getSqlAndValue(config, values);
        if (!limitSqlAndValue.isEmpty()) strings.add(limitSqlAndValue);
        return String.join(" ", strings);
    }

    @Override
    public String getSql(Config config)
    {
        List<String> strings = new ArrayList<>();
        strings.add(select.getSql(config));
        if (distinct)
        {
            strings.add("DISTINCT");
        }
        String fromSqlAndValue = from.getSql(config);
        if (!fromSqlAndValue.isEmpty()) strings.add(fromSqlAndValue);
        String joinsSqlAndValue = joins.getSql(config);
        if (!joinsSqlAndValue.isEmpty()) strings.add(joinsSqlAndValue);
        String whereSqlAndValue = where.getSql(config);
        if (!whereSqlAndValue.isEmpty()) strings.add(whereSqlAndValue);
        String groupBySqlAndValue = groupBy.getSql(config);
        if (!groupBySqlAndValue.isEmpty()) strings.add(groupBySqlAndValue);
        String havingSqlAndValue = having.getSql(config);
        if (!havingSqlAndValue.isEmpty()) strings.add(havingSqlAndValue);
        String orderBySqlAndValue = orderBy.getSql(config);
        if (!orderBySqlAndValue.isEmpty()) strings.add(orderBySqlAndValue);
        String limitSqlAndValue = limit.getSql(config);
        if (!limitSqlAndValue.isEmpty()) strings.add(limitSqlAndValue);
        return String.join(" ", strings);
    }

    @Override
    public Class<?> getTableClass()
    {
        return select.getTarget();
    }

    public void addWhere(SqlExpression cond)
    {
        where.addCond(cond);
    }

    public void addJoin(SqlJoinExpression join)
    {
        joins.addJoin(join);
    }

    public void setGroup(SqlGroupByExpression group)
    {
        groupBy.setColumns(group.getColumns());
    }

    public void addHaving(SqlExpression cond)
    {
        having.addCond(cond);
    }

    public void addOrder(SqlOrderExpression order)
    {
        orderBy.addOrder(order);
    }

    public void setSelect(SqlSelectExpression newSelect)
    {
        select.setColumns(newSelect.getColumns());
        select.setTarget(newSelect.getTarget());
        select.setSingle(newSelect.isSingle());
    }

    public void setLimit(long offset, long rows)
    {
        limit.setOffset(offset);
        limit.setRows(rows);
    }

    public void setDistinct(boolean distinct)
    {
        this.distinct = distinct;
    }

    public SqlFromExpression getFrom()
    {
        return from;
    }

    public int getOrderedCount()
    {
        return 1 + joins.getJoins().size();
    }

    public SqlWhereExpression getWhere()
    {
        return where;
    }

    public SqlGroupByExpression getGroupBy()
    {
        return groupBy;
    }

    public SqlJoinsExpression getJoins()
    {
        return joins;
    }

    public SqlSelectExpression getSelect()
    {
        return select;
    }

    public SqlOrderByExpression getOrderBy()
    {
        return orderBy;
    }

    public SqlLimitExpression getLimit()
    {
        return limit;
    }

    public List<Class<?>> getOrderedClass()
    {
        Class<?> tableClass = getTableClass();
        List<Class<?>> collect = joins.getJoins().stream().map(j -> j.getJoinTable().getTableClass()).collect(Collectors.toList());
        collect.add(0, tableClass);
        return collect;
    }

    public List<PropertyMetaData> getMappingData(Config config)
    {
        List<Class<?>> orderedClass = getOrderedClass();
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        Class<?> target = select.getTarget();
        MetaData metaData = MetaDataCache.getMetaData(target);
        if (orderedClass.contains(target))
        {
            return metaData.getNotIgnorePropertys();
        }
        else
        {
            List<PropertyMetaData> propertyMetaDataList = new ArrayList<>();
            for (PropertyMetaData sel : metaData.getNotIgnorePropertys())
            {
                GOTO:
                for (MetaData data : MetaDataCache.getMetaData(getOrderedClass()))
                {
                    for (PropertyMetaData noi : data.getNotIgnorePropertys())
                    {
                        if (noi.getColumn().equals(sel.getColumn()) && noi.getType().equals(sel.getType()))
                        {
                            propertyMetaDataList.add(sel);
                            break GOTO;
                        }
                    }
                }
            }
            return propertyMetaDataList;
        }
    }
}
