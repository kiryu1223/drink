package io.github.kiryu1223.drink.exception;

public abstract class DrinkException extends RuntimeException
{
    public DrinkException()
    {
    }

    public DrinkException(String message)
    {
        super(message);
    }

    public DrinkException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public DrinkException(Throwable cause)
    {
        super(cause);
    }

    public DrinkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
