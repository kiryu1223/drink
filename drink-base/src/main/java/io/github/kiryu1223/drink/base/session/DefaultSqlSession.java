package io.github.kiryu1223.drink.base.session;


import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.base.transaction.TransactionManager;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Collection;

public class DefaultSqlSession implements SqlSession {
    protected final DataSourceManager dataSourceManager;
    protected final TransactionManager transactionManager;

    public DefaultSqlSession(DataSourceManager dataSourceManager, TransactionManager transactionManager) {
        this.dataSourceManager = dataSourceManager;
        this.transactionManager = transactionManager;
    }

    public <R> R executeQuery(Function<ResultSet, R> func, String sql, Collection<SqlValue> sqlValues) {
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

    private <R> R executeQuery(Connection connection, Function<ResultSet, R> func, String sql, Collection<SqlValue> sqlValues) {
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
    public long executeInsert(String sql, Collection<SqlValue> sqlValues, int length) {
        if (!transactionManager.currentThreadInTransaction()) {
            try (Connection connection = dataSourceManager.getConnection()) {
                boolean autoCommit = connection.getAutoCommit();
                connection.setAutoCommit(true);
                long count = executeInsert(connection, sql, sqlValues, length);
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
                return executeInsert(connection, sql, sqlValues, length);
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

    protected long executeInsert(Connection connection, String sql, Collection<SqlValue> sqlValues, int length) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            boolean batch = setObjects(preparedStatement, sqlValues, length);
            if (batch) {
                return preparedStatement.executeBatch().length;
            }
            else {
                return preparedStatement.executeUpdate();
            }
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
