package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SqlQueryableExpression extends SqlTableExpression
{
    protected final SqlSelectExpression select;
    protected final SqlFromExpression from;
    protected final SqlJoinsExpression joins;
    protected final SqlWhereExpression where;
    protected final SqlGroupByExpression groupBy;
    protected final SqlHavingExpression having;
    protected final SqlOrderByExpression orderBy;
    protected final SqlLimitExpression limit;

    public SqlQueryableExpression(SqlSelectExpression select, SqlFromExpression from, SqlJoinsExpression joins, SqlWhereExpression where, SqlGroupByExpression groupBy, SqlHavingExpression having, SqlOrderByExpression orderBy, SqlLimitExpression limit)
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
    public String getSqlAndValue(Config config, List<Object> values)
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
        String limitSqlAndValue = limit.getSqlAndValue(config, values);
        if (!limitSqlAndValue.isEmpty()) strings.add(limitSqlAndValue);
        return String.join(" ", strings);
    }

//    public String getSqlAndValueAndFirst(Config config, List<Object> values)
//    {
//        List<String> strings = new ArrayList<>();
//        strings.add(select.getSqlAndValue(config, values));
//        String fromSqlAndValue = from.getSqlAndValue(config, values);
//        if (!fromSqlAndValue.isEmpty()) strings.add(fromSqlAndValue);
//        String joinsSqlAndValue = joins.getSqlAndValue(config, values);
//        if (!joinsSqlAndValue.isEmpty()) strings.add(joinsSqlAndValue);
//        String whereSqlAndValue = where.getSqlAndValue(config, values);
//        if (!whereSqlAndValue.isEmpty()) strings.add(whereSqlAndValue);
//        String groupBySqlAndValue = groupBy.getSqlAndValue(config, values);
//        if (!groupBySqlAndValue.isEmpty()) strings.add(groupBySqlAndValue);
//        String havingSqlAndValue = having.getSqlAndValue(config, values);
//        if (!havingSqlAndValue.isEmpty()) strings.add(havingSqlAndValue);
//        String orderBySqlAndValue = orderBy.getSqlAndValue(config, values);
//        if (!orderBySqlAndValue.isEmpty()) strings.add(orderBySqlAndValue);
//        strings.add("LIMIT 1");
//        return String.join(" ", strings);
//    }

    @Override
    public String getSql(Config config)
    {
        List<String> strings = new ArrayList<>();
        strings.add(select.getSql(config));
        String fromSql = from.getSql(config);
        if (!fromSql.isEmpty()) strings.add(fromSql);
        String joinsSql = joins.getSql(config);
        if (!joinsSql.isEmpty()) strings.add(joinsSql);
        String whereSql = where.getSql(config);
        if (!whereSql.isEmpty()) strings.add(whereSql);
        String groupBySql = groupBy.getSql(config);
        if (!groupBySql.isEmpty()) strings.add(groupBySql);
        String havingSql = having.getSql(config);
        if (!havingSql.isEmpty()) strings.add(havingSql);
        String orderBySql = orderBy.getSql(config);
        if (!orderBySql.isEmpty()) strings.add(orderBySql);
        String limitSql = limit.getSql(config);
        if (!limitSql.isEmpty()) strings.add(limitSql);
        return String.join(" ", strings);
    }

    @Override
    public <T extends SqlExpression> T copy(Config config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return (T) factory.queryable(select.copy(config), from.copy(config), joins.copy(config), where.copy(config), groupBy.copy(config), having.copy(config), orderBy.copy(config), limit.copy(config));
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
        this.select.setSingle(distinct);
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

    public SqlHavingExpression getHaving()
    {
        return having;
    }
}
