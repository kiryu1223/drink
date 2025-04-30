package io.github.kiryu1223.drink.base.transform.method;

import io.github.kiryu1223.drink.base.expression.ISqlExpression;

public interface IObjectsMethods {
    /**
     * 不为空
     */
    ISqlExpression notNull(ISqlExpression expression);
}
