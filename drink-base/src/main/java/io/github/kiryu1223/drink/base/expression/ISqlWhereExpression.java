package io.github.kiryu1223.drink.base.expression;


import io.github.kiryu1223.drink.base.IConfig;

import java.util.List;

public interface ISqlWhereExpression extends ISqlExpression
{
    default boolean isEmpty()
    {
        return getConditions().isEmpty();
    }

    ISqlConditionsExpression getConditions();

    @Override
    default String getSqlAndValue(IConfig config, List<Object> values)
    {
        if (isEmpty()) return "";
        return "WHERE " + getConditions().getSqlAndValue(config, values);
    }

    @Override
    default ISqlWhereExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.where(getConditions().copy(config));
    }
}
