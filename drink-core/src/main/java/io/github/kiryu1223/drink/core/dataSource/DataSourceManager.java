package io.github.kiryu1223.drink.core.dataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public interface DataSourceManager
{
    Connection getConnection() throws SQLException;

    DataSource getDataSource();

    void addDataSource(String key, DataSource dataSource);

    void useDs(String key);

    void useDefDs();

    String getDsKey();

    Map<String,DataSource> getDataSources();
}