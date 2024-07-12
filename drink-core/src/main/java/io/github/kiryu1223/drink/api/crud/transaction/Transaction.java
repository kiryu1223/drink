package io.github.kiryu1223.drink.api.crud.transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Transaction implements AutoCloseable
{
    private Connection connection;
    private final DataSource dataSource;
    public static ThreadLocal<Transaction> curTransaction = new ThreadLocal<>();
    private final Integer isolationLevel;

    public Transaction(Integer isolationLevel, DataSource dataSource)
    {
        this.isolationLevel = isolationLevel;
        this.dataSource = dataSource;
        curTransaction.set(this);
    }

    public Integer getIsolationLevel()
    {
        return isolationLevel;
    }

    public void commit()
    {
        try
        {
            connection.commit();
            close();
        }
        catch (SQLException e)
        {
            rollback();
            throw new RuntimeException(e);
        }
        finally
        {
            clear();
        }
    }

    public void rollback()
    {
        try
        {
            connection.rollback();
            close();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            clear();
        }
    }

    @Override
    public void close()
    {
        try
        {
            connection.close();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            clear();
        }
    }

    private void clear()
    {
        curTransaction.remove();
    }

    public Connection getConnection() throws SQLException
    {
        if (connection == null)
        {
            connection = dataSource.getConnection();
        }
        connection.setAutoCommit(false);
        if (isolationLevel != null) connection.setTransactionIsolation(isolationLevel);
        return connection;
    }
}
