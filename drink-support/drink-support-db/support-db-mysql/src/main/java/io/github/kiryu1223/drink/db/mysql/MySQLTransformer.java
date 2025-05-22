package io.github.kiryu1223.drink.db.mysql;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.transform.Transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.github.kiryu1223.drink.base.util.DrinkUtil.*;


public class MySQLTransformer implements Transformer
{
    private final SqlExpressionFactory factory;

    public MySQLTransformer(IConfig config)
    {
        factory = config.getSqlExpressionFactory();
    }

    @Override
    public SqlExpressionFactory getSqlExpressionFactory()
    {
        return factory;
    }

    private String getUnit(Class<?> type) {
        String unit;
        if (isChar(type) || isString(type)) {
            unit = "CHAR";
        }
        else if (isTime(type)) {
            unit = "TIME";
        }
        else if (isDate(type)) {
            unit = "DATE";
        }
        else if (isDateTime(type)) {
            unit = "DATETIME";
        }
        else if (isFloat(type) || isDouble(type)) {
            unit = "DECIMAL(32,16)";
        }
        else if (isDecimal(type)) {
            unit = "DECIMAL(32,18)";
        }
        else if (isByte(type) || isShort(type) || isInt(type) || isLong(type)) {
            unit = "SIGNED";
        }
        else {
            throw new DrinkException("不支持的Java类型:" + type.getName());
        }
        return unit;
    }

    @Override
    public ISqlExpression typeCast(ISqlExpression value, Class<?> type)
    {
        List<String> templates = Arrays.asList("CAST("," AS " + getUnit(type) + ")");
        List<ISqlExpression> sqlExpressions = Collections.singletonList(value);
        return factory.template(templates, sqlExpressions);
    }
}
