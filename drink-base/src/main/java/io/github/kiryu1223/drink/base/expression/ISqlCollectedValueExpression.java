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
    default ISqlCollectedValueExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<Object> newValues = new ArrayList<>(getCollection());
        ISqlCollectedValueExpression value = factory.value(newValues);
        value.setDelimiter(getDelimiter());
        return value;
    }
}
