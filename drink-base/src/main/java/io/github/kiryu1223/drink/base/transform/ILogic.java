package io.github.kiryu1223.drink.base.transform;

import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;

import java.util.Arrays;
import java.util.List;

public interface ILogic {

    SqlExpressionFactory getSqlExpressionFactory();
    /**
     * if表达式
     */
    default ISqlExpression If(ISqlExpression cond, ISqlExpression truePart, ISqlExpression falsePart) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> function = Arrays.asList("IF(", ",", ",", ")");
        List<ISqlExpression> args = Arrays.asList(cond, truePart, falsePart);
        return factory.template(function, args);
    }

    /**
     * 类型转换
     */
    ISqlExpression typeCast(ISqlExpression value, Class<?> type);
}
