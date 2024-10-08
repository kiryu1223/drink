package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.visitor.methods.StringMethods;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SqlCollectedValueExpression extends SqlValueExpression
{
    private final Collection<Object> collection;
    private String delimiter = ",";

    SqlCollectedValueExpression(Collection<Object> collection)
    {
        this.collection = collection;
    }

    public void setDelimiter(String delimiter)
    {
        this.delimiter = delimiter;
    }

    public Collection<Object> getCollection()
    {
        return collection;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        List<String> strings = new ArrayList<>(collection.size());
        for (Object obj : collection)
        {
            strings.add("?");
            if (values != null) values.add(obj);
        }
        return String.join(delimiter, strings);
    }

    @Override
    public <T extends SqlExpression> T copy(Config config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<Object> newValues = new ArrayList<>(collection);
        return (T) factory.value(newValues);
    }
}
