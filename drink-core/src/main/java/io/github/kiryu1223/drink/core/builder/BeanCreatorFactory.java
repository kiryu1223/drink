package io.github.kiryu1223.drink.core.builder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanCreatorFactory
{
    private static final Map<Class<?>, AbsBeanCreator<?>> cache = new ConcurrentHashMap<>();

    protected <T> AbsBeanCreator<T> create(Class<T> target)
    {
        return new DefaultBeanCreator<>(target);
    }

    public <T> AbsBeanCreator<T> get(Class<T> target)
    {
        AbsBeanCreator<T> creator = (AbsBeanCreator<T>) cache.get(target);
        if (creator == null)
        {
            creator = create(target);
            cache.put(target, creator);
        }
        return creator;
    }
}
