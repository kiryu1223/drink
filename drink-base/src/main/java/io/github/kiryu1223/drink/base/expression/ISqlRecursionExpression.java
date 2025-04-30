package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

public interface ISqlRecursionExpression extends ISqlWithExpression {
    ISqlQueryableExpression getQueryable();

    String recursionKeyword();

    String withTableName();

    String parentId();

    String childId();

    int level();

    @Override
    default ISqlRecursionExpression copy(IConfig config) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.recursion(getQueryable(),parentId(),childId(),level());
    }
}
