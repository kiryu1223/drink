package io.github.kiryu1223.drink.db.pg;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTemplateExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTypeCastExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.transform.Transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.github.kiryu1223.drink.base.util.DrinkUtil.*;

public class PostgreSQLTransformer implements Transformer
{
    private final SqlExpressionFactory factory;

    public PostgreSQLTransformer(IConfig config)
    {
        //this.config = config;
        factory = config.getSqlExpressionFactory();
    }
    
    @Override
    public SqlExpressionFactory getSqlExpressionFactory()
    {
        return factory;
    }

    private String getUnit(Class<?> type)
    {
        if (isByte(type) || isShort(type)) {
            return "INT2";
        }
        else if (isInt(type)) {
            return "INT4";
        }
        else if (isLong(type)) {
            return "INT8";
        }
        else if (isFloat(type)) {
            return "FLOAT4";
        }
        else if (isDouble(type)) {
            return "FLOAT8";
        }
        else if (isDecimal(type)) {
            return "NUMERIC";
        }
        else if (isChar(type)) {
            return "CHAR";
        }
        else if (isString(type)) {
            return "VARCHAR";
        }
        else if (isTime(type)) {
            return "TIME";
        }
        else if (isDate(type)) {
            return "DATE";
        }
        else if (isDateTime(type)) {
            return "TIMESTAMP";
        }
        else {
            throw new UnsupportedOperationException("不支持的Java类型:" + type.getName());
        }
    }

    @Override
    public ISqlExpression typeCast(ISqlExpression value, Class<?> type)
    {
        return factory.template(Arrays.asList("","::" + getUnit(type)), Collections.singletonList(value));
    }

    public ISqlExpression If(ISqlExpression cond, ISqlExpression truePart, ISqlExpression falsePart) {
        return factory.template(Arrays.asList("(CASE WHEN ", " THEN ", " ELSE ", " END)"), Arrays.asList(cond, truePart, falsePart));
    }

    public ISqlTemplateExpression groupConcat(List<ISqlExpression> expressions) {
        List<String> strings;
        List<ISqlExpression> args;
        //无分隔符
        if (expressions.size() == 1) {
            ISqlExpression property = expressions.get(0);
            strings = Arrays.asList("STRING_AGG(", "::TEXT,',')");
            args = Collections.singletonList(property);
        }
        //有分隔符
        else {
            ISqlExpression delimiter = expressions.get(0);
            ISqlExpression property = expressions.get(1);
            strings = Arrays.asList("STRING_AGG(", "::TEXT,", ")");
            args = Arrays.asList(property, delimiter);
        }
        return factory.template(strings, args);
    }

    public ISqlTemplateExpression random() {
        return factory.template(Collections.singletonList("RANDOM()"), Collections.emptyList());
    }

    public ISqlTemplateExpression length(ISqlExpression thiz) {
        return factory.template(Arrays.asList("LENGTH(", ")"), Collections.singletonList(thiz));
    }

    public ISqlTemplateExpression indexOf(ISqlExpression thisStr, ISqlExpression subStr) {
        return factory.template(Arrays.asList("STRPOS(", ",", ")"), Arrays.asList(thisStr, subStr));
    }

    public ISqlTemplateExpression indexOf(ISqlExpression thisStr, ISqlExpression subStr, ISqlExpression fromIndex) {
        return factory.template(Arrays.asList("(STRPOS(SUBSTR(", ",", " + 1),", ") + ", ")"), Arrays.asList(thisStr, fromIndex, subStr, fromIndex));
    }
}
