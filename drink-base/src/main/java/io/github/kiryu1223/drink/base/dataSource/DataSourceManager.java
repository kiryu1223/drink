package io.github.kiryu1223.drink.base.dataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public interface DataSourceManager
{
    Connection getConnection() throws SQLException;

    DataSource getDataSource();

    void useDs(String key);

    void useDefDs();

    String getDsKey();
}