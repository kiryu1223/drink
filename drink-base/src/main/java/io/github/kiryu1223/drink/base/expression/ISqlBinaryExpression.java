package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.List;

public interface ISqlBinaryExpression extends ISqlExpression
{
    ISqlExpression getLeft();

    ISqlExpression getRight();

    SqlOperator getOperator();

    @Override
    default String getSqlAndValue(IConfig config, List<Object> values)
    {
        SqlOperator operator = getOperator();
        StringBuilder sb = new StringBuilder();
        sb.append(getLeft().getSqlAndValue(config, values));
        sb.append(" ");
        // (= NULL) => (IS NULL)
        if (operator == SqlOperator.EQ
                && getRight() instanceof ISqlSingleValueExpression
                && ((ISqlSingleValueExpression) getRight()).getValue() == null)
        {
            sb.append(SqlOperator.IS.getOperator());
        }
        else
        {
            sb.append(operator.getOperator());
        }
        sb.append(" ");
        if (operator == SqlOperator.IN)
        {
            sb.append("(");
            sb.append(getRight().getSqlAndValue(config, values));
            sb.append(")");
        }
        else
        {
            sb.append(getRight().getSqlAndValue(config, values));
        }
        return sb.toString();
    }

    @Override
    default ISqlBinaryExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.binary(getOperator(), getLeft().copy(config), getRight().copy(config));
    }
}
