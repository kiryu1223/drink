package io.github.kiryu1223.drink.core.metaData;

import io.github.kiryu1223.drink.ext.IConverter;
import io.github.kiryu1223.drink.ext.NoConverter;

import java.lang.reflect.Method;
import java.util.Objects;

public class PropertyMetaData
{
    private final String property;
    private final String column;
    private final Method getter;
    private final Method setter;
    private final Class<?> type;
    private final IConverter<?, ?> converter;
    private final boolean hasConverter;

    public PropertyMetaData(String property, String column, Method getter, Method setter, Class<?> type, IConverter<?, ?> converter)
    {
        this.property = property;
        this.column = column;
        getter.setAccessible(true);
        this.getter = getter;
        setter.setAccessible(true);
        this.setter = setter;
        this.type = type;
        this.converter = converter;
        this.hasConverter = !(converter instanceof NoConverter);
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

    public Class<?> getType()
    {
        return type;
    }

    public IConverter<?, ?> getConverter()
    {
        return converter;
    }

    public boolean isHasConverter()
    {
        return hasConverter;
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PropertyMetaData metaData = (PropertyMetaData) object;
        return hasConverter == metaData.hasConverter && Objects.equals(property, metaData.property) && Objects.equals(column, metaData.column) && Objects.equals(getter, metaData.getter) && Objects.equals(setter, metaData.setter) && Objects.equals(type, metaData.type) && Objects.equals(converter, metaData.converter);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(property, column, getter, setter, type, converter, hasConverter);
    }

    @Override
    public String toString()
    {
        return "PropertyMetaData{" +
                "property='" + property + '\'' +
                ", column='" + column + '\'' +
                ", getter=" + getter +
                ", setter=" + setter +
                ", type=" + type +
                ", converter=" + converter +
                ", hasConverter=" + hasConverter +
                '}';
    }
}
