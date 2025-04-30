package io.github.kiryu1223.drink.base.transform.method;

import io.github.kiryu1223.drink.base.expression.ISqlExpression;

public interface ITimeMethods {
    /**
     * 左时间大于右时间表达式
     */
    ISqlExpression isAfter(ISqlExpression thiz, ISqlExpression that);

    /**
     * 左时间小于右时间表达式
     */
    ISqlExpression isBefore(ISqlExpression thiz, ISqlExpression that);

    /**
     * 左时间等于右时间表达式
     */
    ISqlExpression isEqual(ISqlExpression thiz, ISqlExpression that);
}
