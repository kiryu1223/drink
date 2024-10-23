package io.github.kiryu1223.drink.nnnn.expression;

import io.github.kiryu1223.drink.base.expression.ISqlLimitExpression;
import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlLimitExpression implements ISqlLimitExpression
{
    protected long offset, rows;

    public long getOffset()
    {
        return offset;
    }

    public long getRows()
    {
        return rows;
    }

    public void setOffset(long offset)
    {
        this.offset = offset;
    }

    public void setRows(long rows)
    {
        this.rows = rows;
    }
}
