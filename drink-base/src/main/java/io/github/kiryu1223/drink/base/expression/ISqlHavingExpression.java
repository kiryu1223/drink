package io.github.kiryu1223.drink.base.expression;


import io.github.kiryu1223.drink.base.IConfig;

import java.util.List;

public interface ISqlHavingExpression extends ISqlExpression
{
    ISqlConditionsExpression getConditions();

    void addCond(ISqlExpression condition);

    default boolean isEmpty()
    {
        return getConditions().isEmpty();
    }

    @Override
    default ISqlHavingExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        ISqlHavingExpression having = factory.having();
        ISqlConditionsExpression conditions = getConditions();
        for (ISqlExpression condition : conditions.getConditions())
        {
            having.addCond(condition);
        }
        return having;
    }
}
