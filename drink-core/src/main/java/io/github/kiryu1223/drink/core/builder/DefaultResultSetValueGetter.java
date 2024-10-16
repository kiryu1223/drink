package io.github.kiryu1223.drink.core.builder;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.*;

public class DefaultResultSetValueGetter implements IResultSetValueGetter
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
        else if (isString(type))
        {
            return resultSet.getString(name);
        }
        else if (isInt(type))
        {
            return resultSet.getInt(name);
        }
        else if (isDate(type))
        {
            Date date = resultSet.getDate(name);
            return date.toLocalDate();
        }
        else
        {
            return resultSet.getObject(name, upperClass(type));
        }
    }

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
        else if (isString(type))
        {
            return resultSet.getString(index);
        }
        else if (isInt(type))
        {
            return resultSet.getInt(index);
        }
        else if (isDate(type))
        {
            Date date = resultSet.getDate(index);
            return date.toLocalDate();
        }
        else
        {
            return resultSet.getObject(index, upperClass(type));
        }
    }
}
