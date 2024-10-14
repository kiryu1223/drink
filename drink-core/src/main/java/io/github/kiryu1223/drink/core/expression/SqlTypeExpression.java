package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public abstract class SqlTypeExpression extends SqlExpression
{
    protected final Class<?> type;

    protected SqlTypeExpression(Class<?> type)
    {
        this.type = type;
    }

    public Class<?> getType()
    {
        return type;
    }

    @Override
    public <T extends SqlExpression> T copy(Config config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return (T) factory.type(type);
    }

    protected boolean isBool()
    {
        return type == boolean.class || type == Boolean.class;
    }

    protected boolean isString()
    {
        return type == String.class;
    }

    protected boolean isChar()
    {
        return type == char.class || type == Character.class;
    }

    protected boolean isTime()
    {
        return type == Time.class || type == LocalTime.class;
    }

    protected boolean isDate()
    {
        return type == java.sql.Date.class || type == LocalDate.class;
    }

    protected boolean isDatetime()
    {
        return type == java.util.Date.class || type == LocalDateTime.class || type == Timestamp.class;
    }

    protected boolean isDecimal()
    {
        return type == BigDecimal.class;
    }

    protected boolean isFloat()
    {
        return type == float.class || type == Float.class;
    }

    protected boolean isDouble()
    {
        return type == double.class || type == Double.class;
    }

    protected boolean isByte()
    {
        return type == byte.class || type == Byte.class;
    }

    protected boolean isShort()
    {
        return type == short.class || type == Short.class;
    }

    protected boolean isInt()
    {
        return type == int.class || type == Integer.class;
    }

    protected boolean isLong()
    {
        return type == long.class || type == Long.class;
    }

    protected boolean isNumber()
    {
        return isByte() || isShort() || isInt() || isLong() || isFloat() || isDouble() || isDecimal();
    }
}
