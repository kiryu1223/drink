package io.github.kiryu1223.drink.core.builder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FastCreatorFactory
{
    private static final Map<Class<?>, FastCreator<?>> cache = new ConcurrentHashMap<>();

    public <T> FastCreator<T> create(Class<T> target)
    {
        return new FastCreator<>(target);
    }

    public <T> FastCreator<T> get(Class<T> target)
    {
        FastCreator<T> fastCreator = (FastCreator<T>) cache.get(target);
        if (fastCreator == null)
        {
            fastCreator = create(target);
            cache.put(target, fastCreator);
        }
        return fastCreator;
    }
}
