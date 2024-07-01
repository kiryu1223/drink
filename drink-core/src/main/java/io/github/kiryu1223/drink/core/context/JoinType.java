package io.github.kiryu1223.drink.core.context;

public enum JoinType
{
    INNER("INNER JOIN"),
    LEFT("LEFT JOIN"),
    RIGHT("RIGHT JOIN"),
    ;

    private final String join;

    JoinType(String join)
    {
        this.join = join;
    }

    public String getJoin()
    {
        return join;
    }
}
