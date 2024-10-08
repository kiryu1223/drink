package io.github.kiryu1223.drink.converter;

import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;
import io.github.kiryu1223.drink.ext.IConverter;

public class IntConverter implements IConverter<Integer, String>
{
    @Override
    public String toDb(Integer value, PropertyMetaData propertyMetaData)
    {
        return value.toString();
    }

    @Override
    public Integer toJava(String value, PropertyMetaData propertyMetaData)
    {
        return Integer.parseInt(value);
    }
}
