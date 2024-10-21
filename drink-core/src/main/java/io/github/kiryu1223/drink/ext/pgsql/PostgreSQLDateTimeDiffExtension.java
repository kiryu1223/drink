package io.github.kiryu1223.drink.ext.pgsql;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.core.expression.SqlSingleValueExpression;
import io.github.kiryu1223.drink.exception.DrinkException;
import io.github.kiryu1223.drink.ext.BaseSqlExtension;
import io.github.kiryu1223.drink.ext.SqlTimeUnit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLDateTimeDiffExtension extends BaseSqlExtension
{
    @Override
    public SqlExpression parse(Config config, Method sqlFunc, List<SqlExpression> args)
    {
        List<String> templates = new ArrayList<>();
        List<SqlExpression> sqlExpressions = new ArrayList<>();
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
                    templates.add("EXTRACT(YEAR FROM AGE(");
                    sqlExpressions.add(to);
                    templates.add(toString + ",");
                    sqlExpressions.add(from);
                    templates.add(fromString + "))::INT8");
                    break;
                case MONTH:
                    templates.add("(EXTRACT(YEAR FROM AGE(");
                    sqlExpressions.add(to);
                    templates.add(toString + ",");
                    sqlExpressions.add(from);
                    templates.add(fromString + ")) * 12 + EXTRACT(MONTH FROM AGE(");
                    sqlExpressions.add(to);
                    templates.add(toString + ",");
                    sqlExpressions.add(from);
                    templates.add(fromString + ")))::INT8");
                    break;
                case WEEK:
                    templates.add("(EXTRACT(DAY FROM (");
                    sqlExpressions.add(to);
                    templates.add(toString + " - ");
                    sqlExpressions.add(from);
                    templates.add(fromString + ")) / 7)::INT8");
                    break;
                case DAY:
                    templates.add("EXTRACT(DAY FROM (");
                    sqlExpressions.add(to);
                    templates.add(toString + " - ");
                    sqlExpressions.add(from);
                    templates.add(fromString + "))::INT8");
                    break;
                case HOUR:
                    templates.add("(EXTRACT(EPOCH FROM ");
                    sqlExpressions.add(to);
                    templates.add(toString + " - ");
                    sqlExpressions.add(from);
                    templates.add(fromString + ") / 3600)::INT8");
                    break;
                case MINUTE:
                    templates.add("(EXTRACT(EPOCH FROM ");
                    sqlExpressions.add(to);
                    templates.add(toString + " - ");
                    sqlExpressions.add(from);
                    templates.add(fromString + ") / 60)::INT8");
                    break;
                case SECOND:
                    templates.add("EXTRACT(EPOCH FROM ");
                    sqlExpressions.add(to);
                    templates.add(toString + " - ");
                    sqlExpressions.add(from);
                    templates.add(fromString + ")::INT8");
                    break;
                case MILLISECOND:
                    templates.add("(EXTRACT(EPOCH FROM ");
                    sqlExpressions.add(to);
                    templates.add(toString + " - ");
                    sqlExpressions.add(from);
                    templates.add(fromString + ") * 1000)::INT8");
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
