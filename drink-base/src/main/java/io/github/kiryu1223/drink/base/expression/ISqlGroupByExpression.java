package io.github.kiryu1223.drink.base.expression;


import io.github.kiryu1223.drink.base.IConfig;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ISqlGroupByExpression extends ISqlExpression
{
    void setColumns(LinkedHashMap<String, ISqlExpression> columns);

    LinkedHashMap<String, ISqlExpression> getColumns();

    @Override
    default ISqlGroupByExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        ISqlGroupByExpression groupByExpression = factory.groupBy();
        for (Map.Entry<String, ISqlExpression> entry : getColumns().entrySet())
        {
            groupByExpression.getColumns().put(entry.getKey(), entry.getValue().copy(config));
        }
        return groupByExpression;
    }
}
