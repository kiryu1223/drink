package io.github.kiryu1223.drink.base.toBean.executor;

import io.github.kiryu1223.drink.base.exception.DrinkException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcQueryResultSet implements AutoCloseable {
    protected final ResultSet rs;
    protected final PreparedStatement preparedStatement;
    protected final Connection connection;
    protected boolean isLast = false;
    protected final boolean inTransaction;

    public JdbcQueryResultSet(ResultSet rs, PreparedStatement preparedStatement, Connection connection, boolean inTransaction) {
        this.rs = rs;
        this.preparedStatement = preparedStatement;
        this.connection = connection;
        this.inTransaction = inTransaction;
    }

    public ResultSet getRs() {
        return rs;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    @Override
    public void close() {
        try {
            if (rs != null) {
                rs.close();
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
