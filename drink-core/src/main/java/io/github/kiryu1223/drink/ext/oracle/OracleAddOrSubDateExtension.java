package io.github.kiryu1223.drink.ext.oracle;

import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.core.expression.SqlSingleValueExpression;
import io.github.kiryu1223.drink.exception.DrinkOracleIntervalException;
import io.github.kiryu1223.drink.ext.BaseSqlExtension;
import io.github.kiryu1223.drink.ext.FunctionBox;

import java.lang.reflect.Method;
import java.util.List;

public class OracleAddOrSubDateExtension extends BaseSqlExtension
{
    @Override
    public FunctionBox parse(Method sqlFunc, List<SqlExpression> args)
    {
        FunctionBox box = new FunctionBox();
        List<String> functions = box.getFunctions();
        List<SqlExpression> sqlExpressions = box.getSqlExpressions();
        if (sqlFunc.getParameterCount() == 2)
        {
            functions.add("(");
            sqlExpressions.add(args.get(0));
            SqlExpression num = args.get(1);
            if (num instanceof SqlSingleValueExpression)
            {
                SqlSingleValueExpression valueExpression = (SqlSingleValueExpression) num;
                if (sqlFunc.getName().equals("addDate"))
                {
                    functions.add(" + INTERVAL '" + valueExpression.getValue() + "' DAY)");
                }
                else
                {
                    functions.add(" - INTERVAL '" + valueExpression.getValue() + "' DAY)");
                }

            }
            else
            {
                throw new DrinkOracleIntervalException();
            }
        }
        else
        {
            functions.add("(");
            sqlExpressions.add(args.get(0));
            sqlExpressions.add(args.get(1));
            SqlExpression num = args.get(2);
            if (num instanceof SqlSingleValueExpression)
            {
                SqlSingleValueExpression valueExpression = (SqlSingleValueExpression) num;
                if (sqlFunc.getName().equals("addDate"))
                {
                    functions.add(" + INTERVAL '" + valueExpression.getValue() + "' ");
                }
                else
                {
                    functions.add(" - INTERVAL '" + valueExpression.getValue() + "' ");
                }
                functions.add(")");
            }
            else
            {
                throw new DrinkOracleIntervalException();
            }
        }
        return box;
    }
}
