package io.github.kiryu1223.drink.ext;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseSqlExtensionCache
{
    private static final Map<Class<? extends BaseSqlExtension>, BaseSqlExtension> baseSqlExtensionCache = new ConcurrentHashMap<>();

    public static BaseSqlExtension get(Class<? extends BaseSqlExtension> c)
    {
        if (!baseSqlExtensionCache.containsKey(c))
        {
            try
            {
                baseSqlExtensionCache.put(c, c.getConstructor().newInstance());
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                   NoSuchMethodException e)
            {
                throw new RuntimeException(e);
            }
        }
        return baseSqlExtensionCache.get(c);
    }
}
