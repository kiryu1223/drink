package io.github.kiryu1223.drink.core.visitor.methods;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.nnnn.expression.SqlTemplateExpression;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MathMethods
{

    public static SqlTemplateExpression atan2(Config config, SqlExpression arg1, SqlExpression arg2)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        switch (config.getDbType())
        {
            case SQLServer:
                function = Arrays.asList("ATN2(", ",", ")");
                break;
            default:
                function = Arrays.asList("ATAN2(", ",", ")");
        }
        return factory.template(function, Arrays.asList(arg1, arg2));
    }

    public static SqlTemplateExpression ceil(Config config, SqlExpression arg)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        switch (config.getDbType())
        {
            case SQLServer:
                function = Arrays.asList("CEILING(", ")");
                break;
            default:
                function = Arrays.asList("CEIL(", ")");
        }
        return factory.template(function, Collections.singletonList(arg));
    }

    public static SqlTemplateExpression toDegrees(Config config, SqlExpression arg)
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
        return factory.template(function, Collections.singletonList(arg));
    }

    public static SqlTemplateExpression toRadians(Config config, SqlExpression arg)
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
        return factory.template(function, Collections.singletonList(arg));
    }

    public static SqlTemplateExpression log(Config config, SqlExpression arg)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        switch (config.getDbType())
        {
            case SQLServer:
                function = Arrays.asList("LOG(", ")");
                break;
            default:
                function = Arrays.asList("LN(", ")");
        }
        return factory.template(function, Collections.singletonList(arg));
    }

    public static SqlTemplateExpression log10(Config config, SqlExpression arg)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        List<SqlExpression> sqlExpressions;
        switch (config.getDbType())
        {
            case SQLServer:
                function = Arrays.asList("LOG(", ",10)");
                break;
            case Oracle:
                function = Arrays.asList("LOG(10,", ")");
                break;
            default:
                function = Arrays.asList("LOG10(", ")");
        }
        return factory.template(function, Collections.singletonList(arg));
    }

    public static SqlTemplateExpression random(Config config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        List<SqlExpression> sqlExpressions;
        switch (config.getDbType())
        {
            case Oracle:
                function = Collections.singletonList("DBMS_RANDOM.VALUE");
                break;
            case SQLite:
                function = Collections.singletonList("ABS(RANDOM() / 10000000000000000000.0)");
                break;
            case PostgreSQL:
                function = Collections.singletonList("RANDOM()");
                break;
            default:
                function = Collections.singletonList("RAND()");
        }
        return factory.template(function, Collections.emptyList());
    }

    public static SqlTemplateExpression round(Config config, SqlExpression arg)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        List<SqlExpression> sqlExpressions;
        switch (config.getDbType())
        {
            case SQLServer:
                function = Arrays.asList("ROUND(", ",0)");
                break;
            default:
                function = Arrays.asList("ROUND(", ")");
        }
        return factory.template(function, Collections.singletonList(arg));
    }
}
