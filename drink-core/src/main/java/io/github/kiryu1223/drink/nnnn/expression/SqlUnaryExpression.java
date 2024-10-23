package io.github.kiryu1223.drink.nnnn.expression;

import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlUnaryExpression;
import io.github.kiryu1223.drink.base.expression.SqlOperator;
import io.github.kiryu1223.drink.config.Config;

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
}
