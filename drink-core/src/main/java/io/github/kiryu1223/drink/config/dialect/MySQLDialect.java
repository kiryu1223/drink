package io.github.kiryu1223.drink.config.dialect;

public class MySQLDialect implements IDialect
{
    @Override
    public String disambiguation(String property)
    {
        return "`" + property + "`";
    }
}
