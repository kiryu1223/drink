package io.github.kiryu1223.drink.base.toBean.beancreator;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanCreatorFactory
{
    private final IConfig config;

    private static final Map<Class<?>, AbsBeanCreator<?>> cache = new ConcurrentHashMap<>();

    public BeanCreatorFactory(IConfig config)
    {
        this.config = config;
    }

    protected <T> AbsBeanCreator<T> create(Class<T> target)
    {
        return new DefaultBeanCreator<>(target,config);
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
