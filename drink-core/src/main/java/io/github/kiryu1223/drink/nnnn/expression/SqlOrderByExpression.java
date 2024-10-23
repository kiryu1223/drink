package io.github.kiryu1223.drink.nnnn.expression;

import io.github.kiryu1223.drink.base.expression.ISqlOrderByExpression;
import io.github.kiryu1223.drink.base.expression.ISqlOrderExpression;

import java.util.ArrayList;
import java.util.List;

public class SqlOrderByExpression implements ISqlOrderByExpression
{
    protected final List<ISqlOrderExpression> sqlOrders = new ArrayList<>();

    SqlOrderByExpression()
    {
    }

    @Override
    public void addOrder(ISqlOrderExpression sqlOrder)
    {
        sqlOrders.add(sqlOrder);
    }

    @Override
    public List<ISqlOrderExpression> getSqlOrders()
    {
        return sqlOrders;
    }
}
