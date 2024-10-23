package io.github.kiryu1223.drink.base.expression;


import io.github.kiryu1223.drink.base.IConfig;

import java.util.List;

public interface ISqlConstStringExpression extends ISqlExpression
{
    String getString();

    @Override
    default String getSqlAndValue(IConfig config, List<Object> values)
    {
        return getString();
    }

    @Override
    default ISqlConstStringExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.constString(getString());
    }
}
