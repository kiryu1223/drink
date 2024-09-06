package io.github.kiryu1223.drink.ext.types;

import io.github.kiryu1223.drink.config.Config;

public class Char extends SqlTypes<Character>
{
    private final int length;

    public Char(int length)
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
            case SqlServer:
                return "NCHAR(1)";
        }
        return String.format("CHAR(%d)", length);
    }
}
