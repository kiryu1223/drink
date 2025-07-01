package io.github.kiryu1223.drink.base.transform;

import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.expression.SqlOperator;

import java.util.Arrays;
import java.util.List;

public interface INumberMethods {

    SqlExpressionFactory getSqlExpressionFactory();
    /**
     * 取模运算
     */
    default ISqlExpression remainder(ISqlExpression left, ISqlExpression right) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        return factory.binary(SqlOperator.MOD, left, right);
    }
}
