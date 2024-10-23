package io.github.kiryu1223.drink.base.expression;


import io.github.kiryu1223.drink.base.IConfig;

import java.util.List;

public interface ISqlUnaryExpression extends ISqlExpression
{
    SqlOperator getOperator();

    ISqlExpression getExpression();

    @Override
    default String getSqlAndValue(IConfig config, List<Object> values)
    {
        SqlOperator operator = getOperator();
        String temp = getExpression().getSqlAndValue(config, values);
        String res;
        if (operator.isLeft())
        {
            res = operator.getOperator() + " " + temp;
        }
        else
        {
            res = temp + " " + operator.getOperator();
        }
        return res;
    }

    @Override
    default ISqlUnaryExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.unary(getOperator(), getExpression().copy(config));
    }
}
