package io.github.kiryu1223.drink.base.tobean.typehandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ITypeHandler<T>
{
    T getValue(ResultSet resultSet, int index, Class<T> c) throws SQLException;

    void setValue(PreparedStatement preparedStatement, int index, T value) throws SQLException;
}
