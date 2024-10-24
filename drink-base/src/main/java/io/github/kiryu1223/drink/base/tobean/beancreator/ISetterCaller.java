package io.github.kiryu1223.drink.base.tobean.beancreator;

import java.lang.reflect.InvocationTargetException;

public interface ISetterCaller<T>
{
    void call(T t, Object value) throws InvocationTargetException, IllegalAccessException;
}
