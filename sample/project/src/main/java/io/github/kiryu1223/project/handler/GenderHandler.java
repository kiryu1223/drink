package io.github.kiryu1223.project.handler;

import io.github.kiryu1223.drink.base.toBean.handler.EnumTypeHandler;
import io.github.kiryu1223.project.pojos.Gender;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class GenderHandler extends EnumTypeHandler<Gender>
{
    @Override
    public Type getActualType()
    {
        return Gender.class;
    }
}
