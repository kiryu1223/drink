package io.github.kiryu1223.drink.db.h2;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTemplateExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.transform.Transformer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.github.kiryu1223.drink.base.util.DrinkUtil.*;

public class H2Transformer implements Transformer
{
    private final SqlExpressionFactory factory;

    public H2Transformer(IConfig config)
    {
        factory = config.getSqlExpressionFactory();
    }
    
    @Override
    public SqlExpressionFactory getSqlExpressionFactory()
    {
        return factory;
    }

    private String getUnit(Class<?> type) {
        if (isChar(type) || isString(type)) {
            return "CHAR";
        }
        else if (isTime(type)) {
            return "TIME";
        }
        else if (isDate(type)) {
            return "DATE";
        }
        else if (isDateTime(type)) {
            return "DATETIME";
        }
        else if (isFloat(type) || isDouble(type)) {
            return "DECIMAL(32,16)";
        }
        else if (isDecimal(type)) {
            return "DECIMAL(32,18)";
        }
        else if (isByte(type) || isShort(type) || isInt(type) || isLong(type)) {
            return "SIGNED";
        }
        else {
            throw new UnsupportedOperationException("不支持的Java类型:" + type.getName());
        }
    }

    @Override
    public ISqlExpression typeCast(ISqlExpression value, Class<?> type)
    {
        List<String> templates = Arrays.asList("CAST("," AS " + getUnit(type) + ")");
        List<ISqlExpression> sqlExpressions = Collections.singletonList(value);
        return factory.template(templates, sqlExpressions);
    }
}
