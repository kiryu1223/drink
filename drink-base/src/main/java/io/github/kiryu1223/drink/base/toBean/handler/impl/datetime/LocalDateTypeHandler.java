package io.github.kiryu1223.drink.base.toBean.handler.impl.datetime;


import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.lang.reflect.Type;
import java.sql.*;
import java.time.LocalDate;

public class LocalDateTypeHandler implements ITypeHandler<LocalDate> {
    @Override
    public LocalDate getValue(ResultSet resultSet, int index, Type type) throws SQLException {
        Date date = resultSet.getDate(index);
        return date == null ? null : date.toLocalDate();
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, LocalDate localDate) throws SQLException {
        if (localDate == null) {
            preparedStatement.setNull(index, JDBCType.DATE.getVendorTypeNumber());
        }
        else {
            preparedStatement.setDate(index, Date.valueOf(localDate));
        }
    }
}
