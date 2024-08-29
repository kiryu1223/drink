package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.SqlContext;
import io.github.kiryu1223.drink.core.context.SqlOperator;

import java.util.List;

public class SqlUnaryExpression extends SqlExpression
{
    private final SqlOperator operator;
    private final SqlExpression context;

    public SqlUnaryExpression(SqlOperator operator, SqlExpression context)
    {
        this.operator = operator;
        this.context = context;
    }

    public SqlOperator getOperator()
    {
        return operator;
    }

    public SqlExpression getContext()
    {
        return context;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        String temp = context.getSqlAndValue(config, values);
        return operator.isLeft() ? operator.getOperator() + " " + temp : temp + " " + operator.getOperator();
    }

    @Override
    public String getSql(Config config)
    {
        String temp = context.getSql(config);
        return operator.isLeft() ? operator.getOperator() + " " + temp : temp + " " + operator.getOperator();
    }
}
