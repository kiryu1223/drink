package io.github.kiryu1223.drink.base.toBean.beancreator;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbsBeanCreator<T>
{
    protected final IConfig config;
    protected final Class<T> target;
    protected final Supplier<T> supplier;
    protected final Map<String, ISetterCaller<T>> setters = new ConcurrentHashMap<>();
    protected final Map<String, IGetterCaller<T,?>> getters = new ConcurrentHashMap<>();

    protected AbsBeanCreator(IConfig config, Class<T> target)
    {
        this.config = config;
        this.target = target;
        this.supplier = initBeanCreator(target);
    }

    protected abstract Supplier<T> initBeanCreator(Class<T> target);

    protected abstract ISetterCaller<T> initBeanSetter(String fieldName);

    protected abstract IGetterCaller<T, ?> initBeanGetter(String fieldName);

    public Supplier<T> getBeanCreator()
    {
        return supplier;
    }

    public ISetterCaller<T> getBeanSetter(String fieldName)
    {
        ISetterCaller<T> setterCaller = setters.get(fieldName);
        if (setterCaller == null)
        {
            setterCaller = initBeanSetter(fieldName);
            setters.put(fieldName, setterCaller);
        }
        return setterCaller;
    }

    public IGetterCaller<T,?> getBeanGetter(String fieldName) {
        IGetterCaller<T, ?> getter = getters.get(fieldName);
        if (getter == null) {
            getter = initBeanGetter(fieldName);
            getters.put(fieldName, getter);
        }
        return getter;
    }
}
