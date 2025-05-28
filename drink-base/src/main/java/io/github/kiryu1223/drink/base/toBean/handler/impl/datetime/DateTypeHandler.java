package io.github.kiryu1223.drink.base.toBean.handler.impl.datetime;


import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.lang.reflect.Type;
import java.sql.*;

public class DateTypeHandler implements ITypeHandler<Date> {
    @Override
    public Date getValue(ResultSet resultSet, int index, Type type) throws SQLException {
        return resultSet.getDate(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Date date) throws SQLException {
        if (date == null) {
            preparedStatement.setNull(index, JDBCType.DATE.getVendorTypeNumber());
        }
        else {
            preparedStatement.setDate(index, date);
        }
    }
}
