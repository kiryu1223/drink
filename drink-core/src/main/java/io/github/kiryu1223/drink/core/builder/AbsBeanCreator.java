package io.github.kiryu1223.drink.core.builder;

import java.util.function.Supplier;

public abstract class AbsBeanCreator<T>
{
    protected final Supplier<T> supplier;

    protected AbsBeanCreator(Class<T> target)
    {
        this.supplier = initBeanCreator(target);
    }

    protected abstract Supplier<T> initBeanCreator(Class<T> target);

    public Supplier<T> getBeanCreator()
    {
        return supplier;
    }
}
