package io.github.kiryu1223.drink.base.expression;


import io.github.kiryu1223.drink.base.IConfig;

import java.util.ArrayList;
import java.util.List;

public interface ISqlSelectExpression extends ISqlExpression
{
    List<ISqlExpression> getColumns();

    boolean isSingle();

    Class<?> getTarget();

    boolean isDistinct();

    @Override
    default String getSqlAndValue(IConfig config, List<Object> values)
    {
        List<String> strings = new ArrayList<>(getColumns().size());
        for (ISqlExpression sqlExpression : getColumns())
        {
            strings.add(sqlExpression.getSqlAndValue(config, values));
        }
        String col = String.join(",", strings);
        List<String> result = new ArrayList<>();
        result.add("SELECT");
        if (isDistinct())
        {
            result.add("DISTINCT");
        }
        result.add(col);
        return String.join(" ", result);
    }

    @Override
    default ISqlSelectExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<ISqlExpression> newColumns = new ArrayList<>(getColumns().size());
        for (ISqlExpression column : getColumns())
        {
            newColumns.add(column.copy(config));
        }
        return factory.select(newColumns, getTarget(),isSingle(), isDistinct());
    }
}
