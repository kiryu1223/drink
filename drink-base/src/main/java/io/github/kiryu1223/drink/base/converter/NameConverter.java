package io.github.kiryu1223.drink.base.converter;

public class NameConverter
{
    public String convertFieldName(String fieldName)
    {
        return fieldName;
    }

    public String convertTableName(String className)
    {
        return convertFieldName(className);
    }
}
