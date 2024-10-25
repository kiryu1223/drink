package io.github.kiryu1223.drink.base.tobean.handler.impl.datetime;


import io.github.kiryu1223.drink.base.tobean.handler.ITypeHandler;
import io.github.kiryu1223.drink.base.tobean.handler.TypeRef;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DateTypeHandler implements ITypeHandler<Date>
{
    @Override
    public Date getValue(ResultSet resultSet, int index,Class<?> c) throws SQLException
    {
        return resultSet.getDate(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Date date) throws SQLException
    {
        preparedStatement.setDate(index,date);
    }
}
