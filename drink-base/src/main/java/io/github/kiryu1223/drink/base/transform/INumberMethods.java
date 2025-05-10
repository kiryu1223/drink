package io.github.kiryu1223.drink.base.transform;

import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;

import java.util.Arrays;
import java.util.List;

public interface INumberMethods {

    SqlExpressionFactory getSqlExpressionFactory();
    /**
     * 取模运算
     */
    default ISqlExpression remainder(ISqlExpression left, ISqlExpression right) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> function = Arrays.asList("(", " % ", ")");
        return factory.template(function, Arrays.asList(left, right));
    }
}
