package io.github.kiryu1223.drink.nnnn.expression.ext.pgsql;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class PostgreSQLTypeExpression extends SqlTypeExpression
{

    protected PostgreSQLTypeExpression(Class<?> type)
    {
        super(type);
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        if (isByte() || isShort())
        {
            return "INT2";
        }
        else if (isInt())
        {
            return "INT4";
        }
        else if (isLong())
        {
            return "INT8";
        }
        else if(isFloat())
        {
            return "FLOAT4";
        }
        else if (isDouble())
        {
            return "FLOAT8";
        }
        else if (isDecimal())
        {
            return "NUMERIC";
        }
        else if (isChar())
        {
            return "CHAR";
        }
        else if (isString())
        {
            return "VARCHAR";
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
            return "TIMESTAMP";
        }
        throw new UnsupportedOperationException("不支持的Java类型:" + type.getName());
    }
}
