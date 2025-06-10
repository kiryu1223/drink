package io.github.kiryu1223.drink.base.toBean.handler.impl.datetime;


import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.lang.reflect.Type;
import java.sql.*;

public class TimestampTypeHandler implements ITypeHandler<Timestamp> {
    @Override
    public Timestamp getValue(ResultSet resultSet, int index, Type type) throws SQLException {
        return resultSet.getTimestamp(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Timestamp timestamp) throws SQLException {
        if (timestamp == null) {
            preparedStatement.setNull(index, JDBCType.TIMESTAMP.getVendorTypeNumber());
        }
        else {
            preparedStatement.setTimestamp(index, timestamp);
        }
    }
}
