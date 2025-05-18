package io.github.kiryu1223.drink.base.toBean.beancreator;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanCreatorFactory
{
    private static final Map<Class<?>, AbsBeanCreator<?>> cache = new ConcurrentHashMap<>();

    protected <T> AbsBeanCreator<T> create(Class<T> target, IConfig config)
    {
        return new DefaultBeanCreator<>(target,config);
    }

    public <T> AbsBeanCreator<T> get(Class<T> target,IConfig config)
    {
        AbsBeanCreator<T> creator = (AbsBeanCreator<T>) cache.get(target);
        if (creator == null)
        {
            creator = create(target,config);
            cache.put(target, creator);
        }
        return creator;
    }
}
