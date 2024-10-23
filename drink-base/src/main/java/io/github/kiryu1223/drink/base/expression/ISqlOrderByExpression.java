package io.github.kiryu1223.drink.base.expression;


import io.github.kiryu1223.drink.base.IConfig;

import java.util.ArrayList;
import java.util.List;

public interface ISqlOrderByExpression extends ISqlExpression
{
    void addOrder(ISqlOrderExpression sqlOrder);

    List<ISqlOrderExpression> getSqlOrders();

    default boolean isEmpty()
    {
        return getSqlOrders().isEmpty();
    }

    @Override
    default String getSqlAndValue(IConfig config, List<Object> values)
    {
        if (isEmpty()) return "";
        List<String> strings = new ArrayList<>(getSqlOrders().size());
        for (ISqlOrderExpression sqlOrder : getSqlOrders())
        {
            strings.add(sqlOrder.getSqlAndValue(config, values));
        }
        return "ORDER BY " + String.join(",", strings);
    }

    @Override
    default ISqlOrderByExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        ISqlOrderByExpression sqlOrderByExpression = factory.orderBy();
        for (ISqlOrderExpression sqlOrder : getSqlOrders())
        {
            sqlOrderByExpression.addOrder(sqlOrder.copy(config));
        }
        return sqlOrderByExpression;
    }
}
