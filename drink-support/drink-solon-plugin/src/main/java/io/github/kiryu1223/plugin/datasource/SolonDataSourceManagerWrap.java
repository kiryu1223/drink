package io.github.kiryu1223.plugin.datasource;

import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SolonDataSourceManagerWrap implements DataSourceManager
{
    private SolonDataSourceManager dataSourceManager;
    private final String dsName;

    public SolonDataSourceManagerWrap(String dsName)
    {
        this.dsName = dsName;
    }

    @Override
    public Connection getConnection() throws SQLException
    {
        return dataSourceManager.getConnection();
    }

    @Override
    public DataSource getDataSource()
    {
        return dataSourceManager.getDataSource();
    }

    @Override
    public void addDataSource(String key, DataSource dataSource)
    {
        dataSourceManager.addDataSource(key, dataSource);
    }

    @Override
    public void useDs(String key)
    {
        dataSourceManager.useDs(key);
    }

    @Override
    public void useDefDs()
    {
        dataSourceManager.useDefDs();
    }

    @Override
    public String getDsKey()
    {
        return dataSourceManager.getDsKey();
    }

    public String getDsName()
    {
        return dsName;
    }

    public void setDataSourceManager(SolonDataSourceManager dataSourceManager)
    {
        if (hasDataSource())
        {
            throw new RuntimeException("DataSourceManager not null");
        }
        this.dataSourceManager = dataSourceManager;
    }

    public boolean hasDataSource()
    {
        return dataSourceManager != null;
    }
}
