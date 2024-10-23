package io.github.kiryu1223.drink.base;

public interface IDialect
{
    String disambiguation(String property);

    default String disambiguationTableName(String table)
    {
        return disambiguation(table);
    }
}
