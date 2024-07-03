package io.github.kiryu1223.drink.core.builder;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Supplier;

public class PropertyMetaData
{
    private final String property;
    private final String column;
    private final Method getter;
    private final Method setter;

    public PropertyMetaData(String property, String column, Method getter, Method setter)
    {
        this.property = property;
        this.column = column;
        this.getter = getter;
        this.setter = setter;
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

    @Override
    public boolean equals(Object object)
    {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PropertyMetaData that = (PropertyMetaData) object;
        return Objects.equals(property, that.property) && Objects.equals(column, that.column) && Objects.equals(getter, that.getter) && Objects.equals(setter, that.setter);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(property, column, getter, setter);
    }
}
