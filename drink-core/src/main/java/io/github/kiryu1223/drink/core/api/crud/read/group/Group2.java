package io.github.kiryu1223.drink.core.api.crud.read.group;


public class Group2<Key, T1, T2> extends SqlAggregation2<T1, T2> implements IGroup
{
    public Key key;
}
