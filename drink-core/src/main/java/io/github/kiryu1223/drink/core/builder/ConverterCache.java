package io.github.kiryu1223.drink.core.builder;

import io.github.kiryu1223.drink.ext.IConverter;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ConverterCache
{
    private ConverterCache()
    {
    }

    private static final Map<Class<? extends IConverter<?, ?>>, IConverter<?, ?>> converterCache = new ConcurrentHashMap<>();

    public static IConverter<?, ?> get(Class<? extends IConverter<?, ?>> c)
    {
        if (!converterCache.containsKey(c))
        {
            try
            {
                converterCache.put(c, c.getConstructor().newInstance());
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                   NoSuchMethodException e)
            {
                throw new RuntimeException(e);
            }
        }
        return converterCache.get(c);
    }
}
