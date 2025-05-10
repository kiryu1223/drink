package io.github.kiryu1223.drink.db.mysql;


import io.github.kiryu1223.drink.base.expression.ISqlQueryableExpression;
import io.github.kiryu1223.drink.base.expression.impl.SqlRecursionExpression;

public class MySQLRecursionExpression extends SqlRecursionExpression {

    public MySQLRecursionExpression(ISqlQueryableExpression queryable, String parentId, String childId, int level)
    {
        super(queryable, parentId, childId, level);
    }

    @Override
    public String recursionKeyword() {
        return "RECURSIVE";
    }
}
