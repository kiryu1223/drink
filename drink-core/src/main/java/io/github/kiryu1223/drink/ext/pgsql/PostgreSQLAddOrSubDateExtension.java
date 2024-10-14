package io.github.kiryu1223.drink.ext.pgsql;

import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.core.expression.SqlSingleValueExpression;
import io.github.kiryu1223.drink.exception.DrinkIntervalException;
import io.github.kiryu1223.drink.ext.BaseSqlExtension;
import io.github.kiryu1223.drink.ext.DbType;
import io.github.kiryu1223.drink.ext.FunctionBox;

import java.lang.reflect.Method;
import java.util.List;

public class PostgreSQLAddOrSubDateExtension extends BaseSqlExtension
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
        if (isPlus)
        {
            functions.add("DATE_ADD(");
        }
        else
        {
            functions.add("DATE_SUBTRACT(");
        }
        sqlExpressions.add(args.get(0));
        if (sqlFunc.getParameterCount() == 2)
        {
            SqlExpression num = args.get(1);
            if (num instanceof SqlSingleValueExpression)
            {
                SqlSingleValueExpression valueExpression = (SqlSingleValueExpression) num;
                functions.add(",INTERVAL '" + valueExpression.getValue() + "' DAY)");
            }
            else
            {
                throw new DrinkIntervalException(DbType.PostgreSQL);
            }
        }
        else
        {
            SqlExpression num = args.get(2);
            if (num instanceof SqlSingleValueExpression)
            {
                SqlSingleValueExpression valueExpression = (SqlSingleValueExpression) num;
                functions.add(",INTERVAL '" + valueExpression.getValue() + "' ");
                sqlExpressions.add(args.get(1));
                functions.add(")");
            }
            else
            {
                throw new DrinkIntervalException(DbType.PostgreSQL);
            }
        }
        return box;
    }
}
