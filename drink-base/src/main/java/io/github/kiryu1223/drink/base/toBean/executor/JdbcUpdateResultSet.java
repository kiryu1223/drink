package io.github.kiryu1223.drink.base.toBean.executor;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JdbcUpdateResultSet extends JdbcQueryResultSet {
    private final long row;

    public JdbcUpdateResultSet(ResultSet rs, PreparedStatement preparedStatement, Connection connection, boolean inTransaction, long row) {
        super(rs, preparedStatement, connection, inTransaction);
        this.row = row;
    }

    public long getRow() {
        return row;
    }
}
