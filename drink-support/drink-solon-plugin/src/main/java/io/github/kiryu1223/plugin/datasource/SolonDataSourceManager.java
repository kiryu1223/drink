package io.github.kiryu1223.plugin.datasource;

import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import org.noear.solon.data.dynamicds.DynamicDataSource;
import org.noear.solon.data.dynamicds.DynamicDsKey;
import org.noear.solon.data.tran.TranUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SolonDataSourceManager implements DataSourceManager
{
    private DataSource defaultDataSource;

    private DynamicDataSource dynamicDataSource;

    private final String dsName;

    public SolonDataSourceManager(String dsName)
    {
        this.dsName = dsName;
    }

    public String getDsName()
    {
        return dsName;
    }

    @Override
    public Connection getConnection() throws SQLException
    {
        return TranUtils.getConnection(getDataSource());
    }

    public void setDynamicDataSource(DynamicDataSource dynamicDataSource)
    {
        this.dynamicDataSource = dynamicDataSource;
    }

    public void addDataSource(String key, DataSource dataSource)
    {
        dynamicDataSource.addTargetDataSource(key, dataSource);
    }

    public DataSource getDataSource()
    {
        String key = getDsKey();
        if (key == null)
        {
            return getDefaultDataSource();
        }
        return getDataSource(key);
    }

    private DataSource getDataSource(String key)
    {
        return dynamicDataSource.determineCurrentTarget();
    }

    private DataSource getDefaultDataSource()
    {
        DataSource result;
        if (dynamicDataSource == null)
        {
            if (defaultDataSource == null)
            {
                throw new RuntimeException("No DataSource found");
            }
            result = defaultDataSource;
        }
        else
        {
            DataSource defaultTargetDataSource = dynamicDataSource.getDefaultTargetDataSource();
            if (defaultTargetDataSource == null)
            {
                throw new RuntimeException("No DataSource found");
            }
            result = defaultTargetDataSource;
        }
        return result;
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
        if (dynamicDataSource != null)
        {
            return dynamicDataSource.determineCurrentKey();
        }
        else
        {
            return null;
        }
    }

    public boolean hasDefaultDataSource()
    {
        return defaultDataSource != null;
    }

    public boolean hasDynamicDataSource()
    {
        return dynamicDataSource != null;
    }

    public void setDefaultDataSource(DataSource dataSource)
    {
        this.defaultDataSource = dataSource;
    }
}
