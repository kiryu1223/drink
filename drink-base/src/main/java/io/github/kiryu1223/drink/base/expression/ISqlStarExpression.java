package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

public interface ISqlStarExpression extends ISqlExpression
{
    ISqlTableRefExpression getTableRefExpression();

    @Override
    default ISqlStarExpression copy(IConfig config)
    {
        return config.getSqlExpressionFactory().star(getTableRefExpression());
    }
}
