package io.github.kiryu1223.drink.base.toBean.executor;

import io.github.kiryu1223.drink.base.exception.DrinkException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcInsertResultSet extends JdbcQueryResultSet {
    private final long count;

    public JdbcInsertResultSet(ResultSet rs, PreparedStatement preparedStatement, Connection connection, long count) {
        super(rs, preparedStatement, connection);
        this.count = count;
    }

    public long getCount() {
        return count;
    }

    @Override
    public void close()
    {
        try {
            if (rs != null)
            {
                rs.close();
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new DrinkException(e);
        }
    }
}
