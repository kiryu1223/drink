package io.github.kiryu1223.drink.core.exception;

import java.lang.reflect.Method;

public class DrinkNotFoundPropertyException extends DrinkException
{
    public DrinkNotFoundPropertyException(Method method)
    {
        super(method.toGenericString());
    }

    public DrinkNotFoundPropertyException(String name)
    {
        super(name);
    }
}
