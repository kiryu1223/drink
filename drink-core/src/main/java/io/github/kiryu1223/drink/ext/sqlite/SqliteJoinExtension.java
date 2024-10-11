package io.github.kiryu1223.drink.ext.sqlite;

import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.ext.BaseSqlExtension;
import io.github.kiryu1223.drink.ext.FunctionBox;

import java.lang.reflect.Method;
import java.util.List;

public class SqliteJoinExtension extends BaseSqlExtension
{
//    static
//    {
//        new OracleJoinExtension();
//    }

    @Override
    public FunctionBox parse(Method sqlFunc, List<SqlExpression> args)
    {
        FunctionBox functionBox = new FunctionBox();
        List<String> functions = functionBox.getFunctions();
        List<SqlExpression> sqlContexts = functionBox.getSqlExpressions();
        SqlExpression separator = args.get(0);
        functions.add("(");
        for (int i = 1; i < args.size(); i++)
        {
            sqlContexts.add(args.get(i));
            if (i < args.size() - 1)
            {
                functions.add("||");
                sqlContexts.add(separator);
                functions.add("||");
            }
        }
        functions.add(")");
        return functionBox;
    }
}
