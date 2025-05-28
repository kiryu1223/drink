package io.github.kiryu1223.drink.base.toBean.handler.impl.number;


import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.lang.reflect.Type;
import java.sql.*;

public class ShortTypeHandler implements ITypeHandler<Short> {
    @Override
    public Short getValue(ResultSet resultSet, int index, Type type) throws SQLException {
        short aShort = resultSet.getShort(index);
        return resultSet.wasNull() ? null : aShort;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Short aShort) throws SQLException {
        if (aShort == null) {
            preparedStatement.setNull(index, JDBCType.SMALLINT.getVendorTypeNumber());
        }
        else {
            preparedStatement.setShort(index, aShort);
        }
    }
}
