package io.github.kiryu1223.drink.core.api.crud.read;

import io.github.kiryu1223.drink.base.sqlExt.IAggregation;

public final class Aggregate<T> implements IAggregation
{
    public T table;
}
