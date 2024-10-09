package io.github.kiryu1223.drink.ext.sqlite;

import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.core.expression.SqlSingleValueExpression;
import io.github.kiryu1223.drink.exception.DrinkOracleIntervalException;
import io.github.kiryu1223.drink.ext.BaseSqlExtension;
import io.github.kiryu1223.drink.ext.FunctionBox;
import io.github.kiryu1223.drink.ext.mysql.MySqlDateTimeDiffExtension;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

public class SqliteAddOrSubDateExtension extends BaseSqlExtension
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
        boolean isPlus = sqlFunc.getName().equals("addDate");
        if (sqlFunc.getParameterTypes()[0] == LocalDate.class)
        {
            functions.add("DATE(");
        }
        else
        {
            functions.add("DATETIME(");
        }
        sqlExpressions.add(args.get(0));
        if (sqlFunc.getParameterCount() == 2)
        {
            SqlExpression num = args.get(1);
            if (num instanceof SqlSingleValueExpression)
            {
                SqlSingleValueExpression valueExpression = (SqlSingleValueExpression) num;
                functions.add(",'" + (isPlus ? "" : "-") + valueExpression.getValue() + " DAY')");
            }
            else
            {
                throw new DrinkOracleIntervalException();
            }
        }
        else
        {
            SqlExpression num = args.get(2);
            if (num instanceof SqlSingleValueExpression)
            {
                SqlSingleValueExpression valueExpression = (SqlSingleValueExpression) num;
                functions.add(",'" + (isPlus ? "" : "-") + valueExpression.getValue()+" ");
                sqlExpressions.add(args.get(1));
                functions.add("')");
            }
            else
            {
                throw new DrinkOracleIntervalException();
            }
        }
        return box;
    }
}
