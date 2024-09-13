package io.github.kiryu1223.drink.core.expression.ext.oracle;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlTypeExpression;

import java.util.List;

public class OracleTypeExpression extends SqlTypeExpression
{
    protected OracleTypeExpression(Class<?> type)
    {
        super(type);
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        if (isBool())
        {
            return "BOOLEAN";
        }
        else if (isDate() || isDatetime())
        {
            return "DATE";
        }
        else if (isNumber())
        {
            return "NUMBER";
        }
        else if (isString())
        {
            return "VARCHAR(255)";
        }
        else if (isChar())
        {
            return "CHAR(4)";
        }
        throw new UnsupportedOperationException("不支持的Java类型:" + type.getName());
    }
}
