package io.github.kiryu1223.drink.starter.configuration;

import io.github.kiryu1223.drink.base.converter.NameConverter;
import io.github.kiryu1223.drink.base.converter.SnakeNameConverter;
import io.github.kiryu1223.drink.base.toBean.beancreator.DefaultBeanCreator;

public enum NameConversionType
{
    None(new NameConverter()),
    LowerCamelCase(new SnakeNameConverter());

    private final NameConverter nameConverter;

    NameConversionType(NameConverter nameConverter)
    {
        this.nameConverter = nameConverter;
    }

    public NameConverter getNameConverter()
    {
        return nameConverter;
    }
}
