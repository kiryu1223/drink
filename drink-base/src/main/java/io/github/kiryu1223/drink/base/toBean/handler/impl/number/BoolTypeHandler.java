package io.github.kiryu1223.drink.base.toBean.handler.impl.number;


import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.lang.reflect.Type;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BoolTypeHandler implements ITypeHandler<Boolean> {
    @Override
    public Boolean getValue(ResultSet resultSet, int index, Type type) throws SQLException {
        boolean aBoolean = resultSet.getBoolean(index);
        return resultSet.wasNull() ? null : aBoolean;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Boolean aBoolean) throws SQLException {
        if (aBoolean == null) {
            preparedStatement.setNull(index, JDBCType.BOOLEAN.getVendorTypeNumber());
        }
        else {
            preparedStatement.setBoolean(index, aBoolean);
        }
    }
}
