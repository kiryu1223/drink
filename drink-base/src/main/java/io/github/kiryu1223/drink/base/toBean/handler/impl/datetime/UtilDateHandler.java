package io.github.kiryu1223.drink.base.toBean.handler.impl.datetime;


import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.Date;

public class UtilDateHandler implements ITypeHandler<Date> {
    @Override
    public Date getValue(ResultSet resultSet, int index, Type type) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp(index);
        return timestamp == null ? null : Date.from(timestamp.toInstant());
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Date date) throws SQLException {
        if (date == null) {
            preparedStatement.setNull(index, JDBCType.TIMESTAMP.getVendorTypeNumber());
        }
        else {
            preparedStatement.setTimestamp(index, Timestamp.from(date.toInstant()));
        }
    }
}
