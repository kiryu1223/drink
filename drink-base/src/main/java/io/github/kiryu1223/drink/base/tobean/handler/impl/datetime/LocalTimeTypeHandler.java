package io.github.kiryu1223.drink.base.tobean.handler.impl.datetime;


import io.github.kiryu1223.drink.base.tobean.handler.ITypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class LocalTimeTypeHandler implements ITypeHandler<LocalTime>
{
    @Override
    public LocalTime getValue(ResultSet resultSet, int index,Class<?> c) throws SQLException
    {
        return resultSet.getTime(index).toLocalTime();
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, LocalTime localTime) throws SQLException
    {
        preparedStatement.setTime(index, Time.valueOf(localTime));
    }
}
