package io.github.kiryu1223.drink.base.tobean.handler.impl.number;


import io.github.kiryu1223.drink.base.tobean.handler.ITypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IntTypeHandler implements ITypeHandler<Integer>
{
    @Override
    public Integer getValue(ResultSet resultSet, int index, Class<?> c) throws SQLException
    {
        return resultSet.getInt(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Integer integer) throws SQLException
    {
        preparedStatement.setInt(index, integer);
    }
}
