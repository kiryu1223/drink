package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface ISqlCollectedValueExpression extends ISqlValueExpression
{
    Collection<Object> getCollection();

    void setDelimiter(String delimiter);

    String getDelimiter();

    @Override
    default String getSqlAndValue(IConfig config, List<Object> values)
    {
        List<String> strings = new ArrayList<>(getCollection().size());
        for (Object obj : getCollection())
        {
            strings.add("?");
            if (values != null) values.add(obj);
        }
        return String.join(getDelimiter(), strings);
    }

    @Override
    default ISqlCollectedValueExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<Object> newValues = new ArrayList<>(getCollection());
        ISqlCollectedValueExpression value = factory.value(newValues);
        value.setDelimiter(getDelimiter());
        return value;
    }
}
