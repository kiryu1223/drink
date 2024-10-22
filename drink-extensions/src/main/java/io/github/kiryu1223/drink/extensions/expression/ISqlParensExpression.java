package io.github.kiryu1223.drink.extensions.expression;


import io.github.kiryu1223.drink.extensions.IConfig;

import java.util.List;

public interface ISqlParensExpression extends ISqlExpression
{
    ISqlExpression getExpression();

    @Override
    default ISqlParensExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.parens(getExpression().copy(config));
    }
}
