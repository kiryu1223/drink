package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlOrderByExpression;
import io.github.kiryu1223.drink.base.expression.ISqlOrderExpression;

import java.util.ArrayList;
import java.util.List;

public class SqlOrderByExpression implements ISqlOrderByExpression
{
    protected final List<ISqlOrderExpression> sqlOrders = new ArrayList<>();

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

    @Override
    public String getSqlAndValue(IConfig config, List<Object> values)
    {
        if (isEmpty()) return "";
        List<String> strings = new ArrayList<>(getSqlOrders().size());
        for (ISqlOrderExpression sqlOrder : getSqlOrders())
        {
            strings.add(sqlOrder.getSqlAndValue(config, values));
        }
        return "ORDER BY " + String.join(",", strings);
    }
}
