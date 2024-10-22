package io.github.kiryu1223.drink.extensions.expression;


import io.github.kiryu1223.drink.extensions.IConfig;

public interface ISqlConstStringExpression extends ISqlExpression
{
    String getString();

    @Override
    default ISqlConstStringExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.constString(getString());
    }
}
