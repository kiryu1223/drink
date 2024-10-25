package io.github.kiryu1223.drink.core.sqlExt.oracle;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTypeExpression;
import io.github.kiryu1223.drink.base.sqlExt.BaseSqlExtension;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.*;

public class OracleCastExtension extends BaseSqlExtension
{
    @Override
    public ISqlExpression parse(IConfig config, Method sqlFunc, List<ISqlExpression> args)
    {
        List<String> templates = new ArrayList<>();
        List<ISqlExpression> sqlExpressions = new ArrayList<>();
        ISqlExpression expression = args.get(1);
        ISqlTypeExpression typeExpression = (ISqlTypeExpression) expression;
        Class<?> type = typeExpression.getType();
        if (isBool(type))
        {
            templates.add("CAST(");
            sqlExpressions.add(args.get(0));
            templates.add(" AS BOOLEAN)");
        }
        else if (isDate(type) || isDateTime(type))
        {
            templates.add("CAST(");
            sqlExpressions.add(args.get(0));
            templates.add(" AS DATE)");
        }
        else if (isByte(type) || isShort(type) || isInt(type) || isLong(type) || isFloat(type) || isDouble(type) || isDecimal(type))
        {
            templates.add("CAST(");
            sqlExpressions.add(args.get(0));
            templates.add(" AS NUMBER)");
        }
        else if (isString(type))
        {
            templates.add("TO_CHAR(");
            sqlExpressions.add(args.get(0));
            templates.add(")");
        }
        else if (isChar(type))
        {
            templates.add("SUBSTR(TO_CHAR(");
            sqlExpressions.add(args.get(0));
            templates.add("),1,1)");
        }
        else
        {
            throw new UnsupportedOperationException("不支持的Java类型:" + type.getName());
        }
        return config.getSqlExpressionFactory().template(templates, sqlExpressions);
    }
}
