package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlUnaryExpression extends SqlExpression
{
    private final SqlOperator operator;
    private final SqlExpression expression;

    SqlUnaryExpression(SqlOperator operator, SqlExpression expression)
    {
        this.operator = operator;
        this.expression = expression;
    }

    public SqlOperator getOperator()
    {
        return operator;
    }

    public SqlExpression getExpression()
    {
        return expression;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        String temp = expression.getSqlAndValue(config, values);
        return operator.isLeft() ? operator.getOperator() + " " + temp : temp + " " + operator.getOperator();
    }

    @Override
    public String getSql(Config config)
    {
        String temp = expression.getSql(config);
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
    public <T extends SqlExpression> T copy(Config config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return (T) factory.unary(operator, expression.copy(config));
    }
}
