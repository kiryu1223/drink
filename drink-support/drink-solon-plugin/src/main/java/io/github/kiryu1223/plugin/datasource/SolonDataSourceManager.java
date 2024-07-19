package io.github.kiryu1223.plugin.datasource;

import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import org.noear.solon.data.tran.TranUtils;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class SolonDataSourceManager implements DataSourceManager
{
    @Override
    public Connection getConnection() throws SQLException
    {
        return TranUtils.getConnection(getDataSource());
    }
}
