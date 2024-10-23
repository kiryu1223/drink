package io.github.kiryu1223.drink.base.expression;

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
