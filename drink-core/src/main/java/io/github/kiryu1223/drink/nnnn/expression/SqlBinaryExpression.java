package io.github.kiryu1223.drink.nnnn.expression;

import io.github.kiryu1223.drink.base.expression.ISqlBinaryExpression;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.SqlOperator;
import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlBinaryExpression implements ISqlBinaryExpression
{
    private final SqlOperator operator;
    private final ISqlExpression left;
    private final ISqlExpression right;

    public SqlBinaryExpression(SqlOperator operator, ISqlExpression left, ISqlExpression right)
    {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public SqlOperator getOperator()
    {
        return operator;
    }

    @Override
    public ISqlExpression getLeft()
    {
        return left;
    }

    @Override
    public ISqlExpression getRight()
    {
        return right;
    }
}
