package io.github.kiryu1223.drink.exception;

import io.github.kiryu1223.drink.ext.DbType;

public class DrinkIntervalException extends DrinkException
{
    public DrinkIntervalException(DbType type)
    {
        super(type.name() + "下的date加减运算函数必须为字面量或者java引用（不可以是数据库字段引用）");
    }
}
