package io.github.kiryu1223.drink.base.toBean.handler.impl.datetime;


import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.lang.reflect.Type;
import java.sql.*;
import java.time.LocalTime;

public class LocalTimeTypeHandler implements ITypeHandler<LocalTime>
{
    @Override
    public LocalTime getValue(ResultSet resultSet, int index, Type type) throws SQLException
    {
        Time time = resultSet.getTime(index);
        return time == null ? null : time.toLocalTime();
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, LocalTime localTime) throws SQLException
    {
        if (localTime == null) {
            preparedStatement.setNull(index, JDBCType.TIME.getVendorTypeNumber());
        }
        else {
            preparedStatement.setTime(index, Time.valueOf(localTime));
        }
    }
}
