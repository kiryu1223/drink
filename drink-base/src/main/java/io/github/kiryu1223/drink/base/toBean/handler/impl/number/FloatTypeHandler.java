package io.github.kiryu1223.drink.base.toBean.handler.impl.number;


import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.lang.reflect.Type;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FloatTypeHandler implements ITypeHandler<Float> {
    @Override
    public Float getValue(ResultSet resultSet, int index, Type type) throws SQLException {
        float aFloat = resultSet.getFloat(index);
        return resultSet.wasNull() ? null : aFloat;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Float aFloat) throws SQLException {
        if (aFloat == null) {
            preparedStatement.setNull(index, JDBCType.FLOAT.getVendorTypeNumber());
        }
        else {
            preparedStatement.setFloat(index, aFloat);
        }
    }
}
