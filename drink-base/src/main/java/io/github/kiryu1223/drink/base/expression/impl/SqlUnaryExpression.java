package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlUnaryExpression;
import io.github.kiryu1223.drink.base.expression.SqlOperator;

import java.util.List;

public class SqlUnaryExpression implements ISqlUnaryExpression
{
    private final SqlOperator operator;
    private final ISqlExpression expression;

    public SqlUnaryExpression(SqlOperator operator, ISqlExpression expression)
    {
        this.operator = operator;
        this.expression = expression;
    }

    @Override
    public SqlOperator getOperator()
    {
        return operator;
    }

    @Override
    public ISqlExpression getExpression()
    {
        return expression;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<Object> values)
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
}
