package io.github.kiryu1223.drink.base.transform.method.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.expression.SqlOperator;
import io.github.kiryu1223.drink.base.transform.Transform;
import io.github.kiryu1223.drink.base.transform.method.IObjectsMethods;

public class ObjectsMethods extends Transform implements IObjectsMethods {

    public ObjectsMethods(IConfig config) {
        super(config);
    }

    /**
     * 是否为空表达式
     */
    public ISqlExpression notNull(ISqlExpression expression) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.binary(SqlOperator.IS, expression, factory.unary(SqlOperator.NOT,factory.AnyValue(null)));
    }
}
