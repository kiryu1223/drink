package io.github.kiryu1223.drink.base.tobean.handler.impl.varchar;


import io.github.kiryu1223.drink.base.tobean.handler.ITypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CharTypeHandler implements ITypeHandler<Character>
{
    @Override
    public Character getValue(ResultSet resultSet, int index,Class<?> c) throws SQLException
    {
        return resultSet.getString(index).charAt(0);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Character character) throws SQLException
    {
        preparedStatement.setString(index, character.toString());
    }
}
