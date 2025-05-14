package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.util.DrinkUtil;

import java.util.Objects;

public class AsName {
    private final String name;
    private String displayName;

    public AsName(Class<?> type) {
        this.name= DrinkUtil.getFirst(type);
    }

    public AsName(String name) {
        this.name = name;
    }

    public String getDisplayName()
    {
        if (displayName == null)
        {

        }
        else
        {
            return displayName;
        }
    }
}
