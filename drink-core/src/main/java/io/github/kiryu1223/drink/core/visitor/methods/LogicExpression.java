package io.github.kiryu1223.drink.core.visitor.methods;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;

import java.util.Arrays;
import java.util.List;

public class LogicExpression
{
    public static ISqlExpression IfExpression(IConfig config, ISqlExpression cond, ISqlExpression truePart, ISqlExpression falsePart)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        List<ISqlExpression> args = Arrays.asList(cond, truePart, falsePart);
        switch (config.getDbType())
        {
            case SQLServer:
            case SQLite:
                function = Arrays.asList("IIF(", ",", ",", ")");
                break;
            case Oracle:
            case PostgreSQL:
                function = Arrays.asList("(CASE WHEN ", " THEN ", " ELSE ", " END)");
                break;
            default:
                function = Arrays.asList("IF(", ",", ",", ")");
        }
        return factory.template(function, args);
    }
}
