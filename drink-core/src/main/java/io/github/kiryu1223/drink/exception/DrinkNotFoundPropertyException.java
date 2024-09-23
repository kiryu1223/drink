package io.github.kiryu1223.drink.exception;

import java.lang.reflect.Method;

public class DrinkNotFoundPropertyException extends DrinkException
{
    public DrinkNotFoundPropertyException(Method method)
    {
        super(method.toGenericString());
    }
}
