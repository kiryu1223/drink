package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.config.dialect.IDialect;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;

import java.util.List;

public class SqlRealTableExpression extends SqlTableExpression
{
    private final Class<?> tableClass;

    SqlRealTableExpression(Class<?> tableClass)
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
        String fullName = "";
        MetaData metaData = MetaDataCache.getMetaData(tableClass);
        IDialect dbConfig = config.getDisambiguation();
        String schema = metaData.getSchema();
        if (!schema.isEmpty())
        {
            fullName += dbConfig.disambiguation(schema) + ".";
        }
        fullName += dbConfig.disambiguation(metaData.getTableName());
        return fullName;
    }
}
