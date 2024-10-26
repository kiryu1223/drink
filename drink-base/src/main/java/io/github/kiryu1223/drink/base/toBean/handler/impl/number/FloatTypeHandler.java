package io.github.kiryu1223.drink.base.toBean.handler.impl.number;


import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FloatTypeHandler implements ITypeHandler<Float>
{
    @Override
    public Float getValue(ResultSet resultSet, int index, Class<?> c) throws SQLException
    {
        float aFloat = resultSet.getFloat(index);
        return resultSet.wasNull() ? null : aFloat;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Float aFloat) throws SQLException
    {
        preparedStatement.setFloat(index, aFloat);
    }
}
