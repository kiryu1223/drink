package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.List;

public interface ISqlOrderExpression extends ISqlExpression
{
    ISqlExpression getExpression();

    boolean isAsc();

    @Override
    default String getSqlAndValue(IConfig config, List<Object> values)
    {
        return getExpression().getSqlAndValue(config, values) + " " + (isAsc() ? "ASC" : "DESC");
    }

    @Override
    default ISqlOrderExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.order(getExpression().copy(config), isAsc());
    }
}
