package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.metaData.FieldMetaData;

public class JsonProperty
{
    private final FieldMetaData field;
    private int index=-1;

    public JsonProperty(FieldMetaData field)
    {
        this.field=field;
    }

    public FieldMetaData getField()
    {
        return field;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public int getIndex()
    {
        return index;
    }

    public String toProperty()
    {
        StringBuilder builder=new StringBuilder();
        builder.append(field.getColumnName());
        if (index >= 0)
        {
            builder.append(String.format("[%d]",index));
        }
        return builder.toString();
    }
}
