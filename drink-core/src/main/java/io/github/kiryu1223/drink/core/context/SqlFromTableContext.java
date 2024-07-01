package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.visitor.ExpressionUtil;

import java.util.List;

public class SqlFromTableContext extends SqlContext
{
    private final String tableName;

    public SqlFromTableContext(String tableName)
    {
        this.tableName = tableName;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return getSql(config);
    }

    @Override
    public String getSql(Config config)
    {
        return tableName;
    }
}
