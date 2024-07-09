package io.github.kiryu1223.drink.ext;

import io.github.kiryu1223.drink.core.builder.PropertyMetaData;

public class NoConverter implements IConverter<Void, Void>
{
    @Override
    public Void toDb(Void value, PropertyMetaData propertyMetaData)
    {
        throw new RuntimeException();
    }

    @Override
    public Void toJava(Void value, PropertyMetaData propertyMetaData)
    {
        throw new RuntimeException();
    }
}
