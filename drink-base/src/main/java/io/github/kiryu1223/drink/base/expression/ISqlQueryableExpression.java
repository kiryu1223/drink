package io.github.kiryu1223.drink.base.expression;


import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.metaData.MetaDataCache;
import io.github.kiryu1223.drink.base.metaData.PropertyMetaData;

import java.util.ArrayList;
import java.util.List;

public interface ISqlQueryableExpression extends ISqlTableExpression
{
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
