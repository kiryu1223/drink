package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlLimitContext extends SqlContext
{
    private final long offset, rows;

    public SqlLimitContext(long rows)
    {
        this.offset = 0;
        this.rows = rows;
    }

    public SqlLimitContext(long offset, long rows)
    {
        this.offset = offset;
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
            return "LIMIT " + offset + "," + rows;
        }
        else
        {
            return "LIMIT " + rows;
        }
    }
}
