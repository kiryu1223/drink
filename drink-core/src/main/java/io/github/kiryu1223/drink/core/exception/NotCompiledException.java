package io.github.kiryu1223.drink.core.exception;

public class NotCompiledException extends RuntimeException
{
    public NotCompiledException()
    {
        super("Please clean and recompile the project");
    }
}
