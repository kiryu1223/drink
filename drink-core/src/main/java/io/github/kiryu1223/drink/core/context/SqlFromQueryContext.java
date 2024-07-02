package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.api.crud.builder.QuerySqlBuilder;
import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlFromQueryContext extends SqlContext
{
    private final QuerySqlBuilder sqlBuilder;

    public SqlFromQueryContext(QuerySqlBuilder sqlBuilder)
    {
        this.sqlBuilder = sqlBuilder;
    }

    public QuerySqlBuilder getSqlBuilder()
    {
        return sqlBuilder;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return  sqlBuilder.getSqlAndValue(values);
    }

    @Override
    public String getSql(Config config)
    {
        return sqlBuilder.getSql();
    }
}
