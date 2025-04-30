package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

public interface ISqlUnionExpression extends ISqlExpression {
    ISqlQueryableExpression getQueryable();
    boolean isAll();

    @Override
    default ISqlUnionExpression copy(IConfig config) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.union(getQueryable().copy(config),isAll());
    }
}
