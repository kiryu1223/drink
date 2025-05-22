package io.github.kiryu1223.drink.starter.dataSource;

import io.github.kiryu1223.drink.base.dataSource.DataSourceManager;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SpringDataSourceManager implements DataSourceManager
{
    private final DataSource dataSource;

    public SpringDataSourceManager(DataSource dataSource)
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
}
