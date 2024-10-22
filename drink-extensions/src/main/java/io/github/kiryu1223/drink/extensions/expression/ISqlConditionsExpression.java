package io.github.kiryu1223.drink.extensions.expression;

import io.github.kiryu1223.drink.extensions.IConfig;

import java.util.List;

public interface ISqlConditionsExpression extends ISqlExpression
{
    List<ISqlExpression> getConditions();

    default boolean isEmpty()
    {
        return getConditions().isEmpty();
    }

    @Override
    default ISqlConditionsExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        ISqlConditionsExpression newConditions = factory.condition();
        for (ISqlExpression condition : getConditions())
        {
            newConditions.getConditions().add(condition.copy(config));
        }
        return newConditions;
    }
}
