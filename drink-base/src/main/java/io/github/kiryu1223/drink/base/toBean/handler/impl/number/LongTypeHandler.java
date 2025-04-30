package io.github.kiryu1223.drink.base.toBean.handler.impl.number;


import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LongTypeHandler implements ITypeHandler<Long>
{
    @Override
    public Long getValue(ResultSet resultSet, int index, Type type) throws SQLException
    {
        long aLong = resultSet.getLong(index);
        return resultSet.wasNull() ? null : aLong;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Long aLong) throws SQLException
    {
        preparedStatement.setLong(index, aLong);
    }
}
