package io.github.kiryu1223.drink.base.toBean.handler;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class JsonTypeHandler<T> implements ITypeHandler<T> {

    @Override
    public T getValue(ResultSet resultSet, int index, Type type) throws SQLException
    {
        String json = resultSet.getString(index);
        return jsonToObject(json,type);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, T value) throws SQLException
    {
        preparedStatement.setString(index, objectToJson(value));
    }

    public abstract T jsonToObject(String json, Type type);

    public abstract String objectToJson(T value);
}
