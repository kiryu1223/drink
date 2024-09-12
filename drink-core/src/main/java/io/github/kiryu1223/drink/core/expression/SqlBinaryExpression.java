package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlBinaryExpression extends SqlExpression
{
    private final SqlOperator operator;
    private final SqlExpression left;
    private final SqlExpression right;

    SqlBinaryExpression(SqlOperator operator, SqlExpression left, SqlExpression right)
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
        StringBuilder sb = new StringBuilder();
        sb.append(left.getSqlAndValue(config, values));
        sb.append(" ");
        sb.append(operator.getOperator());
        sb.append(" ");
        switch (operator)
        {
            case IN:
                sb.append("(");
                sb.append(right.getSqlAndValue(config, values));
                sb.append(")");
                break;
            default:
                sb.append(right.getSqlAndValue(config, values));
                break;
        }
        return sb.toString();
    }

    @Override
    public String getSql(Config config)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(left.getSql(config));
        sb.append(" ");
        sb.append(operator.getOperator());
        sb.append(" ");
        switch (operator)
        {
            case IN:
                sb.append("(");
                sb.append(right.getSql(config));
                sb.append(")");
                break;
            default:
                sb.append(right.getSql(config));
                break;
        }
        return sb.toString();
    }

    @Override
    public <T extends SqlExpression> T copy(Config config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return (T) factory.binary(operator, left.copy(config), right.copy(config));
    }
}
