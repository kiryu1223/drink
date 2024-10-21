package io.github.kiryu1223.drink.core.visitor.methods;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.core.expression.SqlExpressionFactory;

import java.util.Arrays;
import java.util.List;

public class BigDecimalMethods
{
    public static SqlExpression remainder(Config config, SqlExpression left, SqlExpression right)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        List<SqlExpression> sqlExpressions;
        switch (config.getDbType())
        {
            case Oracle:
                function = Arrays.asList("MOD(", ",", ")");
                break;
            default:
                function = Arrays.asList("(", " % ", ")");
        }
        return factory.template(function, Arrays.asList(left, right));
    }
}
