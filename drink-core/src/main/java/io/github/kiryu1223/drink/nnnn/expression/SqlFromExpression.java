package io.github.kiryu1223.drink.nnnn.expression;

import io.github.kiryu1223.drink.base.expression.ISqlFromExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTableExpression;
import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlFromExpression implements ISqlFromExpression
{
    protected final ISqlTableExpression sqlTableExpression;
    protected final int index;

    public SqlFromExpression(ISqlTableExpression sqlTableExpression, int index)
    {
        this.sqlTableExpression = sqlTableExpression;
        this.index = index;
    }

    @Override
    public ISqlTableExpression getSqlTableExpression()
    {
        return sqlTableExpression;
    }

    @Override
    public int getIndex()
    {
        return index;
    }
}
