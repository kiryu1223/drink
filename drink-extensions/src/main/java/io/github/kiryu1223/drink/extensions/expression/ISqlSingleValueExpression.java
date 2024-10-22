package io.github.kiryu1223.drink.extensions.expression;

import io.github.kiryu1223.drink.extensions.IConfig;

public interface ISqlSingleValueExpression extends ISqlValueExpression
{
    Object getValue();

    @Override
    default ISqlSingleValueExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.value(getValue());
    }
}
