package io.github.kiryu1223.plugin.builder;

import io.github.kiryu1223.drink.core.builder.FastCreator;
import io.github.kiryu1223.drink.core.builder.FastCreatorFactory;

public class AotFastCreatorFactory extends FastCreatorFactory
{
    @Override
    public <T> FastCreator<T> get(Class<T> target)
    {
        return new AotFastCreator<>(target);
    }
}
