package io.github.kiryu1223.drink.base.toBean.beancreator;

import java.lang.reflect.InvocationTargetException;

public interface IGetterCaller<T,R> {
    R apply(T t)throws InvocationTargetException, IllegalAccessException;
}
