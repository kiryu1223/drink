package io.github.kiryu1223.drink.base.expression;


import io.github.kiryu1223.drink.base.IConfig;

import java.util.List;

public interface ISqlParensExpression extends ISqlExpression
{
    ISqlExpression getExpression();

    @Override
    default String getSqlAndValue(IConfig config, List<Object> values)
    {
        return "(" + getExpression().getSqlAndValue(config, values) + ")";
    }

    @Override
    default ISqlParensExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.parens(getExpression().copy(config));
    }
}
