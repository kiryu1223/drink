package io.github.kiryu1223.drink.base.transform;

import io.github.kiryu1223.drink.base.IConfig;

public abstract class Transform {
    protected final IConfig config;

    public Transform(IConfig config) {
        this.config = config;
    }
}
