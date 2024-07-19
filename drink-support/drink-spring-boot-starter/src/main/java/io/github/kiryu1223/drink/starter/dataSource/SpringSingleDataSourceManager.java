package io.github.kiryu1223.drink.starter.dataSource;

import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SpringSingleDataSourceManager implements DataSourceManager
{
    private final DataSource dataSource;

    public SpringSingleDataSourceManager(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException
    {
        return DataSourceUtils.getConnection(getDataSource());
    }

    @Override
    public DataSource getDataSource()
    {
        return dataSource;
    }

    @Override
    public void addDataSource(String key, DataSource dataSource)
    {
        throw new RuntimeException("Single DataSourceManager does not support addDataSource");
    }

    @Override
    public void useDs(String key)
    {
        throw new RuntimeException("single DataSourceManager does not support useDs");
    }

    @Override
    public void useDefDs()
    {
        throw new RuntimeException("single DataSourceManager does not support useDefDs");
    }

    @Override
    public String getDsKey()
    {
        return "default";
    }
}
