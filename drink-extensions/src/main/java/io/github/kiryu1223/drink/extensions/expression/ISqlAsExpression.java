package io.github.kiryu1223.drink.extensions.expression;

import io.github.kiryu1223.drink.extensions.IConfig;

public interface ISqlAsExpression extends ISqlExpression
{
    ISqlExpression getExpression();
    String getAsName();
    @Override
    default ISqlAsExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.as(getExpression().copy(config), getAsName());
    }
}
