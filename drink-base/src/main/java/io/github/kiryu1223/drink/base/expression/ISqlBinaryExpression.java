package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.List;

public interface ISqlBinaryExpression extends ISqlExpression
{
    ISqlExpression getLeft();

    ISqlExpression getRight();

    SqlOperator getOperator();

    @Override
    default ISqlBinaryExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.binary(getOperator(), getLeft().copy(config), getRight().copy(config));
    }
}
