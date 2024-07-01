package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.expressionTree.expressions.OperatorType;

import java.util.List;

public class SqlBinaryContext extends SqlContext
{
    private final SqlOperator operator;
    private final SqlContext left;
    private final SqlContext right;

    public SqlBinaryContext(SqlOperator operator, SqlContext left, SqlContext right)
    {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public SqlOperator getOperator()
    {
        return operator;
    }

    public SqlContext getLeft()
    {
        return left;
    }

    public SqlContext getRight()
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
