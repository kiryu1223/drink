package io.github.kiryu1223.drink.extensions.expression;

import io.github.kiryu1223.drink.extensions.IConfig;

public interface ISqlRealTableExpression extends ISqlTableExpression
{
    @Override
    default ISqlRealTableExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.table(getTableClass());
    }
}
