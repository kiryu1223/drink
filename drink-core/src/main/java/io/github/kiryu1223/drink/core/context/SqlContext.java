package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public abstract class SqlContext
{
    public abstract String getSqlAndValue(Config config, List<Object> values);

    public abstract String getSql(Config config);

    protected String getTableAsName(int index)
    {
        return "t" + index;
    }

    protected SqlContext unbox(SqlContext context)
    {
        if (context instanceof SqlParensContext)
        {
            SqlParensContext sqlParensContext = (SqlParensContext) context;
            return unbox(sqlParensContext.getContext());
        }
        else if (context instanceof SqlAsNameContext)
        {
            SqlAsNameContext sqlAsNameContext = (SqlAsNameContext) context;
            return unbox(sqlAsNameContext.getContext());
        }
        else
        {
            return context;
        }
    }
}
