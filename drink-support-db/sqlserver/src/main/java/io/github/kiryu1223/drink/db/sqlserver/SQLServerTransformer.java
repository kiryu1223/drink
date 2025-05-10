package io.github.kiryu1223.drink.db.sqlserver;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTemplateExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.expression.SqlOperator;
import io.github.kiryu1223.drink.base.transform.Transformer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.github.kiryu1223.drink.base.util.DrinkUtil.*;

public class SQLServerTransformer implements Transformer
{
    //private final IConfig config;
    private final SqlExpressionFactory factory;

    public SQLServerTransformer(IConfig config)
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
        if (isChar(type))
        {
            return "NCHAR(1)";
        }
        else if (isString(type))
        {
            return "NVARCHAR";
        }
        else if (isTime(type))
        {
            return "TIME";
        }
        else if (isDate(type))
        {
            return "DATE";
        }
        else if (isDateTime(type))
        {
            return "DATETIME";
        }
        else if (isFloat(type) || isDouble(type))
        {
            return "DECIMAL(32,16)";
        }
        else if (isDecimal(type))
        {
            return "DECIMAL(32,18)";
        }
        else if (isByte(type))
        {
            return "TINYINT";
        }
        else if (isShort(type))
        {
            return "SMALLINT";
        }
        else if (isInt(type))
        {
            return "INT";
        }
        else if (isLong(type))
        {
            return "BIGINT";
        }
        else if (isBool(type))
        {
            return "BIT";
        }
        else
        {
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

    public ISqlTemplateExpression groupConcat(List<ISqlExpression> expressions) {
        List<String> strings;
        List<ISqlExpression> args;
        //无分隔符
        if (expressions.size() == 1) {
            ISqlExpression property = expressions.get(0);
            strings = Arrays.asList("STRING_AGG(", ",',')");
            args = Collections.singletonList(property);
        }
        //有分隔符
        else {
            ISqlExpression delimiter = expressions.get(0);
            ISqlExpression property = expressions.get(1);
            strings = Arrays.asList("STRING_AGG(", ",", ")");
            args = Arrays.asList(property, delimiter);
        }
        return factory.template(strings, args);
    }

    public ISqlTemplateExpression atan2(ISqlExpression arg1, ISqlExpression arg2) {
        return factory.template(Arrays.asList("ATN2(", ",", ")"), Arrays.asList(arg1, arg2));
    }

    public ISqlTemplateExpression ceil(ISqlExpression arg) {
        return factory.template(Arrays.asList("CEILING(", ")"), Collections.singletonList(arg));
    }

    public ISqlTemplateExpression log(ISqlExpression arg) {
        List<String> function = Arrays.asList("LOG(", ")");
        return factory.template(function, Collections.singletonList(arg));
    }

    public ISqlTemplateExpression log10(ISqlExpression arg) {
        List<String> function = Arrays.asList("LOG(", ",10)");
        return factory.template(function, Collections.singletonList(arg));
    }

    public ISqlTemplateExpression round(ISqlExpression arg) {
        List<String> function = Arrays.asList("ROUND(", ",0)");
        return factory.template(function, Collections.singletonList(arg));
    }

    public ISqlTemplateExpression length(ISqlExpression thiz) {
        List<String> functions = Arrays.asList("LEN(", ")");
        return factory.template(functions, Collections.singletonList(thiz));
    }

    public ISqlExpression isEmpty(ISqlExpression thiz) {
        return factory.parens(factory.binary(SqlOperator.EQ, factory.template(Arrays.asList("DATALENGTH(", ")"), Collections.singletonList(thiz)), factory.constString("0")));
    }

    public ISqlTemplateExpression indexOf(ISqlExpression thisStr, ISqlExpression subStr) {
        return factory.template(Arrays.asList("CHARINDEX(", ",", ")"), Arrays.asList(subStr, thisStr));
    }

    public ISqlTemplateExpression indexOf(ISqlExpression thisStr, ISqlExpression subStr, ISqlExpression fromIndex) {
        return factory.template(Arrays.asList("CHARINDEX(", ",", ",", ")"), Arrays.asList(subStr, thisStr, fromIndex));
    }

    public ISqlTemplateExpression substring(ISqlExpression thisStr, ISqlExpression beginIndex) {
        return factory.template(Arrays.asList("SUBSTRING(", ",", ",LEN(", ") - (", " - 1))"), Arrays.asList(thisStr, beginIndex, thisStr, beginIndex));
    }

    public ISqlTemplateExpression substring(ISqlExpression thisStr, ISqlExpression beginIndex, ISqlExpression endIndex) {
        return factory.template(Arrays.asList("SUBSTRING(", ",", ",", ")"), Arrays.asList(thisStr, beginIndex, endIndex));
    }
}
