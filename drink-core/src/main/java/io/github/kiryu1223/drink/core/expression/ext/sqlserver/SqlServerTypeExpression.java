package io.github.kiryu1223.drink.core.expression.ext.sqlserver;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlTypeExpression;

import java.sql.Time;
import java.util.List;

public class SqlServerTypeExpression extends SqlTypeExpression
{
    protected SqlServerTypeExpression(Class<?> type)
    {
        super(type);
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
        else if (isByte())
        {
            return "TINYINT";
        }
        else if (isShort())
        {
            return "SMALLINT";
        }
        else if (isInt())
        {
            return "INT";
        }
        else if (isLong())
        {
            return "BIGINT";
        }
        else if (isFloat() || isDouble())
        {
            return "DECIMAL(32,16)";
        }
        else if (isDecimal())
        {
            return "DECIMAL(36,18)";
        }
        else if (isString())
        {
            return "NVARCHAR";
        }
        else if (isChar())
        {
            return "NCHAR(1)";
        }
        throw new UnsupportedOperationException("不支持的Java类型:" + type.getName());
    }
}
