package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlLimitExpression extends SqlExpression
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

    public boolean onlyHasRows()
    {
        return rows > 0 && offset <= 0;
    }

    public boolean hasRowsAndOffset()
    {
        return rows > 0 && offset > 0;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return getSql(config);
    }

    @Override
    public String getSql(Config config)
    {
        if (rows > 0)
        {
            if (offset > 0)
            {
                return String.format("LIMIT %d OFFSET %d", rows, offset);
            }
            else
            {
                return String.format("LIMIT %d", rows);
            }
        }
        return "";
    }

    @Override
    public <T extends SqlExpression> T copy(Config config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return (T) factory.limit(offset, rows);
    }
}
