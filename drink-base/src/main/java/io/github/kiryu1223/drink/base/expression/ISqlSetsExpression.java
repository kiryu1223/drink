package io.github.kiryu1223.drink.base.expression;


import io.github.kiryu1223.drink.base.IConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface ISqlSetsExpression extends ISqlExpression
{
    List<ISqlSetExpression> getSets();

    void addSet(ISqlSetExpression sqlSetExpression);

    void addSet(Collection<ISqlSetExpression> set);

    @Override
    default ISqlSetsExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        ISqlSetsExpression newSets = factory.sets();
        for (ISqlSetExpression set : getSets())
        {
            newSets.addSet(set.copy(config));
        }
        return newSets;
    }
}
