package io.github.kiryu1223.drink.ext;

import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.core.expression.SqlTypeExpression;

import java.lang.reflect.Method;
import java.util.List;

public class OracleCastExtension extends BaseSqlExtension
{
    @Override
    public FunctionBox parse(Method sqlFunc, List<SqlExpression> args)
    {
        FunctionBox box = new FunctionBox();
        List<String> functions = box.getFunctions();
        List<SqlExpression> sqlExpressions = box.getSqlExpressions();
        SqlExpression expression = args.get(1);
        if (expression instanceof SqlTypeExpression)
        {
            SqlTypeExpression typeExpression = (SqlTypeExpression) expression;
            Class<?> type = typeExpression.getType();
            if (type == String.class)
            {
                functions.add("TO_CHAR(");
                functions.add(")");
                sqlExpressions.add(args.get(0));
                return box;
            }
            else if (type == char.class || type == Character.class)
            {
                functions.add("SUBSTR(TO_CHAR(");
                functions.add("),1,1)");
                sqlExpressions.add(args.get(0));
                return box;
            }
        }
        functions.add("CAST(");
        functions.add(" AS ");
        functions.add(")");
        sqlExpressions.add(args.get(0));
        sqlExpressions.add(args.get(1));
        return box;
    }
}
