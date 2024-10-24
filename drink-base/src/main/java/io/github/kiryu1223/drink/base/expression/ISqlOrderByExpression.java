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
