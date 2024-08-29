package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.ArrayList;
import java.util.List;

public class SqlOrderByExpression extends SqlExpression
{
    private final List<SqlOrderExpression> sqlOrders;

    public SqlOrderByExpression(List<SqlOrderExpression> sqlOrders)
    {
        this.sqlOrders = sqlOrders;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
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
        List<String> strings = new ArrayList<>(sqlOrders.size());
        for (SqlOrderExpression sqlOrder : sqlOrders)
        {
            strings.add(sqlOrder.getSql(config));
        }
        return "ORDER BY " + String.join(",", strings);
    }
}
