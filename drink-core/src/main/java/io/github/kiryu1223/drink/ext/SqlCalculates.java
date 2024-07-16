package io.github.kiryu1223.drink.ext;

import io.github.kiryu1223.drink.annotation.SqlOperatorMethod;
import io.github.kiryu1223.drink.api.crud.read.LQuery;
import io.github.kiryu1223.drink.api.crud.read.QueryBase;
import io.github.kiryu1223.drink.core.context.SqlOperator;
import io.github.kiryu1223.drink.exception.SqlCalculatesInvokeException;
import io.github.kiryu1223.drink.exception.SqlFunctionInvokeException;

import java.util.Collection;

public class SqlCalculates
{
    @SqlOperatorMethod(SqlOperator.PLUS)
    public static <T> T plus(T t1, T t2)
    {
        boom();
        return (T) new Object();
    }

    @SqlOperatorMethod(SqlOperator.MINUS)
    public static <T> T minus(T t1, T t2)
    {
        boom();
        return (T) new Object();
    }

    @SqlOperatorMethod(SqlOperator.MUL)
    public static <T> T mul(T t1, T t2)
    {
        boom();
        return (T) new Object();
    }

    @SqlOperatorMethod(SqlOperator.DIV)
    public static <T> T div(T t1, T t2)
    {
        boom();
        return (T) new Object();
    }

    @SqlOperatorMethod(SqlOperator.MOD)
    public static <T> T mod(T t1, T t2)
    {
        boom();
        return (T) new Object();
    }

    @SqlOperatorMethod(SqlOperator.EQ)
    public static <T> boolean eq(T t1, T t2)
    {
        boom();
        return false;
    }

    @SqlOperatorMethod(SqlOperator.NE)
    public static <T> boolean ne(T t1, T t2)
    {
        boom();
        return false;
    }

    @SqlOperatorMethod(SqlOperator.GT)
    public static <T> boolean gt(T t1, T t2)
    {
        boom();
        return false;
    }

    @SqlOperatorMethod(SqlOperator.LT)
    public static <T> boolean lt(T t1, T t2)
    {
        boom();
        return false;
    }

    @SqlOperatorMethod(SqlOperator.GE)
    public static <T> boolean ge(T t1, T t2)
    {
        boom();
        return false;
    }

    @SqlOperatorMethod(SqlOperator.LE)
    public static <T> boolean LE(T t1, T t2)
    {
        boom();
        return false;
    }

    @SqlOperatorMethod(SqlOperator.LIKE)
    public static <T> boolean like(T t1, T t2)
    {
        boom();
        return false;
    }

    @SqlOperatorMethod(SqlOperator.IN)
    public static <T> boolean in(T t1, Collection<T> ts)
    {
        boom();
        return false;
    }


    @SqlOperatorMethod(SqlOperator.IN)
    public static <T> boolean in(T t1, LQuery<T> query)
    {
        boom();
        return false;
    }

    @SqlOperatorMethod(SqlOperator.BETWEEN)
    public static <T> boolean between(T t, T min, T max)
    {
        boom();
        return false;
    }

    private static void boom()
    {
        if (win) // if win we win always
        {
            throw new SqlCalculatesInvokeException();
        }
    }

    private static final boolean win = true;
}
