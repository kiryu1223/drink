package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlLimitExpression extends SqlExpression
{
    private final long offset, rows;

    public SqlLimitExpression(long rows)
    {
        this.offset = 0;
        this.rows = rows;
    }

    public SqlLimitExpression(long offset, long rows)
    {
        this.offset = offset;
        this.rows = rows;
    }

    public long getOffset()
    {
        return offset;
    }

    public long getRows()
    {
        return rows;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return getSql(config);
    }

    @Override
    public String getSql(Config config)
    {
        if (offset != 0)
        {
            return "LIMIT " + offset + "," + rows;
        }
        else
        {
            return "LIMIT " + rows;
        }
    }
}
