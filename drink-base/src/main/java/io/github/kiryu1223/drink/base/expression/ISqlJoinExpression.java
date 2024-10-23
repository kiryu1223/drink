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
    default String getSqlAndValue(IConfig config, List<Object> values)
    {
        String t = "t" + getIndex();
        return getJoinType().getJoin() + " " + (getJoinTable() instanceof ISqlRealTableExpression ? getJoinTable().getSqlAndValue(config, values) : "(" + getJoinTable().getSqlAndValue(config, values) + ")") + " AS " + config.getDisambiguation().disambiguation(t) + " ON " + getConditions().getSqlAndValue(config, values);
    }

    @Override
    default ISqlJoinExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.join(getJoinType(), getJoinTable().copy(config), getConditions().copy(config), getIndex());
    }
}
