package io.github.kiryu1223.drink.ext.oracle;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.core.expression.SqlSingleValueExpression;
import io.github.kiryu1223.drink.core.expression.SqlTemplateExpression;
import io.github.kiryu1223.drink.exception.DrinkIntervalException;
import io.github.kiryu1223.drink.ext.BaseSqlExtension;
import io.github.kiryu1223.drink.ext.DbType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class OracleAddOrSubDateExtension extends BaseSqlExtension
{
    @Override
    public SqlExpression parse(Config config, Method sqlFunc, List<SqlExpression> args)
    {
        List<String> templates = new ArrayList<>();
        List<SqlExpression> sqlExpressions = new ArrayList<>();
        if (sqlFunc.getParameterCount() == 2)
        {
            templates.add("(");
            sqlExpressions.add(args.get(0));
            SqlExpression num = args.get(1);
            if (num instanceof SqlSingleValueExpression)
            {
                SqlSingleValueExpression valueExpression = (SqlSingleValueExpression) num;
                if (sqlFunc.getName().equals("addDate"))
                {
                    templates.add(" + INTERVAL '" + valueExpression.getValue() + "' DAY)");
                }
                else
                {
                    templates.add(" - INTERVAL '" + valueExpression.getValue() + "' DAY)");
                }
            }
            else
            {
                throw new DrinkIntervalException(DbType.Oracle);
            }
        }
        else
        {
            templates.add("(");
            sqlExpressions.add(args.get(0));
            sqlExpressions.add(args.get(1));
            SqlExpression num = args.get(2);
            if (num instanceof SqlSingleValueExpression)
            {
                SqlSingleValueExpression valueExpression = (SqlSingleValueExpression) num;
                if (sqlFunc.getName().equals("addDate"))
                {
                    templates.add(" + INTERVAL '" + valueExpression.getValue() + "' ");
                }
                else
                {
                    templates.add(" - INTERVAL '" + valueExpression.getValue() + "' ");
                }
                templates.add(")");
            }
            else
            {
                throw new DrinkIntervalException(DbType.Oracle);
            }
        }
        return config.getSqlExpressionFactory().template(templates, sqlExpressions);
    }
}
