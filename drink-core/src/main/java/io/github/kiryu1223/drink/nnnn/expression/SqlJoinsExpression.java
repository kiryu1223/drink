package io.github.kiryu1223.drink.nnnn.expression;

import io.github.kiryu1223.drink.base.expression.ISqlJoinExpression;
import io.github.kiryu1223.drink.base.expression.ISqlJoinsExpression;

import java.util.ArrayList;
import java.util.List;

public class SqlJoinsExpression implements ISqlJoinsExpression
{
    private final List<ISqlJoinExpression> joins = new ArrayList<>();

    @Override
    public void addJoin(ISqlJoinExpression join)
    {
        joins.add(join);
    }

    public List<ISqlJoinExpression> getJoins()
    {
        return joins;
    }
}
