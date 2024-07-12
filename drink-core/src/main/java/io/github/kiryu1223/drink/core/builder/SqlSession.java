package io.github.kiryu1223.drink.core.builder;

import io.github.kiryu1223.drink.api.crud.create.SqlValue;
import io.github.kiryu1223.drink.api.crud.transaction.Transaction;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class SqlSession
{
    public interface Function<T, R>
    {
        R invoke(T t) throws SQLException, NoSuchFieldException, IllegalAccessException, InvocationTargetException;
    }

    public interface Action<R>
    {
        R invoke() throws SQLException, NoSuchFieldException, IllegalAccessException, InvocationTargetException;
    }

    private final DataSource dataSource;

    public SqlSession()
    {
        this.dataSource = DataSourcesManager.getDefluteDataSource();
    }

    public SqlSession(String key)
    {
        this.dataSource = DataSourcesManager.getDataSource(key);
    }

    public <R> R executeQuery(Function<ResultSet, R> func, String sql, List<Object> values)
    {
        Transaction transaction = Transaction.curTransaction.get();
        if (transaction == null)
        {
            try (Connection connection = dataSource.getConnection())
            {
                //connection.setAutoCommit(true);
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
                {
                    setObjects(preparedStatement, values);
                    try (ResultSet resultSet = preparedStatement.executeQuery())
                    {
                        return func.invoke(resultSet);
                    }
                }
            }
            catch (NoSuchFieldException | InvocationTargetException | IllegalAccessException | SQLException e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            try
            {
                Connection connection = transaction.getConnection();
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
                {
                    setObjects(preparedStatement, values);
                    try (ResultSet resultSet = preparedStatement.executeQuery())
                    {
                        return func.invoke(resultSet);
                    }
                }
            }
            catch (NoSuchFieldException | InvocationTargetException | IllegalAccessException | SQLException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

//    public <R> R executeQuery(Function<ResultSet, R> func, String sql)
//    {
//        return executeQuery(func, sql, Collections.emptyList());
//    }

    public long executeUpdate(String sql, List<SqlValue> values)
    {
        Transaction transaction = Transaction.curTransaction.get();
        if (transaction == null)
        {
            System.out.println("无事务");
            return noTransactionExecuteUpdate(sql, values);
        }
        else
        {
            System.out.println("有事务");
            try
            {
                return transactionExecuteUpdate(transaction.getConnection(), sql, values);
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public long executeUpdate(String sql, List<Object> values, Object... o)
    {
        Transaction transaction = Transaction.curTransaction.get();
        if (transaction == null)
        {
            System.out.println("无事务");
            return noTransactionExecuteUpdate(sql, values);
        }
        else
        {
            System.out.println("有事务");
            try
            {
                return transactionExecuteUpdate(transaction.getConnection(), sql, values);
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public long batchExecuteUpdate(String sql, long limit, List<SqlValue> values)
    {
        Transaction transaction = Transaction.curTransaction.get();
        if (transaction == null)
        {
            return batchNoTransactionExecuteUpdate(sql, limit, values);
        }
        else
        {
            try
            {
                return batchTransactionExecuteUpdate(transaction.getConnection(), sql, limit, values);
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    private long noTransactionExecuteUpdate(String sql, List<SqlValue> values)
    {
        try (Connection connection = dataSource.getConnection())
        {
            connection.setAutoCommit(true);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
            {
                setObjectsIfNull(preparedStatement, values);
                return preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    private long noTransactionExecuteUpdate(String sql, List<Object> values, Object... o)
    {
        try (Connection connection = dataSource.getConnection())
        {
            connection.setAutoCommit(true);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
            {
                setObjects(preparedStatement, values);
                return preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    private long batchNoTransactionExecuteUpdate(String sql, long limit, List<SqlValue> values)
    {
        try (Connection connection = dataSource.getConnection())
        {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
            {
                batchSetObjects(preparedStatement, limit, values);
                return preparedStatement.executeBatch().length;
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    private int transactionExecuteUpdate(Connection connection, String sql, List<SqlValue> values)
    {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            setObjectsIfNull(preparedStatement, values);
            return preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    private int transactionExecuteUpdate(Connection connection, String sql, List<Object> values, Object... o)
    {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            setObjects(preparedStatement, values);
            return preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    private long batchTransactionExecuteUpdate(Connection connection, String sql, long limit, List<SqlValue> values)
    {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            batchSetObjects(preparedStatement, limit, values);
            return preparedStatement.executeBatch().length;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void setObjects(PreparedStatement preparedStatement, List<Object> values) throws SQLException
    {
        for (int i = 1; i <= values.size(); i++)
        {
            Object value = values.get(i - 1);
            preparedStatement.setObject(i, value);
        }
    }

    private void setObjectsIfNull(PreparedStatement preparedStatement, List<SqlValue> values) throws SQLException
    {
        for (int i = 1; i <= values.size(); i++)
        {
            SqlValue value = values.get(i - 1);
            Object o = value.getValues().get(0);
            if (o == null)
            {
                preparedStatement.setNull(i, convert(value.getType()));
            }
            else
            {
                preparedStatement.setObject(i, o);
            }
        }
    }

    private void batchSetObjects(PreparedStatement preparedStatement, long limit, List<SqlValue> values) throws SQLException
    {
        for (long i = 0; i < limit; i++)
        {
            int index = 0;
            for (SqlValue value : values)
            {
                Object o = value.getValues().get((int) i);
                if (o == null)
                {
                    preparedStatement.setNull(++index, convert(value.getType()));
                }
                else
                {
                    preparedStatement.setObject(++index, o);
                }
            }
            preparedStatement.addBatch();
        }
    }

    public static int convert(Class<?> type)
    {
        if (type == String.class)
        {
            return Types.VARCHAR;
        }
        else if (type == Integer.class || type == int.class)
        {
            return Types.INTEGER;
        }
        else if (type == Long.class || type == long.class)
        {
            return Types.BIGINT;
        }
        else if (type == Double.class || type == double.class)
        {
            return Types.DOUBLE;
        }
        else if (type == Float.class || type == float.class)
        {
            return Types.FLOAT;
        }
        else if (type == Boolean.class || type == boolean.class)
        {
            return Types.BOOLEAN;
        }
        else if (type == java.sql.Date.class || type == LocalDate.class)
        {
            return Types.DATE;
        }
        else if (type == java.sql.Time.class || type == LocalTime.class)
        {
            return Types.TIME;
        }
        else if (type == java.sql.Timestamp.class || type == LocalDateTime.class)
        {
            return Types.TIMESTAMP;
        }
        else if (type == java.math.BigDecimal.class)
        {
            return Types.DECIMAL;
        }
        else
        {
            return Types.OTHER; // Default to OTHER type if not recognized
        }
    }
}
