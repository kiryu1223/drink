package io.github.kiryu1223.drink.core.builder.pgsql;

import io.github.kiryu1223.drink.core.builder.DefaultResultSetValueGetter;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.*;

public class PostgreSQLResultSetValueGetter extends DefaultResultSetValueGetter
{
//    @Override
//    public Object getValueByName(ResultSet resultSet, String name, Class<?> type) throws NoSuchFieldException, IllegalAccessException, SQLException
//    {
//        if (type.isEnum())
//        {
//            String Enum = resultSet.getString(name);
//            return type.getField(Enum).get(null);
//        }
//        else if (isChar(type))
//        {
//            String result = resultSet.getString(name);
//            return (result != null && !result.isEmpty()) ? result.charAt(0) : null;
//        }
//        else if (isByte(type))
//        {
//            return resultSet.getByte(name);
//        }
//        else if (type == LocalDateTime.class)
//        {
//            Timestamp timestamp = resultSet.getTimestamp(name);
//            return timestamp.toLocalDateTime();
//        }
//        else if (type == LocalDate.class)
//        {
//            Date date = resultSet.getDate(name);
//            return date.toLocalDate();
//        }
//        else if (type == LocalTime.class)
//        {
//            Time time = resultSet.getTime(name);
//            return time.toLocalTime();
//        }
//        else
//        {
//            return resultSet.getObject(name, upperClass(type));
//        }
//    }

    @Override
    public Object getValueByIndex(ResultSet resultSet,int index, Class<?> type) throws NoSuchFieldException, IllegalAccessException, SQLException
    {
        if (type.isEnum())
        {
            String Enum = resultSet.getString(index);
            return type.getField(Enum).get(null);
        }
        else if (isChar(type))
        {
            String result = resultSet.getString(index);
            return (result != null && !result.isEmpty()) ? result.charAt(0) : null;
        }
        else if (isShort(type))
        {
            return resultSet.getShort(index);
        }
        else if (isByte(type))
        {
            return resultSet.getByte(index);
        }
        else if (type == LocalDateTime.class)
        {
            Timestamp timestamp = resultSet.getTimestamp(index);
            return timestamp.toLocalDateTime();
        }
        else if (type == LocalDate.class)
        {
            Date date = resultSet.getDate(index);
            return date.toLocalDate();
        }
        else if (type == LocalTime.class)
        {
            Time time = resultSet.getTime(index);
            return time.toLocalTime();
        }
        else
        {
            return resultSet.getObject(index, upperClass(type));
        }
    }
}
