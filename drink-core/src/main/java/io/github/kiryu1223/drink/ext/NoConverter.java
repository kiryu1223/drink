package io.github.kiryu1223.drink.ext;

import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;

public class NoConverter implements IConverter<Object, Object>
{
    @Override
    public Object toDb(Object value, PropertyMetaData propertyMetaData)
    {
        return value;
    }

    @Override
    public Object toJava(Object value, PropertyMetaData propertyMetaData)
    {
        return value;
    }
}
