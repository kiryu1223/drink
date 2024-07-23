package io.github.kiryu1223.drink.config.dialect;

public class DefaultDialect implements IDialect
{
    @Override
    public String disambiguation(String property)
    {
        return property;
    }
}
