package io.github.kiryu1223.drink.base.expression;


import io.github.kiryu1223.drink.base.IConfig;

import java.util.List;

public interface ISqlJoinExpression extends ISqlExpression
{
    JoinType getJoinType();

    ISqlTableExpression getJoinTable();

    ISqlExpression getConditions();

    int getIndex();

    @Override
    default ISqlJoinExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.join(getJoinType(), getJoinTable().copy(config), getConditions().copy(config), getIndex());
    }
}
