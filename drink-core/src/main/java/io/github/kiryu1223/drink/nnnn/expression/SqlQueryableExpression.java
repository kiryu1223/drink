package io.github.kiryu1223.drink.nnnn.expression;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SqlQueryableExpression extends SqlTableExpression implements ISqlQueryableExpression
{
    protected final ISqlSelectExpression select;
    protected final ISqlFromExpression from;
    protected final ISqlJoinsExpression joins;
    protected final ISqlWhereExpression where;
    protected final ISqlGroupByExpression groupBy;
    protected final ISqlHavingExpression having;
    protected final ISqlOrderByExpression orderBy;
    protected final ISqlLimitExpression limit;

    public SqlQueryableExpression(ISqlSelectExpression select, ISqlFromExpression from, ISqlJoinsExpression joins, ISqlWhereExpression where, ISqlGroupByExpression groupBy, ISqlHavingExpression having, ISqlOrderByExpression orderBy, ISqlLimitExpression limit)
    {
        this.select = select;
        this.from = from;
        this.joins = joins;
        this.where = where;
        this.groupBy = groupBy;
        this.having = having;
        this.orderBy = orderBy;
        this.limit = limit;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<Object> values)
    {
        List<String> strings = new ArrayList<>();
        strings.add(select.getSqlAndValue(config, values));
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
        if (!from.isEmptyTable())
        {
            String limitSqlAndValue = limit.getSqlAndValue(config, values);
            if (!limitSqlAndValue.isEmpty()) strings.add(limitSqlAndValue);
        }
        return String.join(" ", strings);
    }

    @Override
    public Class<?> getTableClass()
    {
        return select.getTarget();
    }

    public void addWhere(ISqlExpression cond)
    {
        where.addCond(cond);
    }

    public void addJoin(ISqlJoinExpression join)
    {
        joins.addJoin(join);
    }

    public void setGroup(ISqlGroupByExpression group)
    {
        groupBy.setColumns(group.getColumns());
    }

    public void addHaving(ISqlExpression cond)
    {
        having.addCond(cond);
    }

    public void addOrder(ISqlOrderExpression order)
    {
        orderBy.addOrder(order);
    }

    public void setSelect(ISqlSelectExpression newSelect)
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
        select.setDistinct(distinct);
    }

    public ISqlFromExpression getFrom()
    {
        return from;
    }

    public int getOrderedCount()
    {
        return 1 + joins.getJoins().size();
    }

    public ISqlWhereExpression getWhere()
    {
        return where;
    }

    public ISqlGroupByExpression getGroupBy()
    {
        return groupBy;
    }

    public ISqlJoinsExpression getJoins()
    {
        return joins;
    }

    public ISqlSelectExpression getSelect()
    {
        return select;
    }

    public ISqlOrderByExpression getOrderBy()
    {
        return orderBy;
    }

    public ISqlLimitExpression getLimit()
    {
        return limit;
    }

    @Override
    public ISqlHavingExpression getHaving()
    {
        return having;
    }

    public List<Class<?>> getOrderedClass()
    {
        Class<?> tableClass = getTableClass();
        List<Class<?>> collect = joins.getJoins().stream().map(j -> j.getJoinTable().getTableClass()).collect(Collectors.toList());
        collect.add(0, tableClass);
        return collect;
    }
}
