package io.github.kiryu1223.drink.core;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataSourcesManager
{
    private DataSourcesManager(){}
    private static final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

    public static Map<String, DataSource> getDataSourceMap()
    {
        return dataSourceMap;
    }

    public static DataSource getDataSource(String key)
    {
        return dataSourceMap.get(key);
    }

    public static DataSource getDataSource()
    {
        return dataSourceMap.get("");
    }
}
