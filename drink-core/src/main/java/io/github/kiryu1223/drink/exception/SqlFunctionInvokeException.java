package io.github.kiryu1223.drink.exception;

public class SqlFunctionInvokeException extends RuntimeException
{
    public SqlFunctionInvokeException()
    {
        super("SqlFunction不能在表达式以外的地方被调用");
    }
}
