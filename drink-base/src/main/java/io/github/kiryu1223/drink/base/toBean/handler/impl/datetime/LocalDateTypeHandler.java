package io.github.kiryu1223.drink.base.toBean.handler.impl.datetime;


import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class LocalDateTypeHandler implements ITypeHandler<LocalDate>
{
    @Override
    public LocalDate getValue(ResultSet resultSet, int index, Type type) throws SQLException
    {
        Date date = resultSet.getDate(index);
        return date == null ? null : date.toLocalDate();
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, LocalDate localDate) throws SQLException
    {
        preparedStatement.setDate(index, Date.valueOf(localDate));
    }
}
