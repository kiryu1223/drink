package io.github.kiryu1223.drink.base.toBean.handler.impl.number;


import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BigDecimalTypeHandler implements ITypeHandler<BigDecimal>
{
    @Override
    public BigDecimal getValue(ResultSet resultSet, int index, Class<?> c) throws SQLException
    {
        return resultSet.getBigDecimal(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, BigDecimal bigDecimal) throws SQLException
    {
        preparedStatement.setBigDecimal(index, bigDecimal);
    }
}
