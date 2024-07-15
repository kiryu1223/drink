package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.config.inter.IDBConfig;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;

import java.util.List;

public class SqlRealTableContext extends SqlTableContext
{
    private final Class<?> tableClass;

    public SqlRealTableContext(Class<?> tableClass)
    {
        this.tableClass = tableClass;
    }

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
        IDBConfig dbConfig = config.getDbConfig();
        return dbConfig.propertyDisambiguation(metaData.getTableName());
    }
}
