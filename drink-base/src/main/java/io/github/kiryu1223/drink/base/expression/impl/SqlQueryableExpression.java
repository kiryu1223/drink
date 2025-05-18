/*
 * Copyright 2017-2024 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kiryu1223
 * @since 3.0
 */
public class SqlQueryableExpression implements ISqlQueryableExpression {

    protected final ISqlSelectExpression select;
    protected final ISqlFromExpression from;
    protected final ISqlJoinsExpression joins;
    protected final ISqlWhereExpression where;
    protected final ISqlGroupByExpression groupBy;
    protected final ISqlHavingExpression having;
    protected final ISqlOrderByExpression orderBy;
    protected final ISqlLimitExpression limit;

    protected SqlQueryableExpression(ISqlSelectExpression select, ISqlFromExpression from, ISqlJoinsExpression joins, ISqlWhereExpression where, ISqlGroupByExpression groupBy, ISqlHavingExpression having, ISqlOrderByExpression orderBy, ISqlLimitExpression limit) {
        this.select = select;
        this.from = from;
        this.joins = joins;
        this.where = where;
        this.groupBy = groupBy;
        this.having = having;
        this.orderBy = orderBy;
        this.limit = limit;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {

        List<String> strings = new ArrayList<>();
        tryWith(config, strings, values);
        strings.add(getSelect().getSqlAndValue(config, values));
        String fromSqlAndValue = getFrom().getSqlAndValue(config, values);
        if (!fromSqlAndValue.isEmpty()) strings.add(fromSqlAndValue);
        String joinsSqlAndValue = getJoins().getSqlAndValue(config, values);
        if (!joinsSqlAndValue.isEmpty()) strings.add(joinsSqlAndValue);
        String whereSqlAndValue = getWhere().getSqlAndValue(config, values);
        if (!whereSqlAndValue.isEmpty()) strings.add(whereSqlAndValue);
        String groupBySqlAndValue = getGroupBy().getSqlAndValue(config, values);
        if (!groupBySqlAndValue.isEmpty()) strings.add(groupBySqlAndValue);
        String havingSqlAndValue = getHaving().getSqlAndValue(config, values);
        if (!havingSqlAndValue.isEmpty()) strings.add(havingSqlAndValue);
        String orderBySqlAndValue = getOrderBy().getSqlAndValue(config, values);
        if (!orderBySqlAndValue.isEmpty()) strings.add(orderBySqlAndValue);
        if (!getFrom().isEmptyTable(config)) {
            String limitSqlAndValue = getLimit().getSqlAndValue(config, values);
            if (!limitSqlAndValue.isEmpty()) strings.add(limitSqlAndValue);
        }
        return String.join(" ", strings);

    }


    @Override
    public Class<?> getMainTableClass() {
        return select.getTarget();
    }

    @Override
    public void addWhere(ISqlExpression cond) {
        where.addCondition(cond);
    }

    @Override
    public void setWhere(ISqlConditionsExpression conditions) {
        where.setConditions(conditions);
    }

    @Override
    public void addJoin(ISqlJoinExpression join) {
        joins.addJoin(join);
    }

    @Override
    public void setGroup(ISqlGroupByExpression group) {
        groupBy.setColumns(group.getColumns());
    }

    @Override
    public void addHaving(ISqlExpression cond) {
        having.addCond(cond);
    }

    @Override
    public void addOrder(ISqlOrderExpression order) {
        orderBy.addOrder(order);
    }

    @Override
    public void setSelect(ISqlSelectExpression newSelect) {
        select.setColumns(newSelect.getColumns());
        select.setTarget(newSelect.getTarget());
        select.setSingle(newSelect.isSingle());
        select.setDistinct(newSelect.isDistinct());

    }

    @Override
    public void setLimit(long offset, long rows) {
        limit.setOffset(offset);
        limit.setRows(rows);
    }

    @Override
    public void setDistinct(boolean distinct) {
        select.setDistinct(distinct);
    }

    @Override
    public ISqlFromExpression getFrom() {
        return from;
    }

    @Override
    public int getOrderedCount() {
        return 1 + joins.getJoins().size();
    }

    @Override
    public ISqlWhereExpression getWhere() {
        return where;
    }

    @Override
    public ISqlGroupByExpression getGroupBy() {
        return groupBy;
    }

    @Override
    public ISqlJoinsExpression getJoins() {
        return joins;
    }

    @Override
    public ISqlSelectExpression getSelect() {
        return select;
    }

    @Override
    public ISqlOrderByExpression getOrderBy() {
        return orderBy;
    }

    @Override
    public ISqlLimitExpression getLimit() {
        return limit;
    }

    @Override
    public ISqlHavingExpression getHaving() {
        return having;
    }

//    public List<Class<?>> getOrderedClass() {
//        Class<?> tableClass = getMainTableClass();
//        List<Class<?>> collect = joins.getJoins().stream().map(j -> j.getJoinTable().getMainTableClass()).collect(Collectors.toList());
//        collect.add(0,tableClass);
//        return collect;
//    }

    protected void tryWith(IConfig config, List<String> strings, List<SqlValue> values) {
        ISqlTableExpression fromSqlTableExpression = from.getSqlTableExpression();
        List<String> withs = new ArrayList<>(joins.getJoins().size() + 1);
        if (fromSqlTableExpression instanceof ISqlWithExpression) {
            withs.add(fromSqlTableExpression.getSqlAndValue(config, values));
        }
        for (ISqlJoinExpression join : joins.getJoins()) {
            ISqlTableExpression joinTable = join.getJoinTable();
            if (joinTable instanceof ISqlWithExpression) {
                withs.add(joinTable.getSqlAndValue(config, values));
            }
        }
        if (!withs.isEmpty()) {
            strings.add("WITH " + String.join(",", withs));
        }
    }
}
