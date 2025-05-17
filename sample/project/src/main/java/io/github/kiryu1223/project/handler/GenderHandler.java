package io.github.kiryu1223.project.handler;

import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;
import io.github.kiryu1223.project.pojos.Gender;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenderHandler implements ITypeHandler<Gender>
{
    @Override
    public Gender getValue(ResultSet resultSet, int index, Type type) throws SQLException
    {
        return Gender.valueOf(resultSet.getString(index));
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Gender value) throws SQLException
    {
        preparedStatement.setString(index,value.name());
    }
}
