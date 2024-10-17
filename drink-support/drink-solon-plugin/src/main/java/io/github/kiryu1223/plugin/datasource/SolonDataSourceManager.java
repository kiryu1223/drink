package io.github.kiryu1223.plugin.datasource;

import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import org.noear.solon.data.tran.TranUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SolonDataSourceManager implements DataSourceManager
{
    private final DataSource dataSource;

    public SolonDataSourceManager(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException
    {
        return TranUtils.getConnection(getDataSource());
    }

    @Override
    public DataSource getDataSource()
    {
        return dataSource;
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
