package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

public interface ISqlDeleteExpression extends ISqlExpression {
    ISqlFromExpression getFrom();

    ISqlJoinsExpression getJoins();

    ISqlWhereExpression getWhere();

    void addJoin(ISqlJoinExpression join);

    void addWhere(ISqlExpression where);

    default void setWhere(ISqlConditionsExpression conditions) {
        getWhere().setConditions(conditions);
    }

    @Override
    default ISqlDeleteExpression copy(IConfig config) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.delete(getFrom().copy(config), getJoins().copy(config), getWhere().copy(config));
    }
}
