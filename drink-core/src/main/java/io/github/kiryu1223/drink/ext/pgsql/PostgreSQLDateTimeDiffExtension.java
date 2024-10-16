package io.github.kiryu1223.drink.ext.pgsql;

import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.core.expression.SqlSingleValueExpression;
import io.github.kiryu1223.drink.exception.DrinkException;
import io.github.kiryu1223.drink.ext.BaseSqlExtension;
import io.github.kiryu1223.drink.ext.FunctionBox;
import io.github.kiryu1223.drink.ext.SqlTimeUnit;

import java.lang.reflect.Method;
import java.util.List;

public class PostgreSQLDateTimeDiffExtension extends BaseSqlExtension
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
        Class<?>[] parameterTypes = sqlFunc.getParameterTypes();
        boolean isToIsString = parameterTypes[2] == String.class;
        boolean isFromIsString = parameterTypes[1] == String.class;
        String toString = isToIsString ? "::TIMESTAMP" : "";
        String fromString = isFromIsString ? "::TIMESTAMP" : "";
        if (unit instanceof SqlSingleValueExpression)
        {
            SqlSingleValueExpression sqlSingleValueExpression = (SqlSingleValueExpression) unit;
            SqlTimeUnit timeUnit = (SqlTimeUnit) sqlSingleValueExpression.getValue();
            switch (timeUnit)
            {
                case YEAR:
                    functions.add("EXTRACT(YEAR FROM AGE(");
                    sqlExpressions.add(to);
                    functions.add(toString + ",");
                    sqlExpressions.add(from);
                    functions.add(fromString + "))::INT8");
                    break;
                case MONTH:
                    functions.add("(EXTRACT(YEAR FROM AGE(");
                    sqlExpressions.add(to);
                    functions.add(toString + ",");
                    sqlExpressions.add(from);
                    functions.add(fromString + ")) * 12 + EXTRACT(MONTH FROM AGE(");
                    sqlExpressions.add(to);
                    functions.add(toString + ",");
                    sqlExpressions.add(from);
                    functions.add(fromString + ")))::INT8");
                    break;
                case WEEK:
                    functions.add("(EXTRACT(DAY FROM (");
                    sqlExpressions.add(to);
                    functions.add(toString + " - ");
                    sqlExpressions.add(from);
                    functions.add(fromString + ")) / 7)::INT8");
                    break;
                case DAY:
                    functions.add("EXTRACT(DAY FROM (");
                    sqlExpressions.add(to);
                    functions.add(toString + " - ");
                    sqlExpressions.add(from);
                    functions.add(fromString + "))::INT8");
                    break;
                case HOUR:
                    functions.add("(EXTRACT(EPOCH FROM ");
                    sqlExpressions.add(to);
                    functions.add(toString + " - ");
                    sqlExpressions.add(from);
                    functions.add(fromString + ") / 3600)::INT8");
                    break;
                case MINUTE:
                    functions.add("(EXTRACT(EPOCH FROM ");
                    sqlExpressions.add(to);
                    functions.add(toString + " - ");
                    sqlExpressions.add(from);
                    functions.add(fromString + ") / 60)::INT8");
                    break;
                case SECOND:
                    functions.add("EXTRACT(EPOCH FROM ");
                    sqlExpressions.add(to);
                    functions.add(toString + " - ");
                    sqlExpressions.add(from);
                    functions.add(fromString + ")::INT8");
                    break;
                case MILLISECOND:
                    functions.add("(EXTRACT(EPOCH FROM ");
                    sqlExpressions.add(to);
                    functions.add(toString + " - ");
                    sqlExpressions.add(from);
                    functions.add(fromString + ") * 1000)::INT8");
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
