package io.github.kiryu1223.plugin.builder;


import io.github.kiryu1223.drink.base.toBean.beancreator.AbsBeanCreator;
import io.github.kiryu1223.drink.base.toBean.beancreator.BeanCreatorFactory;

public class AotBeanCreatorFactory extends BeanCreatorFactory
{
    @Override
    protected <T> AbsBeanCreator<T> create(Class<T> target)
    {
        return new AotFastCreator<>(target);
    }
}
