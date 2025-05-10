package io.github.kiryu1223.drink.base.transform;

import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.expression.SqlOperator;

public interface ITimeMethods {

    SqlExpressionFactory getSqlExpressionFactory();
    /**
     * 左时间大于右时间表达式
     */
    default ISqlExpression isAfter(ISqlExpression thiz, ISqlExpression that) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        return factory.binary(SqlOperator.GT, thiz, that);
    }

    /**
     * 左时间小于右时间表达式
     */
    default ISqlExpression isBefore(ISqlExpression thiz, ISqlExpression that) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        return factory.binary(SqlOperator.LT, thiz, that);
    }

    /**
     * 左时间等于右时间表达式
     */
    default ISqlExpression isEqual(ISqlExpression thiz, ISqlExpression that) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        return factory.binary(SqlOperator.EQ, thiz, that);
    }
}
