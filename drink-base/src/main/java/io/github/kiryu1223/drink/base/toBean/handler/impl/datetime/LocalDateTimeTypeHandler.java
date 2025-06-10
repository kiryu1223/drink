package io.github.kiryu1223.drink.base.toBean.handler.impl.datetime;


import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.lang.reflect.Type;
import java.sql.*;
import java.time.LocalDateTime;

public class LocalDateTimeTypeHandler implements ITypeHandler<LocalDateTime> {
    @Override
    public LocalDateTime getValue(ResultSet resultSet, int index, Type type) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp(index);
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, LocalDateTime localDateTime) throws SQLException {
        if (localDateTime == null) {
            preparedStatement.setNull(index, JDBCType.TIMESTAMP.getVendorTypeNumber());
        }
        else {
            preparedStatement.setTimestamp(index, Timestamp.valueOf(localDateTime));
        }
    }
}
