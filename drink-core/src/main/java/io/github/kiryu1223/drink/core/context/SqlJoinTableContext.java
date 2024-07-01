package io.github.kiryu1223.drink.core.context;


import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlJoinTableContext extends SqlJoinContext
{
    private final String tableName;

    public SqlJoinTableContext(JoinType joinType, SqlContext context, String tableName)
    {
        super(joinType, context);
        this.tableName = tableName;
    }

    public String getTableName()
    {
        return tableName;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return getJoinType().getJoin() + " " + tableName + " ON " + getContext().getSqlAndValue(config, values);
    }

    @Override
    public String getSql(Config config)
    {
        return getJoinType().getJoin() + " " + tableName + " ON " + getContext().getSql(config);
    }
}
