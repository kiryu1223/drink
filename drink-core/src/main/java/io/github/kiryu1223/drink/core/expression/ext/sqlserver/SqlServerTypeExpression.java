package io.github.kiryu1223.drink.core.expression.ext.sqlserver;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlTypeExpression;

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
        if (isBool())
        {
            return "BIT";
        }
        else if (isTime())
        {
            return "TIME";
        }
        else if (isDate())
        {
            return "DATE";
        }
        else if (isDatetime())
        {
            return "DATETIME";
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
