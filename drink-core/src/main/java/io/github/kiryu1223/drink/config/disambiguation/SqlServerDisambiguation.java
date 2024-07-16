package io.github.kiryu1223.drink.config.disambiguation;

public class SqlServerDisambiguation implements IDisambiguation
{
    @Override
    public String disambiguation(String property)
    {
        return "[" + property + "]";
    }
}
