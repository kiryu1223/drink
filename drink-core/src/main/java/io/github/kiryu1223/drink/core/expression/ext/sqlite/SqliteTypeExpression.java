package io.github.kiryu1223.drink.core.expression.ext.sqlite;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlTypeExpression;

import java.util.List;

public class SqliteTypeExpression extends SqlTypeExpression
{

    protected SqliteTypeExpression(Class<?> type)
    {
        super(type);
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        if (isChar() || isString())
        {
            return "TEXT";
        }
        else if (isByte() || isShort() || isInt() || isLong())
        {
            return "INTEGER";
        }
        else if (isFloat() || isDouble() || isDecimal())
        {
            return "REAL";
        }
        throw new UnsupportedOperationException("不支持的Java类型:" + type.getName());
    }
}
