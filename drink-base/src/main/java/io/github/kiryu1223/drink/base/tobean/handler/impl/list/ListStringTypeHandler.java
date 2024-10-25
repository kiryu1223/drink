package io.github.kiryu1223.drink.base.tobean.handler.impl.list;


import io.github.kiryu1223.drink.base.tobean.handler.ITypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListStringTypeHandler implements ITypeHandler<List<String>>
{
    @Override
    public List<String> getValue(ResultSet resultSet, int index, Class<?> c) throws SQLException
    {
        String string = resultSet.getString(index);
        return Arrays.stream(string.split(",")).collect(Collectors.toList());
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, List<String> strings) throws SQLException
    {
        preparedStatement.setString(index, String.join(",", strings));
    }
}
