package io.github.kiryu1223.drink.core.exception;

public class SqlFunctionInvokeException extends RuntimeException
{
    public SqlFunctionInvokeException()
    {
        super("SqlFunction cannot be called from outside an expression");
    }
}
