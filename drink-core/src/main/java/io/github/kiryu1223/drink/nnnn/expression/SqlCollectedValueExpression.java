package io.github.kiryu1223.drink.nnnn.expression;

import io.github.kiryu1223.drink.base.expression.ISqlCollectedValueExpression;

import java.util.Collection;

public class SqlCollectedValueExpression implements ISqlCollectedValueExpression
{
    private final Collection<Object> collection;
    private String delimiter = ",";

    public SqlCollectedValueExpression(Collection<Object> collection)
    {
        this.collection = collection;
    }

    @Override
    public Collection<Object> getCollection()
    {
        return collection;
    }

    @Override
    public void setDelimiter(String delimiter)
    {
        this.delimiter = delimiter;
    }

    @Override
    public String getDelimiter()
    {
        return delimiter;
    }
}
