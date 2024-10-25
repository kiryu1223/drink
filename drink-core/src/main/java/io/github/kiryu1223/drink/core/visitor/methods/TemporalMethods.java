package io.github.kiryu1223.drink.core.visitor.methods;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.expression.SqlOperator;

public class TemporalMethods
{
    public static ISqlExpression isAfter(IConfig config, ISqlExpression thiz, ISqlExpression that)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.binary(SqlOperator.GT, thiz, that);
//        switch (config.getDbType())
//        {
//            case SQLServer:
//                return factory.template(Arrays.asList("IIF((DATEDIFF_BIG(SECOND,", ",", ") <= 0),1,0)"), Arrays.asList(thiz, that));
//            default:
//                return factory.binary(SqlOperator.GT, thiz, that);
//        }
    }

    public static ISqlExpression isBefore(IConfig config, ISqlExpression thiz, ISqlExpression that)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.binary(SqlOperator.LT, thiz, that);
//        switch (config.getDbType())
//        {
//            case SQLServer:
//                return factory.template(Arrays.asList("IIF((DATEDIFF_BIG(SECOND,", ",", ") >= 0),1,0)"), Arrays.asList(thiz, that));
//            default:
//                return factory.binary(SqlOperator.LT, thiz, that);
//        }
    }

    public static ISqlExpression isEqual(IConfig config, ISqlExpression thiz, ISqlExpression that)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.binary(SqlOperator.EQ, thiz, that);
//        switch (config.getDbType())
//        {
//            case SQLServer:
//                return factory.template(Arrays.asList("IIF((DATEDIFF_BIG(SECOND,", ",", ") = 0),1,0)"), Arrays.asList(thiz, that));
//            default:
//                return factory.binary(SqlOperator.EQ, thiz, that);
//        }
    }
}
