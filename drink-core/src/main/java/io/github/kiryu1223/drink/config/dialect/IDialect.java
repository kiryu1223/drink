package io.github.kiryu1223.drink.config.dialect;

public interface IDialect
{
    String disambiguation(String property);

    default String disambiguationTableName(String table)
    {
        return disambiguation(table);
    }
}
