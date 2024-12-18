package io.github.kiryu1223.project.pojos;

import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;

public class GenderConverter implements IConverter<Gender, String>
{
    @Override
    public String toDb(Gender value, PropertyMetaData propertyMetaData)
    {
        return value.name();
    }

    @Override
    public Gender toJava(String value, PropertyMetaData propertyMetaData)
    {
        return Gender.valueOf(value);
    }
}
