package io.github.kiryu1223.drink.base.toBean.executor;

import io.github.kiryu1223.drink.base.exception.DrinkException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcQueryResultSet implements AutoCloseable {
    private final ResultSet rs;
    private final PreparedStatement preparedStatement;
    private final Connection connection;

    public JdbcQueryResultSet(ResultSet rs, PreparedStatement preparedStatement, Connection connection) {
        this.rs = rs;
        this.preparedStatement = preparedStatement;
        this.connection = connection;
    }

    public ResultSet getRs() {
        return rs;
    }

    @Override
    public void close() {
        try {
            rs.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new DrinkException(e);
        }
    }
}
