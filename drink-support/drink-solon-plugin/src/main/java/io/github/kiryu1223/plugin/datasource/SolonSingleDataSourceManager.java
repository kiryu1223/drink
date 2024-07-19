package io.github.kiryu1223.plugin.datasource;

import javax.sql.DataSource;

public class SolonSingleDataSourceManager extends SolonDataSourceManager
{
    private final DataSource dataSource;

    public SolonSingleDataSourceManager(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    @Override
    public DataSource getDataSource()
    {
        return dataSource;
    }

    @Override
    public void addDataSource(String key, DataSource dataSource)
    {

    }

    @Override
    public void useDs(String key)
    {

    }

    @Override
    public void useDefDs()
    {

    }

    @Override
    public String getDsKey()
    {
        return "";
    }
}
