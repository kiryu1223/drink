package io.github.kiryu1223.drink.base.tobean.handler.impl.datetime;



import io.github.kiryu1223.drink.base.tobean.handler.ITypeHandler;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocalDateTypeHandler implements ITypeHandler<LocalDate>
{
    @Override
    public LocalDate getValue(ResultSet resultSet, int index,Class<?> c) throws SQLException
    {
        return resultSet.getDate(index).toLocalDate();
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, LocalDate localDate) throws SQLException
    {
        preparedStatement.setDate(index, Date.valueOf(localDate));
    }
}
