package io.github.kiryu1223.drink.core.dialect;

import io.github.kiryu1223.drink.base.IDialect;

public class DefaultDialect implements IDialect
{
    @Override
    public String disambiguation(String property)
    {
        return property;
    }
}
