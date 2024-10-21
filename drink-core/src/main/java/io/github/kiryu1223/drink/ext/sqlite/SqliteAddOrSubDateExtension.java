package io.github.kiryu1223.drink.ext.sqlite;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.core.expression.SqlSingleValueExpression;
import io.github.kiryu1223.drink.exception.DrinkIntervalException;
import io.github.kiryu1223.drink.ext.BaseSqlExtension;
import io.github.kiryu1223.drink.ext.DbType;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SqliteAddOrSubDateExtension extends BaseSqlExtension
{
    @Override
    public SqlExpression parse(Config config, Method sqlFunc, List<SqlExpression> args)
    {
        List<String> templates = new ArrayList<>();
        List<SqlExpression> sqlExpressions = new ArrayList<>();
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
            SqlExpression num = args.get(1);
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
            SqlExpression num = args.get(2);
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
