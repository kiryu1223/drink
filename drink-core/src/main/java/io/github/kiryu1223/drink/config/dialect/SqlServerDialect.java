package io.github.kiryu1223.drink.config.dialect;

public class SqlServerDialect implements IDialect
{
    @Override
    public String disambiguation(String property)
    {
        return "[" + property + "]";
    }
}
