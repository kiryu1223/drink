package io.github.kiryu1223.drink.exception;

public class SqlCalculatesInvokeException extends RuntimeException
{
    public SqlCalculatesInvokeException()
    {
        super("SqlCalculate cannot be called from outside an expression");
    }
}
