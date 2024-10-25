package io.github.kiryu1223.drink.base.toBean.handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UnKnowTypeHandler<T> implements ITypeHandler<T>
{
    @Override
    public T getValue(ResultSet resultSet, int index, Class<?> c) throws SQLException
    {
        return (T) resultSet.getObject(index, c);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, T value) throws SQLException
    {
        preparedStatement.setObject(index, value);
    }
}
