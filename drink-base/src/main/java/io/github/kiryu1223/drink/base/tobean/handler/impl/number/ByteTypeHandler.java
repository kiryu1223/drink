package io.github.kiryu1223.drink.base.tobean.handler.impl.number;

import io.github.kiryu1223.drink.base.tobean.handler.ITypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ByteTypeHandler implements ITypeHandler<Byte>
{
    @Override
    public Byte getValue(ResultSet resultSet, int index, Class<?> c) throws SQLException
    {
        return resultSet.getByte(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Byte aByte) throws SQLException
    {
        preparedStatement.setByte(index, aByte);
    }
}
