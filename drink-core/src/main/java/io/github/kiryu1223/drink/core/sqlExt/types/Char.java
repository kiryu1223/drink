package io.github.kiryu1223.drink.core.sqlExt.types;

import io.github.kiryu1223.drink.base.IConfig;

public class Char extends SqlTypes<Character>
{
    private final int length;

    public Char(int length)
    {
        this.length = length;
    }

    @Override
    public String getKeyword(IConfig config)
    {
        switch (config.getDbType())
        {
            case MySQL:
                return "CHAR(1)";
            case SQLServer:
                return "NCHAR(1)";
        }
        return String.format("CHAR(%d)", length);
    }
}
