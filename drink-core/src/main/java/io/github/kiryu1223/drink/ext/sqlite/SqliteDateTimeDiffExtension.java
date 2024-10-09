package io.github.kiryu1223.drink.ext.sqlite;

import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.core.expression.SqlSingleValueExpression;
import io.github.kiryu1223.drink.exception.DrinkException;
import io.github.kiryu1223.drink.ext.BaseSqlExtension;
import io.github.kiryu1223.drink.ext.FunctionBox;
import io.github.kiryu1223.drink.ext.SqlTimeUnit;

import java.lang.reflect.Method;
import java.util.List;

public class SqliteDateTimeDiffExtension extends BaseSqlExtension
{
    @Override
    public FunctionBox parse(Method sqlFunc, List<SqlExpression> args)
    {
        FunctionBox box = new FunctionBox();
        List<String> functions = box.getFunctions();
        List<SqlExpression> sqlExpressions = box.getSqlExpressions();
        SqlExpression unit = args.get(0);
        SqlExpression from = args.get(1);
        SqlExpression to = args.get(2);
        if (unit instanceof SqlSingleValueExpression)
        {
            SqlSingleValueExpression sqlSingleValueExpression = (SqlSingleValueExpression) unit;
            SqlTimeUnit timeUnit = (SqlTimeUnit) sqlSingleValueExpression.getValue();
            String aa="(STRFTIME('%Y','2020-10-17') - STRFTIME('%Y','2019-05-17'))";
            switch (timeUnit)
            {
                case YEAR:
                    functions.add("(STRFTIME('%Y',");
                    sqlExpressions.add(to);
                    functions.add(") - STRFTIME('%Y',");
                    sqlExpressions.add(from);
                    functions.add("))");
                    break;
                case MONTH:
                    functions.add("((STRFTIME('%Y',");
                    sqlExpressions.add(to);
                    functions.add(") - STRFTIME('%Y',");
                    sqlExpressions.add(from);
                    functions.add(")) * 12 + STRFTIME('%m',");
                    sqlExpressions.add(to);
                    functions.add(") - STRFTIME('%m',");
                    sqlExpressions.add(from);
                    functions.add("))");
                    break;
                case WEEK:
                    functions.add("((JULIANDAY(");
                    sqlExpressions.add(to);
                    functions.add(") - JULIANDAY(");
                    sqlExpressions.add(from);
                    functions.add(")) / 7)");
                    break;
                case DAY:
                    functions.add("(JULIANDAY(");
                    sqlExpressions.add(to);
                    functions.add(") - JULIANDAY(");
                    sqlExpressions.add(from);
                    functions.add("))");
                    break;
                case HOUR:
                    functions.add("((JULIANDAY(");
                    sqlExpressions.add(to);
                    functions.add(") - JULIANDAY(");
                    sqlExpressions.add(from);
                    functions.add(")) * 24)");
                    break;
                case MINUTE:
                    functions.add("((JULIANDAY(");
                    sqlExpressions.add(to);
                    functions.add(") - JULIANDAY(");
                    sqlExpressions.add(from);
                    functions.add(")) * 24 * 60)");
                    break;
                case SECOND:
                    functions.add("((JULIANDAY(");
                    sqlExpressions.add(to);
                    functions.add(") - JULIANDAY(");
                    sqlExpressions.add(from);
                    functions.add(")) * 24 * 60 * 60)");
                    break;
                case MILLISECOND:
                    functions.add("((JULIANDAY(");
                    sqlExpressions.add(to);
                    functions.add(") - JULIANDAY(");
                    sqlExpressions.add(from);
                    functions.add(")) * 24 * 60 * 60 * 1000)");
                    break;
                case MICROSECOND:
                    functions.add("((JULIANDAY(");
                    sqlExpressions.add(to);
                    functions.add(") - JULIANDAY(");
                    sqlExpressions.add(from);
                    functions.add(")) * 24 * 60 * 60 * 1000 * 1000)");
                    break;
            }
        }
        else
        {
            throw new DrinkException("SqlTimeUnit必须为可求值的");
        }
        return box;
    }
}
