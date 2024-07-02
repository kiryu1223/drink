package io.github.kiryu1223.drink.core.builder;

import io.github.kiryu1223.drink.annotation.Column;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MetaData
{
    private final Map<String, Field> Columns = new HashMap<>();

    public MetaData(Class<?> type)
    {
        for (Field field : type.getDeclaredFields())
        {
            String name = field.getName();
            Column column = field.getAnnotation(Column.class);
            if (column == null || column.value().isEmpty())
            {
                Columns.put(name, field);
            }
            else
            {
                Columns.put(column.value(), field);
            }
        }
    }

    public Map<String, Field> getColumns()
    {
        return Columns;
    }
}
