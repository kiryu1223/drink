package io.github.kiryu1223.drink.core.visitor.methods;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.core.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.core.expression.SqlOperator;

import java.util.Arrays;

public class TemporalMethods
{
    public static SqlExpression isAfter(Config config, SqlExpression thiz, SqlExpression that)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        switch (config.getDbType())
        {
            case SqlServer:
                return factory.function(Arrays.asList("IIF((DATEDIFF_BIG(SECOND,", ",", ") <= 0),1,0)"), Arrays.asList(thiz, that));
            default:
                return factory.binary(SqlOperator.GT, thiz, that);
        }
    }

    public static SqlExpression isBefore(Config config, SqlExpression thiz, SqlExpression that)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        switch (config.getDbType())
        {
            case SqlServer:
                return factory.function(Arrays.asList("IIF((DATEDIFF_BIG(SECOND,", ",", ") >= 0),1,0)"), Arrays.asList(thiz, that));
            default:
                return factory.binary(SqlOperator.LT, thiz, that);
        }
    }

    public static SqlExpression isEqual(Config config, SqlExpression thiz, SqlExpression that)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        switch (config.getDbType())
        {
            case SqlServer:
                return factory.function(Arrays.asList("IIF((DATEDIFF_BIG(SECOND,", ",", ") = 0),1,0)"), Arrays.asList(thiz, that));
            default:
                return factory.binary(SqlOperator.EQ, thiz, that);
        }
    }
}
