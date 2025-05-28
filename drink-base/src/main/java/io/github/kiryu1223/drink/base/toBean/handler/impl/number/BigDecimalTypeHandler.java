package io.github.kiryu1223.drink.base.toBean.handler.impl.number;


import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BigDecimalTypeHandler implements ITypeHandler<BigDecimal>
{
    @Override
    public BigDecimal getValue(ResultSet resultSet, int index, Type type) throws SQLException
    {
        return resultSet.getBigDecimal(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, BigDecimal bigDecimal) throws SQLException
    {
        if (bigDecimal == null) {
            preparedStatement.setNull(index, JDBCType.DECIMAL.getVendorTypeNumber());
        }
        else {
            preparedStatement.setBigDecimal(index, bigDecimal);
        }
    }
}
