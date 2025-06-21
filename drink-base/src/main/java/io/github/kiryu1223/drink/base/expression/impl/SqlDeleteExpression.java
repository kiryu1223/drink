package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.ArrayList;
import java.util.List;

public class SqlDeleteExpression implements ISqlDeleteExpression {
    private final ISqlFromExpression from;
    private final ISqlJoinsExpression joins;
    private final ISqlWhereExpression where;

    protected SqlDeleteExpression(ISqlFromExpression from, ISqlJoinsExpression joins, ISqlWhereExpression where) {
        this.from = from;
        this.joins = joins;
        this.where = where;
    }

    @Override
    public ISqlFromExpression getFrom() {
        return from;
    }

    @Override
    public ISqlJoinsExpression getJoins() {
        return joins;
    }

    @Override
    public ISqlWhereExpression getWhere() {
        return where;
    }

    @Override
    public void addJoin(ISqlJoinExpression join) {
        joins.addJoin(join);
    }

    @Override
    public void addWhere(ISqlExpression where) {
        this.where.addCondition(where);
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        IDialect disambiguation = config.getDisambiguation();
        List<String> strings = new ArrayList<>();
        strings.add("DELETE");
        strings.add(from.getTableRefExpression().getSqlAndValue(config,values));
        strings.add(from.getSqlAndValue(config, values));
        if (!joins.isEmpty()) {
            strings.add(joins.getSqlAndValue(config, values));
        }
        if (!where.isEmpty()) {
            strings.add(where.getSqlAndValue(config, values));
        }
        return String.join(" ", strings);
    }
}
