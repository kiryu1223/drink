package io.github.kiryu1223.drink.core.expression.ext.h2;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlTypeExpression;

import java.util.List;

public class H2TypeExpression extends SqlTypeExpression
{
    protected H2TypeExpression(Class<?> type)
    {
        super(type);
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        if (isTime())
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
        else if (isDecimal())
        {
            return "DECIMAL(36,18)";
        }
        else if (isFloat() || isDouble())
        {
            return "DECIMAL(32,16)";
        }
        else if (isByte() || isShort() || isInt() || isLong())
        {
            return "SIGNED";
        }
        else if (isChar() || isString())
        {
            return "CHAR";
        }
        throw new UnsupportedOperationException("不支持的Java类型:" + type.getName());
    }
}
