package io.github.kiryu1223.drink.base.tobean.handler.impl.other;

import io.github.kiryu1223.drink.base.tobean.handler.ITypeHandler;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class URLTypeHandler implements ITypeHandler<URL>
{
    @Override
    public URL getValue(ResultSet resultSet, int index, Class<?> c) throws SQLException
    {
        return resultSet.getURL(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, URL value) throws SQLException
    {
        preparedStatement.setURL(index, value);
    }
}
