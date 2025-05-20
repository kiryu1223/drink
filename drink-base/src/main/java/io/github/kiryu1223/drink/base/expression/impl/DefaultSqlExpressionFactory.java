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
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author kiryu1223
 * @since 3.0
 */
public class DefaultSqlExpressionFactory implements SqlExpressionFactory {

    private final IConfig config;

    protected DefaultSqlExpressionFactory(IConfig config)
    {
        this.config = config;
    }

    @Override
    public IConfig getConfig()
    {
        return config;
    }

    @Override
    public ISqlAsExpression as(ISqlExpression expression, String asName) {
        return new SqlAsExpression(expression, asName);
    }

    @Override
    public ISqlColumnExpression column(FieldMetaData fieldMetaData, ISqlTableRefExpression tableRefExpression) {
        return new SqlColumnExpression(fieldMetaData, tableRefExpression);
    }

    @Override
    public ISqlConditionsExpression condition(boolean and) {
        return new SqlConditionsExpression(and);
    }

    @Override
    public ISqlFromExpression from(ISqlTableExpression sqlTable, ISqlTableRefExpression tableRefExpression) {
        return new SqlFromExpression(sqlTable, tableRefExpression);
    }

    @Override
    public ISqlGroupByExpression groupBy(LinkedHashMap<String, ISqlExpression> columns) {
        return new SqlGroupByExpression(columns);
    }

    @Override
    public ISqlHavingExpression having() {
        return new SqlHavingExpression(condition());
    }

    @Override
    public ISqlJoinExpression join(JoinType joinType, ISqlTableExpression joinTable, ISqlConditionsExpression conditions, ISqlTableRefExpression tableRefExpression) {
        return new SqlJoinExpression(joinType, joinTable, conditions, tableRefExpression);
    }

    @Override
    public ISqlJoinsExpression Joins() {
        return new SqlJoinsExpression();
    }

    @Override
    public ISqlLimitExpression limit() {
        return new SqlLimitExpression();
    }

    @Override
    public ISqlOrderByExpression orderBy() {
        return new SqlOrderByExpression();
    }

    @Override
    public ISqlOrderExpression order(ISqlExpression expression, boolean asc) {
        return new SqlOrderExpression(expression, asc);
    }

    @Override
    public ISqlQueryableExpression queryable(ISqlSelectExpression select, ISqlFromExpression from, ISqlJoinsExpression joins, ISqlWhereExpression where, ISqlGroupByExpression groupBy, ISqlHavingExpression having, ISqlOrderByExpression orderBy, ISqlLimitExpression limit) {
        return new SqlQueryableExpression(select, from, joins, where, groupBy, having, orderBy, limit);
    }

    @Override
    public ISqlRealTableExpression table(Class<?> tableClass) {
        return new SqlRealTableExpression(tableClass);
    }

    @Override
    public ISqlSelectExpression select(List<ISqlExpression> column, Class<?> target, boolean isSingle, boolean isDistinct) {
        return new SqlSelectExpression(column, target, isSingle, isDistinct);
    }

    @Override
    public ISqlWhereExpression where(ISqlConditionsExpression conditions) {
        return new SqlWhereExpression(conditions);
    }

    @Override
    public ISqlSetExpression set(ISqlColumnExpression column, ISqlExpression value) {
        return new SqlSetExpression(column, value);
    }

    @Override
    public ISqlSingleValueExpression value(Object value) {
        return new SqlSingleValueExpression(value);
    }

    @Override
    public ISqlCollectedValueExpression value(Collection<?> value) {
        return new SqlCollectedValueExpression(value);
    }

    @Override
    public ISqlTemplateExpression template(List<String> templates, List<? extends ISqlExpression> expressions) {
        return new SqlTemplateExpression(templates, expressions);
    }

    @Override
    public ISqlBinaryExpression binary(SqlOperator operator, ISqlExpression left, ISqlExpression right) {
        return new SqlBinaryExpression(operator, left, right);
    }

    @Override
    public ISqlUnaryExpression unary(SqlOperator operator, ISqlExpression expression) {
        return new SqlUnaryExpression(operator, expression);
    }

    @Override
    public ISqlParensExpression parens(ISqlExpression expression) {
        return new SqlParensExpression(expression);
    }

    @Override
    public ISqlConstStringExpression constString(String s) {
        return new SqlConstStringExpression(s);
    }

    @Override
    public ISqlSetsExpression sets() {
        return new SqlSetsExpression();
    }

    @Override
    public ISqlTypeCastExpression typeCast(Class<?> c,ISqlExpression expression) {
        return new SqlTypeCastExpression(c,expression);
    }

    @Override
    public ISqlWithExpression with(ISqlQueryableExpression queryable, String name) {
        return new SqlWithExpression(queryable, name);
    }

    @Override
    public ISqlWithsExpression withs() {
        return new SqlWithsExpression();
    }

    @Override
    public ISqlUnionQueryableExpression unionQueryable(List<ISqlQueryableExpression> queryable, List<Boolean> unions) {
        return new SqlUnionQueryableExpression(queryable, unions);
    }

    @Override
    public ISqlRecursionExpression recursion(ISqlQueryableExpression queryable, FieldMetaData parentId, FieldMetaData childId, int level) {
        return new SqlRecursionExpression(queryable, parentId, childId, level);
    }

    @Override
    public ISqlUpdateExpression update(ISqlFromExpression from, ISqlJoinsExpression joins, ISqlSetsExpression sets, ISqlWhereExpression where) {
        return new SqlUpdateExpression(from, joins, sets, where);
    }

    @Override
    public ISqlDeleteExpression delete(ISqlFromExpression from, ISqlJoinsExpression joins, ISqlWhereExpression where) {
        return new SqlDeleteExpression(from, joins, where);
    }

    @Override
    public ISqlDynamicColumnExpression dynamicColumn(String column, Class<?> type, ISqlTableRefExpression tableISqlTableRefExpression) {
        return new SqlDynamicColumnExpression(column, type, tableISqlTableRefExpression);
    }

    @Override
    public ISqlTableRefExpression tableRef(String name) {
        return new SqlTableRefExpression(name);
    }
}
