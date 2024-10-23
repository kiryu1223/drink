package io.github.kiryu1223.drink.base.expression;


import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.metaData.MetaDataCache;
import io.github.kiryu1223.drink.base.metaData.PropertyMetaData;

import java.util.ArrayList;
import java.util.List;

public interface ISqlQueryableExpression extends ISqlTableExpression
{
//    protected final SqlSelectExpression select;
//    protected final ISqlFromExpression from;
//    protected final SqlJoinsExpression joins;
//    protected final SqlWhereExpression where;
//    protected final SqlGroupByExpression groupBy;
//    protected final ISqlHavingExpression having;
//    protected final ISqlOrderByExpression orderBy;
//    protected final SqlLimitExpression limit;
//
//     SqlQueryableExpression(SqlSelectExpression select, ISqlFromExpression from, SqlJoinsExpression joins, SqlWhereExpression where, SqlGroupByExpression groupBy, ISqlHavingExpression having, ISqlOrderByExpression orderBy, SqlLimitExpression limit)
//    {
//        this.select = select;
//        this.from = from;
//        this.joins = joins;
//        this.where = where;
//        this.groupBy = groupBy;
//        this.having = having;
//        this.orderBy = orderBy;
//        this.limit = limit;
//    }

    @Override
    default String getSqlAndValue(IConfig config, List<Object> values)
    {
        List<String> strings = new ArrayList<>();
        strings.add(getSelect().getSqlAndValue(config, values));
        String fromSqlAndValue = getFrom().getSqlAndValue(config, values);
        if (!fromSqlAndValue.isEmpty()) strings.add(fromSqlAndValue);
        String joinsSqlAndValue = getJoins().getSqlAndValue(config, values);
        if (!joinsSqlAndValue.isEmpty()) strings.add(joinsSqlAndValue);
        String whereSqlAndValue = getWhere().getSqlAndValue(config, values);
        if (!whereSqlAndValue.isEmpty()) strings.add(whereSqlAndValue);
        String groupBySqlAndValue = getGroupBy().getSqlAndValue(config, values);
        if (!groupBySqlAndValue.isEmpty()) strings.add(groupBySqlAndValue);
        String havingSqlAndValue = getHaving().getSqlAndValue(config, values);
        if (!havingSqlAndValue.isEmpty()) strings.add(havingSqlAndValue);
        String orderBySqlAndValue = getOrderBy().getSqlAndValue(config, values);
        if (!orderBySqlAndValue.isEmpty()) strings.add(orderBySqlAndValue);
        if (!getFrom().isEmptyTable())
        {
            String limitSqlAndValue = getLimit().getSqlAndValue(config, values);
            if (!limitSqlAndValue.isEmpty()) strings.add(limitSqlAndValue);
        }
        return String.join(" ", strings);
    }

    @Override
    default ISqlQueryableExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.queryable(getSelect().copy(config), getFrom().copy(config), getJoins().copy(config), getWhere().copy(config), getGroupBy().copy(config), getHaving().copy(config), getOrderBy().copy(config), getLimit().copy(config));
    }

    @Override
    Class<?> getTableClass();

    void addWhere(ISqlExpression cond);

    void addJoin(ISqlJoinExpression join);

    void setGroup(ISqlGroupByExpression group);

    void addHaving(ISqlExpression cond);

    void addOrder(ISqlOrderExpression order);

    void setSelect(ISqlSelectExpression newSelect);

    void setLimit(long offset, long rows);

    void setDistinct(boolean distinct);

    ISqlFromExpression getFrom();

    int getOrderedCount();

    ISqlWhereExpression getWhere();

    ISqlGroupByExpression getGroupBy();

    ISqlJoinsExpression getJoins();

    ISqlSelectExpression getSelect();

    ISqlOrderByExpression getOrderBy();

    ISqlLimitExpression getLimit();

    ISqlHavingExpression getHaving();

    List<Class<?>> getOrderedClass();

    default List<PropertyMetaData> getMappingData(IConfig config)
    {
        List<Class<?>> orderedClass = getOrderedClass();
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        Class<?> target = getSelect().getTarget();
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
