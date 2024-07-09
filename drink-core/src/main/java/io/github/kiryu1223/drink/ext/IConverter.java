package io.github.kiryu1223.drink.ext;

import io.github.kiryu1223.drink.core.builder.PropertyMetaData;

public interface IConverter<J, D>
{
    D toDb(J value, PropertyMetaData propertyMetaData);

    J toJava(D value, PropertyMetaData propertyMetaData);
}
