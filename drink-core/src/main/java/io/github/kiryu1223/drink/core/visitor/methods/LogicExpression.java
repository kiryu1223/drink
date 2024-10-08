package io.github.kiryu1223.drink.core.visitor.methods;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.core.expression.SqlExpressionFactory;

import java.util.Arrays;
import java.util.List;

public class LogicExpression
{
    public static SqlExpression IfExpression(Config config, SqlExpression cond, SqlExpression truePart, SqlExpression falsePart)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        List<SqlExpression> args = Arrays.asList(cond, truePart, falsePart);
        switch (config.getDbType())
        {
            case SqlServer:
                function = Arrays.asList("IIF(", ",", ",", ")");
                break;
            case Oracle:
                function = Arrays.asList("(CASE WHEN ", " THEN ", " ELSE ", " END)");
                break;
            default:
                function = Arrays.asList("IF(", ",", ",", ")");
        }
        return factory.function(function, args);
    }
}
