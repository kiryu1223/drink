package io.github.kiryu1223.drink.base.toBean.handler.impl.number;

import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.lang.reflect.Type;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ByteTypeHandler implements ITypeHandler<Byte> {
    @Override
    public Byte getValue(ResultSet resultSet, int index, Type type) throws SQLException {
        byte aByte = resultSet.getByte(index);
        return resultSet.wasNull() ? null : aByte;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Byte aByte) throws SQLException {
        if (aByte == null) {
            preparedStatement.setNull(index, JDBCType.TINYINT.getVendorTypeNumber());
        }
        else {
            preparedStatement.setByte(index, aByte);
        }
    }
}
