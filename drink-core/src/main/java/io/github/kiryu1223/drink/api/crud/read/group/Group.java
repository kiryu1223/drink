package io.github.kiryu1223.drink.api.crud.read.group;


public class Group<Key, T> extends SqlAggregation<T> implements IGroup
{
    public Key key;
}
