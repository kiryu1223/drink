package io.github.kiryu1223.drink.core.builder;

import java.lang.reflect.InvocationTargetException;

public interface ISetterCaller<T>
{
    void call(T t, Object value) throws InvocationTargetException, IllegalAccessException;
}
