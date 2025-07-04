package io.github.kiryu1223.drink.base.toBean.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JdbcInsertResultSet extends JdbcQueryResultSet {
    private final long count;

    public JdbcInsertResultSet(ResultSet rs, PreparedStatement preparedStatement, Connection connection, long count) {
        super(rs, preparedStatement, connection);
        this.count = count;
    }

    public long getCount() {
        return count;
    }
}
