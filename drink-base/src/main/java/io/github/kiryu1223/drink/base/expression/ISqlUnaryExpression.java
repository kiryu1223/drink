package io.github.kiryu1223.drink.base.expression;


import io.github.kiryu1223.drink.base.IConfig;

import java.util.List;

public interface ISqlUnaryExpression extends ISqlExpression
{
    SqlOperator getOperator();

    ISqlExpression getExpression();

    @Override
    default ISqlUnaryExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.unary(getOperator(), getExpression().copy(config));
    }
}
