package io.github.kiryu1223.drink.core.visitor.methods;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;

import java.util.Arrays;
import java.util.List;

public class BigDecimalMethods
{
    public static ISqlExpression remainder(IConfig config, ISqlExpression left, ISqlExpression right)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        List<ISqlExpression> sqlExpressions;
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
