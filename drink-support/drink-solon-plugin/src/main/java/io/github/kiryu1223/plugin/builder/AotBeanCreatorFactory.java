package io.github.kiryu1223.plugin.builder;

import io.github.kiryu1223.drink.core.builder.AbsBeanCreator;
import io.github.kiryu1223.drink.core.builder.BeanCreatorFactory;

public class AotBeanCreatorFactory extends BeanCreatorFactory
{
    @Override
    protected <T> AbsBeanCreator<T> create(Class<T> target)
    {
        return new AotFastCreator<>(target);
    }
}
