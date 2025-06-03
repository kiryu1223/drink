package io.github.kiryu1223.drink.base.toBean.beancreator;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public abstract class AbsBeanCreator<T>
{
    protected final IConfig config;
    protected final Class<T> target;
    protected Supplier<T> supplier;
    protected final Map<String, ISetterCaller<T>> setters = new ConcurrentHashMap<>();
    protected final Map<String, IGetterCaller<T, ?>> getters = new ConcurrentHashMap<>();

    protected AbsBeanCreator(IConfig config, Class<T> target)
    {
        this.config = config;
        this.target = target;
        //this.supplier = initBeanCreator(target);
    }

    public void setBeanCreator(Supplier<T> supplier)
    {
        this.supplier=supplier;
    }

    public void setBeanSetter(String fieldName,ISetterCaller<T> caller)
    {
        setters.put(fieldName,caller);
    }

    public void setBeanGetter(String fieldName,IGetterCaller<T,?> caller)
    {
        getters.put(fieldName,caller);
    }

    protected abstract Supplier<T> initBeanCreator(Class<T> target);

    protected abstract ISetterCaller<T> initBeanSetter(String fieldName);

    protected abstract IGetterCaller<T, ?> initBeanGetter(String fieldName);

    public Supplier<T> getBeanCreator()
    {
        if (supplier == null)
        {
            supplier = initBeanCreator(target);
        }
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

    public IGetterCaller<T, ?> getBeanGetter(String fieldName)
    {
        IGetterCaller<T, ?> getter = getters.get(fieldName);
        if (getter == null)
        {
            getter = initBeanGetter(fieldName);
            getters.put(fieldName, getter);
        }
        return getter;
    }
}
