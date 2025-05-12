package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.ArrayList;
import java.util.List;

public interface ISqlUnionQueryableExpression extends ISqlTableExpression {
    List<ISqlQueryableExpression> getQueryable();

    List<Boolean> getUnions();

    @Override
    default Class<?> getMainTableClass() {
        return getQueryable().get(0).getMainTableClass();
    }

    default void addQueryable(ISqlQueryableExpression queryable, boolean union) {
        getQueryable().add(queryable);
        getUnions().add(union);
    }

    default ISqlUnionQueryableExpression copy(IConfig config) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<ISqlQueryableExpression> copyQuery = new ArrayList<>(getQueryable().size());
        List<Boolean> copyUnions = new ArrayList<>(getUnions().size());
        for (ISqlQueryableExpression iSqlQueryableExpression : getQueryable()) {
            copyQuery.add(iSqlQueryableExpression.copy(config));
        }
        copyUnions.addAll(getUnions());
        return factory.unionQueryable(copyQuery, copyUnions);
    }
}
