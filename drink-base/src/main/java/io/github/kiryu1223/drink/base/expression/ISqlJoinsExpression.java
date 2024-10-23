package io.github.kiryu1223.drink.base.expression;


import io.github.kiryu1223.drink.base.IConfig;

import java.util.ArrayList;
import java.util.List;

public interface ISqlJoinsExpression extends ISqlExpression
{
    void addJoin(ISqlJoinExpression join);

    List<ISqlJoinExpression> getJoins();

    default boolean isEmpty()
    {
        return getJoins().isEmpty();
    }

    @Override
    default String getSqlAndValue(IConfig config, List<Object> values)
    {
        if (getJoins().isEmpty()) return "";
        List<String> strings = new ArrayList<>(getJoins().size());
        for (ISqlJoinExpression join : getJoins())
        {
            strings.add(join.getSqlAndValue(config,values));
        }
        return String.join(" ", strings);
    }

    @Override
    default ISqlJoinsExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        ISqlJoinsExpression newJoins = factory.Joins();
        for (ISqlJoinExpression join : getJoins())
        {
            newJoins.addJoin(join.copy(config));
        }
        return newJoins;
    }
}
