package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.sql.Time;
import java.util.List;

public class SqlTypeExpression extends SqlExpression
{
    private final Class<?> type;

    public SqlTypeExpression(Class<?> type)
    {
        this.type = type;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return getSql(config);
    }

    @Override
    public String getSql(Config config)
    {
        if (type == Time.class || type == java.time.LocalTime.class)
        {
            return "TIME";
        }
        else if (type == java.sql.Date.class || type == java.time.LocalDate.class)
        {
            return "DATE";
        }
        else if (type == java.sql.Timestamp.class || type == java.util.Date.class || type == java.time.LocalDateTime.class)
        {
            return "DATETIME";
        }
        else if (type.getName().equals("java.math.BigDecimal"))
        {
            return "DECIMAL(36,18)";
        }
        else if (type == double.class || type == Double.class || type == float.class || type == Float.class)
        {
            return "DECIMAL(32,16)";
        }
        else if (type == byte.class || type == short.class || type == int.class || type == long.class || type == Byte.class || type == Short.class || type == Integer.class || type == Long.class)
        {
            return "SIGNED";
        }
        else if (type == String.class)
        {
            return "CHAR";
        }
        throw new UnsupportedOperationException("不支持当前转换函数:" + type.getName());
    }
}
