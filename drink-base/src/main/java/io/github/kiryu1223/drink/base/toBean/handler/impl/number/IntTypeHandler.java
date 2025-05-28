package io.github.kiryu1223.drink.base.toBean.handler.impl.number;


import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.lang.reflect.Type;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IntTypeHandler implements ITypeHandler<Integer>
{
    @Override
    public Integer getValue(ResultSet resultSet, int index, Type type) throws SQLException
    {
        int anInt = resultSet.getInt(index);
        return resultSet.wasNull() ? null : anInt;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Integer integer) throws SQLException
    {
        if (integer == null) {
            preparedStatement.setNull(index, JDBCType.INTEGER.getVendorTypeNumber());
        }
        else {
            preparedStatement.setInt(index, integer);
        }
    }
}
