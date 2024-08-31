package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlLimitExpression extends SqlExpression
{
    private long offset, rows;

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

    public void setOffset(long offset)
    {
        this.offset = offset;
    }

    public void setRows(long rows)
    {
        this.rows = rows;
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
            return "LIMIT " + rows + " OFFSET " + offset;
        }
        else
        {
            if (rows != 0)
            {
                return "LIMIT " + rows;
            }
            else
            {
                return "";
            }
        }
    }
}
