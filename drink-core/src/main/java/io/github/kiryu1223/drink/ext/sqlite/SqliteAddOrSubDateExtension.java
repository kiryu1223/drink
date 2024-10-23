package io.github.kiryu1223.drink.ext.sqlite;

import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.sqlext.BaseSqlExtension;
import io.github.kiryu1223.drink.nnnn.expression.SqlSingleValueExpression;
import io.github.kiryu1223.drink.exception.DrinkIntervalException;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SqliteAddOrSubDateExtension extends BaseSqlExtension
{
    @Override
    public ISqlExpression parse(IConfig config, Method sqlFunc, List<ISqlExpression> args)
    {
        List<String> templates = new ArrayList<>();
        List<ISqlExpression> sqlExpressions = new ArrayList<>();
        boolean isPlus = sqlFunc.getName().equals("addDate");
        if (sqlFunc.getParameterTypes()[0] == LocalDate.class)
        {
            templates.add("DATE(");
        }
        else
        {
            templates.add("DATETIME(");
        }
        sqlExpressions.add(args.get(0));
        if (sqlFunc.getParameterCount() == 2)
        {
            ISqlExpression num = args.get(1);
            if (num instanceof SqlSingleValueExpression)
            {
                SqlSingleValueExpression valueExpression = (SqlSingleValueExpression) num;
                templates.add(",'" + (isPlus ? "" : "-") + valueExpression.getValue() + " DAY')");
            }
            else
            {
                throw new DrinkIntervalException(DbType.SQLite);
            }
        }
        else
        {
            ISqlExpression num = args.get(2);
            if (num instanceof SqlSingleValueExpression)
            {
                SqlSingleValueExpression valueExpression = (SqlSingleValueExpression) num;
                templates.add(",'" + (isPlus ? "" : "-") + valueExpression.getValue()+" ");
                sqlExpressions.add(args.get(1));
                templates.add("')");
            }
            else
            {
                throw new DrinkIntervalException(DbType.SQLite);
            }
        }
        return config.getSqlExpressionFactory().template(templates, sqlExpressions);
    }
}
