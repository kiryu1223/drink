package io.github.kiryu1223.drink.extensions.expression;

public enum JoinType
{
    INNER,
    LEFT,
    RIGHT,
    ;

    public String getJoin()
    {
        return name() + " JOIN";
    }
}
