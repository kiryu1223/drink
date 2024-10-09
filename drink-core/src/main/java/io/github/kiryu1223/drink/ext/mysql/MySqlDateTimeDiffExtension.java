package io.github.kiryu1223.drink.ext.mysql;

import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.core.expression.SqlSingleValueExpression;
import io.github.kiryu1223.drink.exception.DrinkException;
import io.github.kiryu1223.drink.ext.BaseSqlExtension;
import io.github.kiryu1223.drink.ext.FunctionBox;
import io.github.kiryu1223.drink.ext.SqlTimeUnit;

import java.lang.reflect.Method;
import java.util.List;

public class MySqlDateTimeDiffExtension extends BaseSqlExtension
{
//    static
//    {
//        new MySqlDateTimeDiffExtension();
//    }

    @Override
    public FunctionBox parse(Method sqlFunc, List<SqlExpression> args)
    {
        FunctionBox box = new FunctionBox();
        List<String> functions = box.getFunctions();
        List<SqlExpression> sqlExpressions = box.getSqlExpressions();
        SqlExpression expression = args.get(0);
        if (expression instanceof SqlSingleValueExpression)
        {
            SqlSingleValueExpression sqlSingleValueExpression = (SqlSingleValueExpression) expression;
            SqlTimeUnit timeUnit = (SqlTimeUnit) sqlSingleValueExpression.getValue();
            if (timeUnit == SqlTimeUnit.MILLISECOND)
            {
                functions.add("(TIMESTAMPDIFF(MICROSECOND,");
                sqlExpressions.add(args.get(1));
                functions.add(",");
                sqlExpressions.add(args.get(2));
                functions.add(") / 1000)");
            }
            else
            {
                functions.add("TIMESTAMPDIFF(");
                sqlExpressions.add(expression);
                functions.add(",");
                sqlExpressions.add(args.get(1));
                functions.add(",");
                sqlExpressions.add(args.get(2));
                functions.add(")");
            }
        }
        else
        {
            throw new DrinkException("SqlTimeUnit必须为可求值的");
        }
        return box;
    }
}
