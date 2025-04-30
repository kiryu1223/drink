package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlQueryableExpression;
import io.github.kiryu1223.drink.base.expression.ISqlUnionExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.List;

public class SqlUnionExpression implements ISqlUnionExpression {
    protected final ISqlQueryableExpression queryable;
    protected final boolean all;

    public SqlUnionExpression(ISqlQueryableExpression queryable, boolean all) {
        this.queryable = queryable;
        this.all = all;
    }

    @Override
    public ISqlQueryableExpression getQueryable() {
        return queryable;
    }

    @Override
    public boolean isAll() {
        return all;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        String union = "UNION" + (all ? " ALL " : " ");
        return union + "(" + queryable.getSqlAndValue(config, values) + ")";
    }
}
