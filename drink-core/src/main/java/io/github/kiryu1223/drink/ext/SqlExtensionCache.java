package io.github.kiryu1223.drink.ext;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SqlExtensionCache
{
    private static final Map<Class<? extends BaseSqlExtension>, BaseSqlExtension> sqlExtensionCache = new ConcurrentHashMap<>();

    public static BaseSqlExtension get(Class<? extends BaseSqlExtension> c)
    {
        if (!sqlExtensionCache.containsKey(c))
        {
            try
            {
                sqlExtensionCache.put(c, c.getConstructor().newInstance());
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                   NoSuchMethodException e)
            {
                throw new RuntimeException(e);
            }
        }
        return sqlExtensionCache.get(c);
    }
}
