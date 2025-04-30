package io.github.kiryu1223.drink.base.dataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public interface DataSourceManager
{
    /**
     * 获取连接
     */
    Connection getConnection() throws SQLException;

    /**
     * 获取数据源
     */
    DataSource getDataSource();
}