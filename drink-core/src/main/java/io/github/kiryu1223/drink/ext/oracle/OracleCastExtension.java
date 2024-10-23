package io.github.kiryu1223.drink.ext.oracle;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.sqlext.BaseSqlExtension;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.isChar;
import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.isString;

public class OracleCastExtension extends BaseSqlExtension
{
    @Override
    public ISqlExpression parse(IConfig config, Method sqlFunc, List<ISqlExpression> args)
    {
        List<String> templates = new ArrayList<>();
        List<ISqlExpression> sqlExpressions = new ArrayList<>();
        ISqlExpression expression = args.get(1);
        if (expression instanceof SqlTypeExpression)
        {
            SqlTypeExpression typeExpression = (SqlTypeExpression) expression;
            Class<?> type = typeExpression.getType();
            if (isString(type))
            {
                templates.add("TO_CHAR(");
                templates.add(")");
                sqlExpressions.add(args.get(0));
                return config.getSqlExpressionFactory().template(templates, sqlExpressions);
            }
            else if (isChar(type))
            {
                templates.add("SUBSTR(TO_CHAR(");
                templates.add("),1,1)");
                sqlExpressions.add(args.get(0));
                return config.getSqlExpressionFactory().template(templates, sqlExpressions);
            }
        }
        templates.add("CAST(");
        templates.add(" AS ");
        templates.add(")");
        sqlExpressions.add(args.get(0));
        sqlExpressions.add(args.get(1));
        return config.getSqlExpressionFactory().template(templates, sqlExpressions);
    }
}
