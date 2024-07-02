package io.github.kiryu1223.drink.core.builder;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class DataSourcesManager
{
    private DataSourcesManager()
    {
    }

    private static final Map<String, DataSource> dataSourceMap = new HashMap<>();

    private static DataSource defluteDataSource;

    public static void addDataSource(String key, DataSource dataSource)
    {
        dataSourceMap.put(key, dataSource);
    }

    public static DataSource getDataSource(String key)
    {
        return dataSourceMap.get(key);
    }

    public static DataSource getDefluteDataSource()
    {
        return defluteDataSource;
    }

    public static void setDeflateDataSource(DataSource defluteDataSource)
    {
        DataSourcesManager.defluteDataSource = defluteDataSource;
    }
}
