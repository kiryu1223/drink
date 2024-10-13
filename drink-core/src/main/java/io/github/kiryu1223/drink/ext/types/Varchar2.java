package io.github.kiryu1223.drink.ext.types;

import io.github.kiryu1223.drink.config.Config;

public class Varchar2 extends SqlTypes<String>
{
    private final int length;

    public Varchar2(int length)
    {
        this.length = length;
    }

    @Override
    public String getKeyword(Config config)
    {
        switch (config.getDbType())
        {
            case MySQL:
                return "CHAR";
            case SQLServer:
                return "VARCHAR";
        }
        return String.format("VARCHAR2(%d)", length);
    }
}
