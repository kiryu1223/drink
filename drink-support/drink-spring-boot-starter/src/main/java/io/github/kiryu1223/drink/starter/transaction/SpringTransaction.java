package io.github.kiryu1223.drink.starter.transaction;

import io.github.kiryu1223.drink.api.transaction.DefaultTransaction;
import io.github.kiryu1223.drink.api.transaction.TransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SpringTransaction extends DefaultTransaction
{
    public SpringTransaction(Integer isolationLevel, DataSource dataSource, TransactionManager manager)
    {
        super(isolationLevel,dataSource,manager);
    }

    @Override
    public void close()
    {
        DataSourceUtils.releaseConnection(connection, dataSource);
        clear();
    }

    @Override
    public Connection getConnection() throws SQLException
    {
        if (connection == null)
        {
            connection = DataSourceUtils.getConnection(dataSource);
        }
        connection.setAutoCommit(false);
        if (isolationLevel != null) connection.setTransactionIsolation(isolationLevel);
        return connection;
    }
}
