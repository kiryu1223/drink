package io.github.kiryu1223.drink.base.toBean.handler;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class UnKnowTypeHandler<T> implements ITypeHandler<T>
{
    @Override
    public T getValue(ResultSet resultSet, int index, Type type) throws SQLException
    {
        if (type instanceof Class<?>) {
            return resultSet.getObject(index, (Class<T>) type);
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
