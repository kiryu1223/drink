package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlJoinContext extends SqlContext
{
    private final JoinType joinType;
    private final SqlContext joinTable;
    private final SqlContext onContext;

    public SqlJoinContext(JoinType joinType, SqlContext joinTable, SqlContext context)
    {
        this.joinType = joinType;
        this.joinTable = joinTable;
        this.onContext = context;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return joinType.getJoin() + " " + joinTable.getSqlAndValue(config, values) + " ON " + onContext.getSqlAndValue(config, values);
    }

    @Override
    public String getSql(Config config)
    {
        return joinType.getJoin() + " " + joinTable.getSql(config) + " ON " + onContext.getSql(config);
    }
}
