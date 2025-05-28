package io.github.kiryu1223.drink.base.toBean.handler.impl.varchar;


import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.lang.reflect.Type;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CharTypeHandler implements ITypeHandler<Character>
{
    @Override
    public Character getValue(ResultSet resultSet, int index, Type type) throws SQLException
    {
        String string = resultSet.getString(index);
        return string == null ? null : string.charAt(0);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Character character) throws SQLException
    {
        if (character == null) {
            preparedStatement.setNull(index, JDBCType.CHAR.getVendorTypeNumber());
        }
        else {
            preparedStatement.setString(index, character.toString());
        }
    }
}
