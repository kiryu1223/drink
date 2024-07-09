package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.api.crud.builder.QuerySqlBuilder;
import io.github.kiryu1223.drink.api.crud.read.QueryBase;
import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlVirtualTableContext extends SqlTableContext
{
    private final QuerySqlBuilder sqlBuilder;

    public SqlVirtualTableContext(QuerySqlBuilder sqlBuilder)
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
        return sqlBuilder.getSqlAndValue(values);
    }

    @Override
    public String getSql(Config config)
    {
        return sqlBuilder.getSql();
    }
}
