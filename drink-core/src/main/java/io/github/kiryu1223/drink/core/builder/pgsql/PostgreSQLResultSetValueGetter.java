package io.github.kiryu1223.drink.core.builder.pgsql;

import io.github.kiryu1223.drink.core.builder.DefaultResultSetValueGetter;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.*;

public class PostgreSQLResultSetValueGetter extends DefaultResultSetValueGetter
{
    @Override
    public Object getValueByName(ResultSet resultSet, String name, Class<?> type) throws NoSuchFieldException, IllegalAccessException, SQLException
    {
        if (type.isEnum())
        {
            String Enum = resultSet.getString(name);
            return type.getField(Enum).get(null);
        }
        else if (isChar(type))
        {
            String result = resultSet.getString(name);
            return (result != null && !result.isEmpty()) ? result.charAt(0) : null;
        }
        else if (isByte(type))
        {
            return resultSet.getByte(name);
        }
        else if (type == LocalDateTime.class)
        {
            Timestamp timestamp = resultSet.getTimestamp(name);
            return timestamp.toLocalDateTime();
        }
        else if (type == LocalDate.class)
        {
            Date date = resultSet.getDate(name);
            return date.toLocalDate();
        }
        else if (type == LocalTime.class)
        {
            Time time = resultSet.getTime(name);
            return time.toLocalTime();
        }
        else
        {
            return resultSet.getObject(name, upperClass(type));
        }
    }

    @Override
    public Object getFirstValue(ResultSet resultSet, Class<?> type) throws NoSuchFieldException, IllegalAccessException, SQLException
    {
        if (type.isEnum())
        {
            String Enum = resultSet.getString(1);
            return type.getField(Enum).get(null);
        }
        else if (isChar(type))
        {
            String result = resultSet.getString(1);
            return (result != null && !result.isEmpty()) ? result.charAt(0) : null;
        }
        else if (isShort(type))
        {
            return resultSet.getShort(1);
        }
        else if (isByte(type))
        {
            return resultSet.getByte(1);
        }
        else if (type == LocalDateTime.class)
        {
            Timestamp timestamp = resultSet.getTimestamp(1);
            return timestamp.toLocalDateTime();
        }
        else if (type == LocalDate.class)
        {
            Date date = resultSet.getDate(1);
            return date.toLocalDate();
        }
        else if (type == LocalTime.class)
        {
            Time time = resultSet.getTime(1);
            return time.toLocalTime();
        }
        else
        {
            return resultSet.getObject(1, upperClass(type));
        }
    }
}
