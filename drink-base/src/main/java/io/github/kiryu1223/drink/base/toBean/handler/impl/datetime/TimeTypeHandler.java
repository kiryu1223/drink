package io.github.kiryu1223.drink.base.toBean.handler.impl.datetime;


import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

public class TimeTypeHandler implements ITypeHandler<Time>
{
    @Override
    public Time getValue(ResultSet resultSet, int index,Class<?> c) throws SQLException
    {
        return resultSet.getTime(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Time time) throws SQLException
    {
        preparedStatement.setTime(index, time);
    }
}
