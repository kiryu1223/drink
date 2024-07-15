package io.github.kiryu1223.drink.starter.dataSource;

import io.github.kiryu1223.drink.core.dataSource.DefaultDataSourceManager;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SpringDataSourceManager extends DefaultDataSourceManager
{
    public SpringDataSourceManager(DataSource defluteDataSource)
    {
        super(defluteDataSource);
    }

    @Override
    public Connection getConnection()
    {
        return DataSourceUtils.getConnection(getDataSource());
    }
}
