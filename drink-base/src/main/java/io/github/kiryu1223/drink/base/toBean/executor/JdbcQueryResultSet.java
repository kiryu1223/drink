package io.github.kiryu1223.drink.base.toBean.executor;

import io.github.kiryu1223.drink.base.exception.DrinkException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcQueryResultSet implements AutoCloseable {
    protected final ResultSet resultSet;
    protected final PreparedStatement preparedStatement;
    protected final Connection connection;
    protected final boolean inTransaction;

    public JdbcQueryResultSet(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection, boolean inTransaction) {
        this.resultSet = resultSet;
        this.preparedStatement = preparedStatement;
        this.connection = connection;
        this.inTransaction = inTransaction;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (!inTransaction) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DrinkException(e);
        }
    }
}
