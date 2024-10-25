package io.github.kiryu1223.drink.base.toBean.handler.impl.number;


import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShortTypeHandler implements ITypeHandler<Short>
{
    @Override
    public Short getValue(ResultSet resultSet, int index, Class<?> c) throws SQLException
    {
        return resultSet.getShort(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Short aShort) throws SQLException
    {
        preparedStatement.setShort(index,aShort);
    }
}
