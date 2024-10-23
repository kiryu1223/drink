package io.github.kiryu1223.drink.ext.types;

import io.github.kiryu1223.drink.base.sqlext.ISqlKeywords;

public abstract class SqlTypes<T> implements ISqlKeywords
{
    public static Varchar2 varchar2()
    {
        return new Varchar2(255);
    }

    public static Varchar2 varchar2(int length)
    {
        return new Varchar2(length);
    }

    public static Char Char()
    {
        return new Char(4);
    }

    public static Char Char(int length)
    {
        return new Char(length);
    }
}
