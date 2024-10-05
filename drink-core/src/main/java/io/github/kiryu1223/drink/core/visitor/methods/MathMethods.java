package io.github.kiryu1223.drink.core.visitor.methods;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.core.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.core.expression.SqlFunctionExpression;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MathMethods
{
//    {
//        Math.class.getMethod("", double.class);
//    }

    public static SqlFunctionExpression atan2(Config config, SqlExpression arg1, SqlExpression arg2)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        switch (config.getDbType())
        {
            case SqlServer:
                function = Arrays.asList("ATN2(", ",", ")");
                break;
            default:
                function = Arrays.asList("ATAN2(", ",", ")");
        }
        return factory.function(function, Arrays.asList(arg1, arg2));
    }

    public static SqlFunctionExpression ceil(Config config, SqlExpression arg)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        switch (config.getDbType())
        {
            case SqlServer:
                function = Arrays.asList("CEILING(", ")");
                break;
            default:
                function = Arrays.asList("CEIL(", ")");
        }
        return factory.function(function, Collections.singletonList(arg));
    }

    public static SqlFunctionExpression toDegrees(Config config, SqlExpression arg)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        String s = "({a} * 180 / " + Math.PI + ")";
        switch (config.getDbType())
        {
            case Oracle:
                function = Arrays.asList("(", " * 180 / " + Math.PI + ")");
                break;
            default:
                function = Arrays.asList("DEGREES(", ")");
        }
        return factory.function(function, Collections.singletonList(arg));
    }

    public static SqlFunctionExpression toRadians(Config config, SqlExpression arg)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        switch (config.getDbType())
        {
            case Oracle:
                function = Arrays.asList("(", " * " + Math.PI + " / 180)");
                break;
            default:
                function = Arrays.asList("RADIANS(", ")");
        }
        return factory.function(function, Collections.singletonList(arg));
    }

    public static SqlFunctionExpression log(Config config, SqlExpression arg)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        switch (config.getDbType())
        {
            case SqlServer:
                function = Arrays.asList("LOG(", ")");
                break;
            default:
                function = Arrays.asList("LN(", ")");
        }
        return factory.function(function, Collections.singletonList(arg));
    }

    public static SqlFunctionExpression log10(Config config, SqlExpression arg)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        List<SqlExpression> sqlExpressions;
        switch (config.getDbType())
        {
            case SqlServer:
                function = Arrays.asList("LOG(", ",10)");
                break;
            case Oracle:
                function = Arrays.asList("LOG(10,", ")");
                break;
            default:
                function = Arrays.asList("LOG10(", ")");
        }
        return factory.function(function, Collections.singletonList(arg));
    }

    public static SqlFunctionExpression random(Config config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        List<SqlExpression> sqlExpressions;
        switch (config.getDbType())
        {
            case Oracle:
                function = Collections.singletonList("DBMS_RANDOM.VALUE");
                break;
            default:
                function = Collections.singletonList("RAND()");
        }
        return factory.function(function, Collections.emptyList());
    }

    public static SqlFunctionExpression round(Config config, SqlExpression arg)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        List<SqlExpression> sqlExpressions;
        switch (config.getDbType())
        {
            case SqlServer:
                function = Arrays.asList("ROUND(", ",0)");
                break;
            default:
                function = Arrays.asList("ROUND(", ")");
        }
        return factory.function(function, Collections.singletonList(arg));
    }
}
