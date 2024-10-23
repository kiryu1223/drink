package io.github.kiryu1223.drink.nnnn.expression;

import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlJoinExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTableExpression;
import io.github.kiryu1223.drink.base.expression.JoinType;
import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlJoinExpression implements ISqlJoinExpression
{
    protected final JoinType joinType;
    protected final ISqlTableExpression joinTable;
    protected final ISqlExpression conditions;
    protected final int index;

    protected SqlJoinExpression(JoinType joinType, ISqlTableExpression joinTable, ISqlExpression conditions, int index)
    {
        this.joinType = joinType;
        this.joinTable = joinTable;
        this.conditions = conditions;
        this.index = index;
    }

    @Override
    public JoinType getJoinType()
    {
        return joinType;
    }

    @Override
    public ISqlTableExpression getJoinTable()
    {
        return joinTable;
    }

    @Override
    public ISqlExpression getConditions()
    {
        return conditions;
    }

    @Override
    public int getIndex()
    {
        return index;
    }
}
