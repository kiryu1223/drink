package io.github.kiryu1223.drink.base.toBean.handler.impl.datetime;


import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.lang.reflect.Type;
import java.sql.*;

public class TimeTypeHandler implements ITypeHandler<Time> {
    @Override
    public Time getValue(ResultSet resultSet, int index, Type type) throws SQLException {
        return resultSet.getTime(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Time time) throws SQLException {
        if (time == null) {
            preparedStatement.setNull(index, JDBCType.TIME.getVendorTypeNumber());
        }
        else {
            preparedStatement.setTime(index, time);
        }
    }
}
