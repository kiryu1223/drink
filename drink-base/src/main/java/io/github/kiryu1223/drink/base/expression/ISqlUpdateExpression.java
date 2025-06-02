package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

public interface ISqlUpdateExpression extends ISqlExpression {
    ISqlFromExpression getFrom();

    ISqlJoinsExpression getJoins();

    ISqlSetsExpression getSets();

    ISqlWhereExpression getWhere();

    void addJoin(ISqlJoinExpression join);

    void addSet(ISqlSetExpression set);

    void addWhere(ISqlExpression where);

    default void setWhere(ISqlConditionsExpression conditions) {
        getWhere().setConditions(conditions);
    }

    @Override
    default ISqlUpdateExpression copy(IConfig config) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.update(getFrom().copy(config), getJoins().copy(config), getSets().copy(config), getWhere().copy(config));
    }
}
