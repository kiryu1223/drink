package io.github.kiryu1223.project.handler;

import io.github.kiryu1223.drink.base.toBean.handler.EnumTypeHandler;
import io.github.kiryu1223.project.pojos.Gender;


public class GenderHandler extends EnumTypeHandler<Gender>
{
    @Override
    public Class<Gender> getActualType()
    {
        return Gender.class;
    }
}
