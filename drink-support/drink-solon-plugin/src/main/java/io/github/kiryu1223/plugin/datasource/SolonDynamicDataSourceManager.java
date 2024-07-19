package io.github.kiryu1223.plugin.datasource;

import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import org.noear.solon.data.dynamicds.DynamicDataSource;
import org.noear.solon.data.dynamicds.DynamicDsKey;
import org.noear.solon.data.tran.TranUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SolonDynamicDataSourceManager extends SolonDataSourceManager implements DataSourceManager
{
    private final DynamicDataSource dynamicDataSource;

    public SolonDynamicDataSourceManager(DynamicDataSource dynamicDataSource)
    {
        this.dynamicDataSource = dynamicDataSource;
    }

    public void addDataSource(String key, DataSource dataSource)
    {
        dynamicDataSource.addTargetDataSource(key, dataSource);
    }

    public DataSource getDataSource()
    {
        return dynamicDataSource.determineCurrentTarget();
    }

    public void useDs(String key)
    {
        dynamicDataSource.setCurrentKey(key);
    }

    @Override
    public void useDefDs()
    {
        DynamicDsKey.remove();
    }

    public String getDsKey()
    {
        return dynamicDataSource.determineCurrentKey();
    }
}
