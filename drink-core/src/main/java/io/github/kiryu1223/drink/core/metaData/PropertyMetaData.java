package io.github.kiryu1223.drink.core.metaData;

import io.github.kiryu1223.drink.annotation.Navigate;
import io.github.kiryu1223.drink.ext.IConverter;
import io.github.kiryu1223.drink.ext.NoConverter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

public class PropertyMetaData
{
    private final String property;
    private final String column;
    private final Method getter;
    private final Method setter;
    private final Field field;
    private final IConverter<?, ?> converter;
    private final boolean hasConverter;
    private final boolean ignoreColumn;
    private final Navigate navigate;

    public PropertyMetaData(String property, String column, Method getter, Method setter, Field field, IConverter<?, ?> converter, boolean ignoreColumn, Navigate navigate)
    {
        this.property = property;
        this.column = column;
        this.ignoreColumn = ignoreColumn;
        getter.setAccessible(true);
        this.getter = getter;
        setter.setAccessible(true);
        this.setter = setter;
        this.field = field;
        this.converter = converter;
        this.hasConverter = !(converter instanceof NoConverter);
        this.navigate = navigate;
    }

    public String getProperty()
    {
        return property;
    }

    public String getColumn()
    {
        return column;
    }

    public Method getGetter()
    {
        return getter;
    }

    public Method getSetter()
    {
        return setter;
    }


    public Field getField()
    {
        return field;
    }

    public IConverter<?, ?> getConverter()
    {
        return converter;
    }

    public boolean isHasConverter()
    {
        return hasConverter;
    }

    public boolean isIgnoreColumn()
    {
        return ignoreColumn;
    }

    public boolean HasNavigate()
    {
        return navigate != null;
    }

    public Navigate getNavigate()
    {
        return navigate;
    }

    public Class<?> getParentType()
    {
        return field.getDeclaringClass();
    }
}
