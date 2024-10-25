package io.github.kiryu1223.drink.base.tobean.handler.impl.number;


import io.github.kiryu1223.drink.base.tobean.handler.ITypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BoolTypeHandler implements ITypeHandler<Boolean>
{
    @Override
    public Boolean getValue(ResultSet resultSet, int index, Class<?> c) throws SQLException
    {
        return resultSet.getBoolean(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Boolean aBoolean) throws SQLException
    {
        preparedStatement.setBoolean(index, aBoolean);
    }
}
