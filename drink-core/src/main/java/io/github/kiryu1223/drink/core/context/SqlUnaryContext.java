package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlUnaryContext extends SqlContext
{
    private final SqlOperator operator;
    private final SqlContext context;

    public SqlUnaryContext(SqlOperator operator, SqlContext context)
    {
        this.operator = operator;
        this.context = context;
    }

    public SqlOperator getOperator()
    {
        return operator;
    }

    public SqlContext getContext()
    {
        return context;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        String temp = context.getSqlAndValue(config, values);
        //if (operator == SqlOperator.NOT && !(context instanceof SqlParensContext)) temp = "(" + temp + ")";
        return operator.isLeft() ? operator.getOperator() + " " + temp : temp + " " + operator.getOperator();
    }

    @Override
    public String getSql(Config config)
    {
        String temp = context.getSql(config);
        //if (operator == SqlOperator.NOT && !(context instanceof SqlParensContext)) temp = "(" + temp + ")";
        return operator.isLeft() ? operator.getOperator() + " " + temp : temp + " " + operator.getOperator();
    }
}
