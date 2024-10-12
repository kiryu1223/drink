package io.github.kiryu1223.drink.core.builder;

import java.sql.ResultSet;
import java.sql.SQLException;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.isChar;
import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.upperClass;

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
        else
        {
            return resultSet.getObject(1, upperClass(type));
        }
    }
}
