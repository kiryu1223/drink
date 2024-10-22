package io.github.kiryu1223.drink.extensions.expression;


import io.github.kiryu1223.drink.extensions.IConfig;

import java.util.List;

public interface ISqlHavingExpression extends ISqlExpression
{
    ISqlConditionsExpression getConditions();

    @Override
    default String getSqlAndValue(IConfig config, List<Object> values)
    {
        if (getConditions().isEmpty()) return "";
        return "HAVING " + getConditions().getSqlAndValue(config, values);
    }

    @Override
    default ISqlHavingExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.having(getConditions().copy(config));
    }
}
