package io.github.kiryu1223.drink.config.dialect;

import io.github.kiryu1223.drink.base.IDialect;

public class OracleDialect implements IDialect
{
    @Override
    public String disambiguation(String property)
    {
        return "\"" + property + "\"";
    }
}
