package io.github.kiryu1223.drink.ext.pgsql;

import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.sqlext.BaseSqlExtension;
import io.github.kiryu1223.drink.nnnn.expression.SqlSingleValueExpression;
import io.github.kiryu1223.drink.exception.DrinkIntervalException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLAddOrSubDateExtension extends BaseSqlExtension
{
    @Override
    public ISqlExpression parse(IConfig config, Method sqlFunc, List<ISqlExpression> args)
    {
        List<String> templates = new ArrayList<>();
        List<ISqlExpression> sqlExpressions = new ArrayList<>();
        boolean isPlus = sqlFunc.getName().equals("addDate");
        if (isPlus)
        {
            templates.add("DATE_ADD(");
        }
        else
        {
            templates.add("DATE_SUBTRACT(");
        }
        sqlExpressions.add(args.get(0));
        if (sqlFunc.getParameterCount() == 2)
        {
            ISqlExpression num = args.get(1);
            if (num instanceof SqlSingleValueExpression)
            {
                SqlSingleValueExpression valueExpression = (SqlSingleValueExpression) num;
                templates.add(",INTERVAL '" + valueExpression.getValue() + "' DAY)");
            }
            else
            {
                throw new DrinkIntervalException(DbType.PostgreSQL);
            }
        }
        else
        {
            ISqlExpression num = args.get(2);
            if (num instanceof SqlSingleValueExpression)
            {
                SqlSingleValueExpression valueExpression = (SqlSingleValueExpression) num;
                templates.add(",INTERVAL '" + valueExpression.getValue() + "' ");
                sqlExpressions.add(args.get(1));
                templates.add(")");
            }
            else
            {
                throw new DrinkIntervalException(DbType.PostgreSQL);
            }
        }
        return config.getSqlExpressionFactory().template(templates, sqlExpressions);
    }
}
