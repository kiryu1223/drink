package io.github.kiryu1223.drink.exception;

import io.github.kiryu1223.drink.base.DbType;

public class DrinkLimitNotFoundOrderByException extends DrinkException
{
    public DrinkLimitNotFoundOrderByException(DbType dbType)
    {
        super(dbType.name() + "数据库下进行的limit操作需要声明order by的字段，或者为表类指定一个主键");
    }
}
