package io.github.kiryu1223.drink.base.transform.method;

import io.github.kiryu1223.drink.base.expression.ISqlExpression;

public interface INumberMethods {
    /**
     * 取模运算
     */
    ISqlExpression remainder(ISqlExpression left, ISqlExpression right);
}
