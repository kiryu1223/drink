package io.github.kiryu1223.drink.core.dataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDataSourceManager implements DataSourceManager {
    private final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

    private final ThreadLocal<String> dsKey = new ThreadLocal<>();

    public DefaultDataSourceManager(DataSource defluteDataSource) {
        dataSourceMap.put("default", defluteDataSource);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    public void addDataSource(String key, DataSource dataSource) {
        dataSourceMap.put(key, dataSource);
    }

    public DataSource getDataSource() {
        String key = getDsKey();
        if (key == null) {
            return dataSourceMap.get("default");
        }
        return dataSourceMap.get(key);
    }

    public void useDs(String key) {
        dsKey.set(key);
    }

    @Override
    public void useDefDs() {
        dsKey.remove();
    }

    public String getDsKey() {
        return dsKey.get();
    }

    @Override
    public Map<String, DataSource> getDataSources() {
        return dataSourceMap;
    }
}
