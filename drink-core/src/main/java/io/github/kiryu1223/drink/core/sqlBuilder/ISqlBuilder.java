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
package io.github.kiryu1223.drink.core.sqlBuilder;


import io.github.kiryu1223.drink.base.Filter;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.core.visitor.QuerySqlVisitor;
import io.github.kiryu1223.drink.core.visitor.UpdateSqlVisitor;
import io.github.kiryu1223.expressionTree.expressions.LambdaExpression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SQL构造器
 *
 * @author kiryu1223
 * @since 3.0
 */
public interface ISqlBuilder {
    ISqlExpression getSqlExpression();

    /**
     * 获取配置
     */
    IConfig getConfig();

    /**
     * 获取SQL
     */
    default String getSql() {
        return getSqlAndValue(null);
    }

    /**
     * 获取SQL和参数
     */
    default String getSqlAndValue(List<SqlValue> values) {
        AsNameManager.start();
        String sql = tryFilter(getSqlExpression()).getSqlAndValue(getConfig(), values);
        AsNameManager.clear();
        return sql;
    }

    boolean isIgnoreFilterAll();

    List<String> getIgnoreFilterIds();

    default <T extends ISqlExpression> T tryFilter(T expression) {
        if (isIgnoreFilterAll()) return expression;
        IConfig config = getConfig();
        Filter filter = config.getFilter();
        boolean[] needFilter = {false};
        expression.accept(new SqlTreeVisitor() {
            @Override
            public void visit(ISqlRealTableExpression expression) {
                Class<?> c = expression.getMainTableClass();
                List<LambdaExpression<?>> applyList = filter.getApplyList(c, getIgnoreFilterIds());
                if (!applyList.isEmpty()) {
                    needFilter[0] = true;
                    return;
                }
                super.visit(expression);
            }
        });
        if (!needFilter[0]) return expression;
        T copy = expression.copy(getConfig());
        copy.accept(new SqlTreeVisitor() {
            @Override
            public void visit(ISqlQueryableExpression query) {
                super.visit(query);
                ISqlFromExpression from = query.getFrom();
                ISqlTableExpression table = from.getSqlTableExpression();
                if (table instanceof ISqlRealTableExpression) {
                    ISqlRealTableExpression realTable = (ISqlRealTableExpression) table;
                    Class<?> type = realTable.getMainTableClass();
                    SqlExpressionFactory factory = config.getSqlExpressionFactory();
                    List<LambdaExpression<?>> applyList = filter.getApplyList(type, getIgnoreFilterIds());
                    for (LambdaExpression<?> lambdaExpression : applyList) {
                        ISqlWhereExpression where = query.getWhere();
                        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(config, query);
                        ISqlExpression expression = sqlVisitor.visit(lambdaExpression);
                        query.setWhere(factory.condition(new ArrayList<>(Arrays.asList(factory.parens(where.getConditions()), expression))));
                    }
                }
                ISqlJoinsExpression joins = query.getJoins();
                for (ISqlJoinExpression join : joins.getJoins()) {
                    ISqlTableExpression joinTable = join.getJoinTable();
                    if (joinTable instanceof ISqlRealTableExpression) {
                        ISqlRealTableExpression realTable = (ISqlRealTableExpression) joinTable;
                        Class<?> type = realTable.getMainTableClass();
                        SqlExpressionFactory factory = config.getSqlExpressionFactory();
                        List<LambdaExpression<?>> applyList = filter.getApplyList(type, getIgnoreFilterIds());
                        int index = 1;
                        for (LambdaExpression<?> lambdaExpression : applyList) {
                            ISqlConditionsExpression conditions = join.getConditions();
                            QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(config, query, null, index++);
                            ISqlExpression expression = sqlVisitor.visit(lambdaExpression);
                            join.setConditions(factory.condition(Arrays.asList(factory.parens(conditions), expression)));
                        }
                    }
                }
            }

            @Override
            public void visit(ISqlUpdateExpression update) {
                super.visit(update);
                ISqlFromExpression from = update.getFrom();
                ISqlTableExpression table = from.getSqlTableExpression();
                if (table instanceof ISqlRealTableExpression) {
                    ISqlRealTableExpression realTable = (ISqlRealTableExpression) table;
                    Class<?> type = realTable.getMainTableClass();
                    SqlExpressionFactory factory = config.getSqlExpressionFactory();
                    List<LambdaExpression<?>> applyList = filter.getApplyList(type, getIgnoreFilterIds());
                    for (LambdaExpression<?> lambdaExpression : applyList) {
                        ISqlWhereExpression where = update.getWhere();
                        UpdateSqlVisitor sqlVisitor = new UpdateSqlVisitor(config, update);
                        ISqlExpression expression = sqlVisitor.visit(lambdaExpression);
                        ISqlConditionsExpression conditions = where.getConditions();
                        if (conditions.isAnd()) {
                            conditions.addCondition(expression);
                        }
                        else {
                            update.setWhere(factory.condition(new ArrayList<>(Arrays.asList(factory.parens(conditions), expression)), false));
                        }
                    }
                }
                ISqlJoinsExpression joins = update.getJoins();
                for (ISqlJoinExpression join : joins.getJoins()) {
                    ISqlTableExpression joinTable = join.getJoinTable();
                    if (joinTable instanceof ISqlRealTableExpression) {
                        ISqlRealTableExpression realTable = (ISqlRealTableExpression) joinTable;
                        Class<?> type = realTable.getMainTableClass();
                        SqlExpressionFactory factory = config.getSqlExpressionFactory();
                        List<LambdaExpression<?>> applyList = filter.getApplyList(type, getIgnoreFilterIds());
                        int index = 1;
                        for (LambdaExpression<?> lambdaExpression : applyList) {
                            ISqlConditionsExpression conditions = join.getConditions();
                            UpdateSqlVisitor sqlVisitor = new UpdateSqlVisitor(config, update, index++);
                            ISqlExpression expression = sqlVisitor.visit(lambdaExpression);
                            if (conditions.isAnd()) {
                                conditions.addCondition(expression);
                            }
                            else {
                                join.setConditions(factory.condition(Arrays.asList(factory.parens(conditions), expression), false));
                            }
                        }
                    }
                }
            }

            @Override
            public void visit(ISqlDeleteExpression delete) {
                super.visit(delete);
                ISqlFromExpression from = delete.getFrom();
                ISqlTableExpression table = from.getSqlTableExpression();
                if (table instanceof ISqlRealTableExpression) {
                    ISqlRealTableExpression realTable = (ISqlRealTableExpression) table;
                    Class<?> type = realTable.getMainTableClass();
                    SqlExpressionFactory factory = config.getSqlExpressionFactory();
                    List<LambdaExpression<?>> applyList = filter.getApplyList(type, getIgnoreFilterIds());
                    for (LambdaExpression<?> lambdaExpression : applyList) {
                        ISqlWhereExpression where = delete.getWhere();
                        UpdateSqlVisitor sqlVisitor = new UpdateSqlVisitor(config, delete);
                        ISqlExpression expression = sqlVisitor.visit(lambdaExpression);
                        ISqlConditionsExpression conditions = where.getConditions();
                        if (conditions.isAnd()) {
                            conditions.addCondition(expression);
                        }
                        else {
                            delete.setWhere(factory.condition(new ArrayList<>(Arrays.asList(factory.parens(conditions), expression)), false));
                        }
                    }
                }
                ISqlJoinsExpression joins = delete.getJoins();
                for (ISqlJoinExpression join : joins.getJoins()) {
                    ISqlTableExpression joinTable = join.getJoinTable();
                    if (joinTable instanceof ISqlRealTableExpression) {
                        ISqlRealTableExpression realTable = (ISqlRealTableExpression) joinTable;
                        Class<?> type = realTable.getMainTableClass();
                        SqlExpressionFactory factory = config.getSqlExpressionFactory();
                        List<LambdaExpression<?>> applyList = filter.getApplyList(type, getIgnoreFilterIds());
                        int index = 1;
                        for (LambdaExpression<?> lambdaExpression : applyList) {
                            ISqlConditionsExpression conditions = join.getConditions();
                            UpdateSqlVisitor sqlVisitor = new UpdateSqlVisitor(config, delete, index++);
                            ISqlExpression expression = sqlVisitor.visit(lambdaExpression);
                            if (conditions.isAnd()) {
                                conditions.addCondition(expression);
                            }
                            else {
                                join.setConditions(factory.condition(Arrays.asList(factory.parens(conditions), expression), false));
                            }
                        }
                    }
                }
            }
        });
        return copy;
    }

    void addIgnoreFilterId(String filterId);

    void setIgnoreFilterAll(boolean condition);

    ISqlFromExpression getForm();

    ISqlJoinsExpression getJoins();

    default void as(String alisaName) {
        ISqlJoinsExpression joins = getJoins();
        if (!joins.isEmpty()) {
            List<ISqlJoinExpression> joinsList = joins.getJoins();
            ISqlJoinExpression iSqlJoinExpression = joinsList.get(joinsList.size() - 1);
            iSqlJoinExpression.getTableRefExpression().setName(alisaName);
        }
        else {
            getForm().getTableRefExpression().setName(alisaName);
        }
    }
}
