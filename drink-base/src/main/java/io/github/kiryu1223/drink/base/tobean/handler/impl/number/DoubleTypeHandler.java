package io.github.kiryu1223.drink.base.tobean.handler.impl.number;


import io.github.kiryu1223.drink.base.tobean.handler.ITypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoubleTypeHandler implements ITypeHandler<Double>
{
    @Override
    public Double getValue(ResultSet resultSet, int index, Class<?> c) throws SQLException
    {
        return resultSet.getDouble(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Double aDouble) throws SQLException
    {
        preparedStatement.setDouble(index,aDouble);
    }
}
