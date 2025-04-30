package io.github.kiryu1223.app.pojos;


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
