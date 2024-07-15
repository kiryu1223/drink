package io.github.kiryu1223.drink.core.dataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public interface DataSourceManager
{
    Connection getConnection() throws SQLException;

    DataSource getDataSource();

    void addDataSource(String key, DataSource dataSource);

    void useDs(String key);

    void useDefDs();

    String getDsKey();
}