package io.github.kiryu1223.drink.base.toBean.handler.impl.varchar;


import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.lang.reflect.Type;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StringTypeHandler implements ITypeHandler<String>
{
    @Override
    public String getValue(ResultSet resultSet, int index, Type type) throws SQLException
    {
        return resultSet.getString(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, String s) throws SQLException
    {
        if (s == null) {
            preparedStatement.setNull(index, JDBCType.VARCHAR.getVendorTypeNumber());
        }
        else {
            preparedStatement.setString(index, s);
        }
    }
}
