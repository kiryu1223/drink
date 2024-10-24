package io.github.kiryu1223.drink.ext.mysql;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlSingleValueExpression;
import io.github.kiryu1223.drink.base.sqlext.BaseSqlExtension;
import io.github.kiryu1223.drink.base.sqlext.SqlTimeUnit;
import io.github.kiryu1223.drink.exception.DrinkException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MySqlDateTimeDiffExtension extends BaseSqlExtension
{
    @Override
    public ISqlExpression parse(IConfig config, Method sqlFunc, List<ISqlExpression> args)
    {
        List<String> templates=new ArrayList<>();
        List<ISqlExpression> sqlExpressions=new ArrayList<>();
        ISqlExpression expression = args.get(0);
        if (expression instanceof ISqlSingleValueExpression)
        {
            ISqlSingleValueExpression sqlSingleValueExpression = (ISqlSingleValueExpression) expression;
            SqlTimeUnit timeUnit = (SqlTimeUnit) sqlSingleValueExpression.getValue();
            if (timeUnit == SqlTimeUnit.MILLISECOND)
            {
                templates.add("(TIMESTAMPDIFF(MICROSECOND,");
                sqlExpressions.add(args.get(1));
                templates.add(",");
                sqlExpressions.add(args.get(2));
                templates.add(") / 1000)");
            }
            else
            {
                templates.add("TIMESTAMPDIFF(");
                sqlExpressions.add(expression);
                templates.add(",");
                sqlExpressions.add(args.get(1));
                templates.add(",");
                sqlExpressions.add(args.get(2));
                templates.add(")");
            }
        }
        else
        {
            throw new DrinkException("SqlTimeUnit必须为可求值的");
        }
        return config.getSqlExpressionFactory().template(templates,sqlExpressions);
    }
}
