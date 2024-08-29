package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.SqlContext;
import io.github.kiryu1223.drink.core.context.SqlOperator;

import java.util.List;

public class SqlBinaryExpression extends SqlExpression
{
    private final SqlOperator operator;
    private final SqlExpression left;
    private final SqlExpression right;

    public SqlBinaryExpression(SqlOperator operator, SqlExpression left, SqlExpression right)
    {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public SqlOperator getOperator()
    {
        return operator;
    }

    public SqlExpression getLeft()
    {
        return left;
    }

    public SqlExpression getRight()
    {
        return right;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return left.getSqlAndValue(config, values) + " " + operator.getOperator() + " " + right.getSqlAndValue(config, values);
    }

    @Override
    public String getSql(Config config)
    {
        return left.getSql(config) + " " + operator.getOperator() + " " + right.getSql(config);
    }
}
