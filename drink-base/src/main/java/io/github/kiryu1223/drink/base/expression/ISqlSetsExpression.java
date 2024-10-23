package io.github.kiryu1223.drink.base.expression;


import io.github.kiryu1223.drink.base.IConfig;

import java.util.ArrayList;
import java.util.List;

public interface ISqlSetsExpression extends ISqlExpression
{
    List<ISqlSetExpression> getSets();

    void addSet(ISqlSetExpression sqlSetExpression);

    @Override
    default String getSqlAndValue(IConfig config, List<Object> values)
    {
        List<String> strings = new ArrayList<>(getSets().size());
        for (ISqlSetExpression expression : getSets())
        {
            strings.add(expression.getSqlAndValue(config, values));
        }
        return "SET " + String.join(",", strings);
    }

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
