package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SqlCollectedValueExpression extends SqlValueExpression
{
    private final Collection<Object> collection;

    public SqlCollectedValueExpression(Collection<Object> collection)
    {
        this.collection = collection;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        List<String> strings = new ArrayList<>(collection.size());
        for (Object obj : collection)
        {
            strings.add("?");
            values.add(obj);
        }
        return String.join(",", strings);
    }

    @Override
    public String getSql(Config config)
    {
        List<String> strings = new ArrayList<>(collection.size());
        for (Object obj : collection)
        {
            strings.add("?");
        }
        return String.join(",", strings);
    }
}
