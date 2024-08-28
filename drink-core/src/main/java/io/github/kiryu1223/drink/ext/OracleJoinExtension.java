package io.github.kiryu1223.drink.ext;

import io.github.kiryu1223.drink.core.context.SqlContext;

import java.lang.reflect.Method;
import java.util.List;

public class OracleJoinExtension extends BaseSqlExtension
{
    @Override
    public FunctionBox parse(Method sqlFunc, List<SqlContext> args)
    {
        FunctionBox functionBox = new FunctionBox();
        List<String> functions = functionBox.getFunctions();
        List<SqlContext> sqlContexts = functionBox.getSqlContexts();
        SqlContext separator = args.get(0);
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
