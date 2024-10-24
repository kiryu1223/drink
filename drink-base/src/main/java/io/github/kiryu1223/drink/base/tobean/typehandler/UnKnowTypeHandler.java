package io.github.kiryu1223.drink.base.tobean.typehandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UnKnowTypeHandler<T> implements ITypeHandler<T>
{
    @Override
    public T getValue(ResultSet resultSet, int index, Class<T> c) throws SQLException
    {
        return resultSet.getObject(index, c);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, T value) throws SQLException
    {
        preparedStatement.setObject(index, value);
    }
}
