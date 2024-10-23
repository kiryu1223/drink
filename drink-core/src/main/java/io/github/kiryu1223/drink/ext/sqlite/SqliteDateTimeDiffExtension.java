package io.github.kiryu1223.drink.ext.sqlite;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.sqlext.BaseSqlExtension;
import io.github.kiryu1223.drink.base.sqlext.SqlTimeUnit;
import io.github.kiryu1223.drink.nnnn.expression.SqlSingleValueExpression;
import io.github.kiryu1223.drink.exception.DrinkException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SqliteDateTimeDiffExtension extends BaseSqlExtension
{
    @Override
    public ISqlExpression parse(IConfig config, Method sqlFunc, List<ISqlExpression> args)
    {
        List<String> templates = new ArrayList<>();
        List<ISqlExpression> sqlExpressions = new ArrayList<>();
        ISqlExpression unit = args.get(0);
        ISqlExpression from = args.get(1);
        ISqlExpression to = args.get(2);
        if (unit instanceof SqlSingleValueExpression)
        {
            SqlSingleValueExpression sqlSingleValueExpression = (SqlSingleValueExpression) unit;
            SqlTimeUnit timeUnit = (SqlTimeUnit) sqlSingleValueExpression.getValue();
            switch (timeUnit)
            {
                case YEAR:
                    templates.add("(STRFTIME('%Y',");
                    sqlExpressions.add(to);
                    templates.add(") - STRFTIME('%Y',");
                    sqlExpressions.add(from);
                    templates.add("))");
                    break;
                case MONTH:
                    templates.add("((STRFTIME('%Y',");
                    sqlExpressions.add(to);
                    templates.add(") - STRFTIME('%Y',");
                    sqlExpressions.add(from);
                    templates.add(")) * 12 + STRFTIME('%m',");
                    sqlExpressions.add(to);
                    templates.add(") - STRFTIME('%m',");
                    sqlExpressions.add(from);
                    templates.add("))");
                    break;
                case WEEK:
                    templates.add("((JULIANDAY(");
                    sqlExpressions.add(to);
                    templates.add(") - JULIANDAY(");
                    sqlExpressions.add(from);
                    templates.add(")) / 7)");
                    break;
                case DAY:
                    templates.add("(JULIANDAY(");
                    sqlExpressions.add(to);
                    templates.add(") - JULIANDAY(");
                    sqlExpressions.add(from);
                    templates.add("))");
                    break;
                case HOUR:
                    templates.add("((JULIANDAY(");
                    sqlExpressions.add(to);
                    templates.add(") - JULIANDAY(");
                    sqlExpressions.add(from);
                    templates.add(")) * 24)");
                    break;
                case MINUTE:
                    templates.add("((JULIANDAY(");
                    sqlExpressions.add(to);
                    templates.add(") - JULIANDAY(");
                    sqlExpressions.add(from);
                    templates.add(")) * 24 * 60)");
                    break;
                case SECOND:
                    templates.add("((JULIANDAY(");
                    sqlExpressions.add(to);
                    templates.add(") - JULIANDAY(");
                    sqlExpressions.add(from);
                    templates.add(")) * 24 * 60 * 60)");
                    break;
                case MILLISECOND:
                    templates.add("((JULIANDAY(");
                    sqlExpressions.add(to);
                    templates.add(") - JULIANDAY(");
                    sqlExpressions.add(from);
                    templates.add(")) * 24 * 60 * 60 * 1000)");
                    break;
                case MICROSECOND:
                    templates.add("((JULIANDAY(");
                    sqlExpressions.add(to);
                    templates.add(") - JULIANDAY(");
                    sqlExpressions.add(from);
                    templates.add(")) * 24 * 60 * 60 * 1000 * 1000)");
                    break;
            }
        }
        else
        {
            throw new DrinkException("SqlTimeUnit必须为可求值的");
        }
        return config.getSqlExpressionFactory().template(templates, sqlExpressions);
    }
}
