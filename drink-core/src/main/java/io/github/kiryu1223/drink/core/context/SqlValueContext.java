package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.builder.PropertyMetaData;
import io.github.kiryu1223.drink.core.visitor.SqlVisitor;
import io.github.kiryu1223.drink.ext.IConverter;
import io.github.kiryu1223.drink.ext.SqlTimeUnit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.cast;

public class SqlValueContext extends SqlContext
{
    private final Object value;

    public SqlValueContext(Object value)
    {
        this.value = value;
    }

    public Object getValue()
    {
        return value;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        if (value == null)
        {
            return "NULL";
        }
        else if (value instanceof Collection)
        {
            Collection<?> collection = (Collection<?>) value;
            List<String> strings = new ArrayList<>(collection.size());
            for (Object obj : collection)
            {
                strings.add("?");
                values.add(obj);
            }
            return String.join(",", strings);
        }
        else
        {
            values.add(value);
            return "?";
        }
    }

    public String getSqlAndValue(Config config, List<Object> values, IConverter<?,?> converter, PropertyMetaData propertyMetaData)
    {
        if (value == null)
        {
            return "NULL";
        }
        else if (value instanceof Collection)
        {
            Collection<?> collection = (Collection<?>) value;
            List<String> strings = new ArrayList<>(collection.size());
            for (Object obj : collection)
            {
                strings.add("?");
                values.add(cast(obj));
            }
            return String.join(",", strings);
        }
        else
        {
            values.add(converter.toDb(cast(value),propertyMetaData));
            return "?";
        }
    }

    @Override
    public String getSql(Config config)
    {
        if (value == null)
        {
            return "NULL";
        }
        else if (value instanceof SqlTimeUnit)
        {
            SqlTimeUnit timeUnit = (SqlTimeUnit) value;
            return timeUnit.name();
        }
        else if (value instanceof Collection)
        {
            Collection<?> collection = (Collection<?>) value;
            List<String> strings = new ArrayList<>(collection.size());
            for (Object obj : collection)
            {
                strings.add("?");
            }
            return "(" + String.join(",", strings) + ")";
        }
        else
        {
            return "?";
        }
    }
}
