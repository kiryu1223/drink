package io.github.kiryu1223.drink.core.builder;

import io.github.kiryu1223.drink.config.Config;

public class FastCreatorFactory
{
    public <T> FastCreator<T> get(Class<T> target)
    {
        return new FastCreator<>(target);
    }
}
