package io.github.kiryu1223.drink.core.context;

public abstract class SqlJoinContext extends SqlContext
{
    private final JoinType joinType;
    private final SqlContext context;

    public SqlJoinContext(JoinType joinType, SqlContext context)
    {
        this.joinType = joinType;
        this.context = context;
    }

    public JoinType getJoinType()
    {
        return joinType;
    }

    public SqlContext getContext()
    {
        return context;
    }
}
