package io.github.kiryu1223.drink.ext;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlExpression;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseSqlExtension
{
    public abstract SqlExpression parse(Config config, Method sqlFunc, List<SqlExpression> args);

    private static final Map<Class<? extends BaseSqlExtension>, BaseSqlExtension> sqlExtensionCache = new ConcurrentHashMap<>();

    public static BaseSqlExtension getCache(Class<? extends BaseSqlExtension> c)
    {
        BaseSqlExtension baseSqlExtension = sqlExtensionCache.get(c);
        if (baseSqlExtension == null)
        {
            try
            {
                baseSqlExtension = c.newInstance();
                sqlExtensionCache.put(c, baseSqlExtension);
            }
            catch (InstantiationException | IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        }
        return baseSqlExtension;
    }
}
