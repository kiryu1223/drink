package io.github.kiryu1223.drink.base.tobean.typehandler;

import java.util.HashMap;
import java.util.Map;

public class TypeHandlerManager
{
    private static final Map<Class<?>, ITypeHandler<?>> cache = new HashMap<>();
    private static final UnKnowTypeHandler<?> unKnowTypeHandler = new UnKnowTypeHandler<>();

    public static <T> void set(Class<T> c, ITypeHandler<T> typeHandler)
    {
        cache.put(c, typeHandler);
    }

    public static <T> ITypeHandler<T> get(Class<T> c)
    {
        ITypeHandler<T> iTypeHandler = (ITypeHandler<T>) cache.get(c);
        if (iTypeHandler == null)
        {
            return (ITypeHandler<T>) unKnowTypeHandler;
        }
        return iTypeHandler;
    }
}
