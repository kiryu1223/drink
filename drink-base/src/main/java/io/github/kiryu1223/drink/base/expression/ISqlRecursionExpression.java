package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;

public interface ISqlRecursionExpression extends ISqlWithExpression {
    ISqlQueryableExpression getQueryable();

    String withTableName();

    FieldMetaData parentId();

    FieldMetaData childId();

    int level();

    @Override
    default ISqlRecursionExpression copy(IConfig config) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.recursion(getQueryable().copy(config),parentId(),childId(),level());
    }
}
