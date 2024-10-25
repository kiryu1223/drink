package io.github.kiryu1223.drink.base.toBean.handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ITypeHandler<T>
{
    T getValue(ResultSet resultSet, int index, Class<?> c) throws SQLException;

    void setValue(PreparedStatement preparedStatement, int index, T value) throws SQLException;
}
