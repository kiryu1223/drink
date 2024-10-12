package io.github.kiryu1223.drink.core.builder.sqlite;

import io.github.kiryu1223.drink.core.builder.DefaultResultSetValueGetter;

import java.sql.ResultSet;
import java.sql.SQLException;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.*;

public class SqliteResultSetValueGetter extends DefaultResultSetValueGetter
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
        else if (isShort(type))
        {
            return resultSet.getShort(name);
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
        else
        {
            return resultSet.getObject(1, upperClass(type));
        }
    }
}
