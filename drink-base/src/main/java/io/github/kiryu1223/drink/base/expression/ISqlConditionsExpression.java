package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.ArrayList;
import java.util.List;

public interface ISqlConditionsExpression extends ISqlExpression
{
    List<ISqlExpression> getConditions();

    default void addCondition(ISqlExpression cond)
    {
        getConditions().add(cond);
    }

    default boolean isEmpty()
    {
        return getConditions().isEmpty();
    }

    @Override
    default String getSqlAndValue(IConfig config, List<Object> values)
    {
        List<String> whereStr = new ArrayList<>(getConditions().size());
        for (ISqlExpression expression : getConditions())
        {
            whereStr.add(expression.getSqlAndValue(config, values));
        }
        return String.join(" AND ", whereStr);
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
