package io.github.kiryu1223.drink.core.expression.mysql;


import io.github.kiryu1223.drink.base.expression.ISqlQueryableExpression;
import io.github.kiryu1223.drink.base.expression.impl.SqlRecursionExpression;

public class MySqlRecursionExpression extends SqlRecursionExpression {
    public MySqlRecursionExpression(ISqlQueryableExpression queryable, String parentId, String childId, int level) {
        super(queryable, parentId, childId, level);
    }

    @Override
    public String recursionKeyword() {
        return "RECURSIVE";
    }
}
