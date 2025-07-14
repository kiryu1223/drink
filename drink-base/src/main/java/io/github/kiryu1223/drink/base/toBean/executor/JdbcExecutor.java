package io.github.kiryu1223.drink.base.toBean.executor;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.log.ISqlLogger;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.transaction.TransactionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class JdbcExecutor {

    private static void logSql(IConfig config, String sql) {
        ISqlLogger sqlLogger = config.getSqlLogger();
        sqlLogger.printSql(sql);
    }

    private static void logValues(IConfig config, List<SqlValue> values) {
        ISqlLogger sqlLogger = config.getSqlLogger();
        sqlLogger.printValues(values);
    }

    private static void logValueList(IConfig config, List<List<SqlValue>> values) {
        if (config.isPrintValues()) {
            ISqlLogger sqlLogger = config.getSqlLogger();
            for (List<SqlValue> value : values) {
                sqlLogger.printValues(value);
            }
        }
    }

    private static void logTime(IConfig config, long time) {
        ISqlLogger sqlLogger = config.getSqlLogger();
        sqlLogger.printTime(time);
    }

    private static void logUpdate(IConfig config, long update) {
        ISqlLogger sqlLogger = config.getSqlLogger();
        sqlLogger.printUpdate(update);
    }

    public static JdbcQueryResultSet executeQuery(IConfig config, String sql, List<SqlValue> values) {
        return executeQuery(config, sql, values, 0);
    }

    public static JdbcQueryResultSet executeQuery(IConfig config, String sql, List<SqlValue> values, int fetchSize) {
        try {
            Connection connection = connection(config);
            logSql(config, sql);
            logValues(config, values);
            PreparedStatement preparedStatement = statement(connection, sql, values);
            if (fetchSize > 0) {
                preparedStatement.setFetchSize(fetchSize);
            }
            boolean printTime = config.isPrintTime();
            long start = printTime ? System.currentTimeMillis() : 0;
            ResultSet resultSet = preparedStatement.executeQuery();
            long end = printTime ? System.currentTimeMillis() : 0;
            logTime(config, end - start);
            boolean inTransaction = config.getTransactionManager().currentThreadInTransaction();
            return new JdbcQueryResultSet(resultSet, preparedStatement, connection, inTransaction);
        } catch (SQLException e) {
            throw new DrinkException(e);
        }
    }

    public static JdbcUpdateResultSet executeUpdate(IConfig config, String sql, List<List<SqlValue>> values) {
        try {
            Connection connection = connection(config);
            logSql(config, sql);
            logValueList(config, values);
            PreparedStatement statement = batchStatement(connection, sql, values, false);
            boolean printTime = config.isPrintTime();
            long start = printTime ? System.currentTimeMillis() : 0;
            int i = statement.executeUpdate();
            long end = printTime ? System.currentTimeMillis() : 0;
            logTime(config, end - start);
            logUpdate(config, i);
            boolean inTransaction = config.getTransactionManager().currentThreadInTransaction();
            return new JdbcUpdateResultSet(null, statement, connection, inTransaction, i);
        } catch (SQLException e) {
            throw new DrinkException(e);
        }
    }

    public static JdbcUpdateResultSet executeDelete(IConfig config, String sql, List<SqlValue> values) {
        return executeUpdate(config, sql, Collections.singletonList(values));
    }

    private static Connection connection(IConfig config) throws SQLException {
        DataSourceManager dataSourceManager = config.getDataSourceManager();
        TransactionManager transactionManager = config.getTransactionManager();
        Connection connection;
        if (!transactionManager.currentThreadInTransaction()) {
            connection = dataSourceManager.getConnection();
        }
        else {
            if (transactionManager.isOpenTransaction()) {
                connection = transactionManager.getCurTransaction().getConnection();
            }
            else {
                connection = dataSourceManager.getConnection();
            }
        }
        return connection;
    }

    private static PreparedStatement statement(Connection connection, String sql, List<SqlValue> sqlValues) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        setObjects(preparedStatement, sqlValues);
        return preparedStatement;
    }

    private static void setObjects(PreparedStatement preparedStatement, List<SqlValue> sqlValues) throws SQLException {
        int index = 1;
        for (SqlValue sqlValue : sqlValues) {
            sqlValue.preparedStatementSetValue(preparedStatement, index++);
        }
    }

    public static JdbcUpdateResultSet executeInsert(IConfig config, String sql, List<List<SqlValue>> sqlValues, boolean generatedKeys) {
        try {
            Connection connection = connection(config);
            logSql(config, sql);
            logValueList(config, sqlValues);
            boolean batch = sqlValues.size() > 1;
            PreparedStatement preparedStatement = batchStatement(connection, sql, sqlValues, generatedKeys);
            int count;
            boolean printTime = config.isPrintTime();
            long start = printTime ? System.currentTimeMillis() : 0;
            if (batch) {
                count = preparedStatement.executeBatch().length;
            }
            else {
                count = preparedStatement.executeUpdate();
            }
            long end = printTime ? System.currentTimeMillis() : 0;
            logTime(config, end - start);
            ResultSet resultSet = null;
            if (generatedKeys) {
                resultSet = preparedStatement.getGeneratedKeys();
            }
            boolean inTransaction = config.getTransactionManager().currentThreadInTransaction();
            return new JdbcUpdateResultSet(resultSet, preparedStatement, connection, inTransaction, count);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static PreparedStatement batchStatement(Connection connection, String sql, List<List<SqlValue>> sqlValues, boolean generatedKeys) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql, generatedKeys ? PreparedStatement.RETURN_GENERATED_KEYS : PreparedStatement.NO_GENERATED_KEYS);
        boolean batch = sqlValues.size() > 1;
        for (int i = 0; i < sqlValues.size(); i++) {
            List<SqlValue> sqlValue = sqlValues.get(i);
            setObjects(preparedStatement, sqlValue);
            if (batch && i < sqlValues.size() - 1) {
                preparedStatement.addBatch();
            }
        }
        return preparedStatement;
    }
}
