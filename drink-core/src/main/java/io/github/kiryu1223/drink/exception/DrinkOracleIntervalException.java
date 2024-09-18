package io.github.kiryu1223.drink.exception;

public class DrinkOracleIntervalException extends DrinkException
{
    public DrinkOracleIntervalException()
    {
        super("Oracle下的date加减运算函数必须为字面量或者java引用（不可以是数据库字段引用）");
    }
}
