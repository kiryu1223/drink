package io.github.kiryu1223.drink.starter.dataSource;

import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class SpringDynamicDataSourceManager extends AbstractRoutingDataSource implements DataSourceManager
{
    private final ThreadLocal<String> keyHolder = new ThreadLocal<>();
    private final String primary;

    public SpringDynamicDataSourceManager(String primary)
    {
        this.primary = primary;
    }

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources)
    {
        super.setTargetDataSources(targetDataSources);
        //afterPropertiesSet();
    }

    @Override
    public DataSource getDataSource()
    {
        return determineTargetDataSource();
    }

    @Override
    public void addDataSource(String key, DataSource dataSource)
    {
        throw new RuntimeException("DynamicDataSource cant add datasource");
    }

    @Override
    public void useDs(String key)
    {
        keyHolder.set(key);
    }

    @Override
    public void useDefDs()
    {
        keyHolder.set(primary);
    }

    @Override
    public String getDsKey()
    {
        String s = keyHolder.get();
        return s == null ? primary : s;
    }

    @Override
    protected Object determineCurrentLookupKey()
    {
        return getDsKey();
    }

    @Override
    public Connection getConnection() throws SQLException
    {
        return DataSourceUtils.getConnection(getDataSource());
    }
}
