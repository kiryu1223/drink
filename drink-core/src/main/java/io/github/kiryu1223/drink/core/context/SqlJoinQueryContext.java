package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.api.crud.builder.QuerySqlBuilder;
import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlJoinQueryContext extends SqlJoinContext
{
    private final QuerySqlBuilder sqlBuilder;

    public SqlJoinQueryContext(JoinType joinType, SqlContext context, QuerySqlBuilder sqlBuilder)
    {
        super(joinType, context);
        this.sqlBuilder = sqlBuilder;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return getJoinType().getJoin() + " (" + sqlBuilder.getSqlAndValue(values) + ") ON " + getContext().getSqlAndValue(config, values);
    }

    @Override
    public String getSql(Config config)
    {
        return getJoinType().getJoin() + " (" + sqlBuilder.getSql() + ") ON " + getContext().getSql(config);
    }
}
