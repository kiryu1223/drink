package io.github.kiryu1223.drink.extensions.expression;

import io.github.kiryu1223.drink.extensions.IConfig;

import java.util.List;

public interface ISqlBinaryExpression extends ISqlExpression
{
    ISqlExpression getLeft();
    ISqlExpression getRight();
    SqlOperator getSqlOperator();

    @Override
    default ISqlBinaryExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.binary(getSqlOperator(), getLeft().copy(config), getRight().copy(config));
    }
}
