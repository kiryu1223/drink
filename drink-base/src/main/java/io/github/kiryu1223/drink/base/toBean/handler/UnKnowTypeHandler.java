package io.github.kiryu1223.drink.base.toBean.handler;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UnKnowTypeHandler<T> implements ITypeHandler<T>
{
    @Override
    public T getValue(ResultSet resultSet, int index, Type type) throws SQLException
    {
        if (type instanceof Class<?>) {
            Class<?> aClass = (Class<?>) type;
            if (aClass.isEnum()) {
                return (T) Enum.valueOf((Class<Enum>) type, resultSet.getString(index));
            }
            else {
                return (T) resultSet.getObject(index, aClass);
            }
        }
        else {
            return (T) resultSet.getObject(index);
        }
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, T value) throws SQLException
    {
        preparedStatement.setObject(index, value);
    }
}
