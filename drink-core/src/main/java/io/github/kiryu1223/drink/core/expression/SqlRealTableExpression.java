package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.config.dialect.IDialect;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;

import java.util.List;

public class SqlRealTableExpression extends SqlTableExpression
{
    private final Class<?> tableClass;

    public SqlRealTableExpression(Class<?> tableClass)
    {
        this.tableClass = tableClass;
    }

    @Override
    public Class<?> getTableClass()
    {
        return tableClass;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return getSql(config);
    }

    @Override
    public String getSql(Config config)
    {
        MetaData metaData = MetaDataCache.getMetaData(tableClass);
        IDialect dbConfig = config.getDisambiguation();
        return dbConfig.disambiguation(metaData.getTableName());
    }
}
