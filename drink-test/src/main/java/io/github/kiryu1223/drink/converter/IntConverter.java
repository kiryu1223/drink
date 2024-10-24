package io.github.kiryu1223.drink.converter;


import io.github.kiryu1223.drink.base.metaData.IConverter;
import io.github.kiryu1223.drink.base.metaData.PropertyMetaData;

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
