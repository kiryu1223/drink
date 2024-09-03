package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.ArrayList;
import java.util.List;

public class SqlOrderByExpression extends SqlExpression
{
    protected final List<SqlOrderExpression> sqlOrders = new ArrayList<>();

    SqlOrderByExpression()
    {
    }

    public void addOrder(SqlOrderExpression sqlOrder)
    {
        sqlOrders.add(sqlOrder);
    }

    public List<SqlOrderExpression> getSqlOrders()
    {
        return sqlOrders;
    }

    public boolean isEmpty()
    {
        return sqlOrders.isEmpty();
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        if (sqlOrders.isEmpty()) return "";
        List<String> strings = new ArrayList<>(sqlOrders.size());
        for (SqlOrderExpression sqlOrder : sqlOrders)
        {
            strings.add(sqlOrder.getSqlAndValue(config, values));
        }
        return "ORDER BY " + String.join(",", strings);
    }

    @Override
    public String getSql(Config config)
    {
        if (sqlOrders.isEmpty()) return "";
        List<String> strings = new ArrayList<>(sqlOrders.size());
        for (SqlOrderExpression sqlOrder : sqlOrders)
        {
            strings.add(sqlOrder.getSql(config));
        }
        return "ORDER BY " + String.join(",", strings);
    }
}
