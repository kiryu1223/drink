package io.github.kiryu1223.drink.base.session;


import io.github.kiryu1223.drink.base.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.base.transaction.TransactionManager;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class DefaultSqlSession implements SqlSession {
    protected final DataSourceManager dataSourceManager;
    protected final TransactionManager transactionManager;

    public DefaultSqlSession(DataSourceManager dataSourceManager, TransactionManager transactionManager) {
        this.dataSourceManager = dataSourceManager;
        this.transactionManager = transactionManager;
    }

    public <R> R executeQuery(Function<R> func, String sql, Collection<SqlValue> sqlValues) {
        if (!transactionManager.currentThreadInTransaction()) {
            try (Connection connection = dataSourceManager.getConnection()) {
                return executeQuery(connection, func, sql, sqlValues);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                Connection connection;
                if (transactionManager.isOpenTransaction()) {
                    connection = transactionManager.getCurTransaction().getConnection();
                }
                else {
                    connection = dataSourceManager.getConnection();
                }
                return executeQuery(connection, func, sql, sqlValues);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public ResultSet executeQuery(String sql, Collection<SqlValue> sqlValues, int fetchSize)
    {
        if (!transactionManager.currentThreadInTransaction()) {
            try (Connection connection = dataSourceManager.getConnection()) {
                return getResultSet(connection, sql, sqlValues,fetchSize);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                Connection connection;
                if (transactionManager.isOpenTransaction()) {
                    connection = transactionManager.getCurTransaction().getConnection();
                }
                else {
                    connection = dataSourceManager.getConnection();
                }
                return getResultSet(connection, sql, sqlValues,fetchSize);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private ResultSet getResultSet(Connection connection,String sql, Collection<SqlValue> sqlValues,int fetchSize) throws SQLException
    {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setFetchSize(fetchSize);
        setObjects(preparedStatement, sqlValues);
        return preparedStatement.executeQuery();
    }

    private <R> R executeQuery(Connection connection, Function<R> func, String sql, Collection<SqlValue> sqlValues) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setObjects(preparedStatement, sqlValues);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return func.invoke(resultSet);
            }
        } catch (SQLException | NoSuchFieldException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long executeInsert(Action action, String sql, Collection<SqlValue> sqlValues, int length, boolean autoIncrement) {
        if (!transactionManager.currentThreadInTransaction()) {
            try (Connection connection = dataSourceManager.getConnection()) {
//                boolean autoCommit = connection.getAutoCommit();
//                connection.setAutoCommit(true);
                long count = executeInsert(action, connection, sql, sqlValues, length, autoIncrement);
//                connection.setAutoCommit(autoCommit);
                return count;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                Connection connection;
                if (transactionManager.isOpenTransaction()) {
                    connection = transactionManager.getCurTransaction().getConnection();
                }
                else {
                    connection = dataSourceManager.getConnection();
                }
                return executeInsert(action, connection, sql, sqlValues, length, autoIncrement);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public long executeDelete(String sql, Collection<SqlValue> sqlValues) {
        return executeUpdate(sql, sqlValues);
    }

    public long executeUpdate(String sql, Collection<SqlValue> sqlValues) {
        if (!transactionManager.currentThreadInTransaction()) {
            try (Connection connection = dataSourceManager.getConnection()) {
                boolean autoCommit = connection.getAutoCommit();
                connection.setAutoCommit(true);
                long count = executeUpdate(connection, sql, sqlValues);
                connection.setAutoCommit(autoCommit);
                return count;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                Connection connection;
                if (transactionManager.isOpenTransaction()) {
                    connection = transactionManager.getCurTransaction().getConnection();
                }
                else {
                    connection = dataSourceManager.getConnection();
                }
                return executeUpdate(connection, sql, sqlValues);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected long executeInsert(Action action, Connection connection, String sql, Collection<SqlValue> sqlValues, int length, boolean autoIncrement) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, autoIncrement ? PreparedStatement.RETURN_GENERATED_KEYS : PreparedStatement.NO_GENERATED_KEYS)) {
            boolean batch = setObjects(preparedStatement, sqlValues, length);
            int count;
            if (batch) {
                count = preparedStatement.executeBatch().length;
            }
            else {
                count = preparedStatement.executeUpdate();
            }
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys())
            {
                action.invoke(resultSet);
            }
            return count;
        } catch (SQLException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected long executeUpdate(Connection connection, String sql, Collection<SqlValue> sqlValues) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setObjects(preparedStatement, sqlValues);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void setObjects(PreparedStatement preparedStatement, Collection<SqlValue> sqlValues) throws SQLException {
        int index = 1;
        for (SqlValue sqlValue : sqlValues) {
            sqlValue.preparedStatementSetValue(preparedStatement, index++);
        }
    }

    protected boolean setObjects(PreparedStatement preparedStatement, Collection<SqlValue> sqlValues, int length) throws SQLException, InvocationTargetException, IllegalAccessException {
        int size = sqlValues.size();
        boolean batch = size > length;
        int index = 1;
        for (SqlValue sqlValue : sqlValues) {
            sqlValue.preparedStatementSetValue(preparedStatement, index++);
            if (index > length && batch) {
                index = 1;
                preparedStatement.addBatch();
            }
        }
        return batch;
    }
}
