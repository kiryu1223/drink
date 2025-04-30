package io.github.kiryu1223.drink.base.transform.expression;

import io.github.kiryu1223.drink.base.expression.ISqlExpression;

public interface ILogic {
    /**
     * if表达式
     */
    ISqlExpression If(ISqlExpression cond, ISqlExpression truePart, ISqlExpression falsePart);

    /**
     * 类型转换
     */
    ISqlExpression typeCast(ISqlExpression value, ISqlExpression type);
}
