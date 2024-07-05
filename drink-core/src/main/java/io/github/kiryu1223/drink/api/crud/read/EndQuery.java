package io.github.kiryu1223.drink.api.crud.read;

import java.util.List;

public class EndQuery<T> extends QueryBase
{
    public EndQuery(QueryBase queryBase)
    {
        super(queryBase.getSqlBuilder());
        setSingle(true);
    }

    @Override
    public boolean any()
    {
        return super.any();
    }

    @Override
    public List<T> toList()
    {
        return super.toList();
    }
}
