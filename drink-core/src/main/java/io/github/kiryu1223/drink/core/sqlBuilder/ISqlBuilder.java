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
import io.github.kiryu1223.drink.core.visitor.SqlVisitor;
import io.github.kiryu1223.expressionTree.expressions.LambdaExpression;

import java.util.List;

/**
 * SQL构造器
 *
 * @author kiryu1223
 * @since 3.0
 */
public interface ISqlBuilder {
    /**
     * 获取配置
     */
    IConfig getConfig();

    /**
     * 获取SQL
     */
    String getSql();

    /**
     * 获取SQL和参数
     */
    String getSqlAndValue(List<SqlValue> values);

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
                        SqlVisitor sqlVisitor = new SqlVisitor(config, query);
                        ISqlExpression expression = sqlVisitor.visit(lambdaExpression);
                        ISqlConditionsExpression condition = factory.condition();
                        condition.addCondition(factory.parens(where.getConditions()));
                        condition.addCondition(expression);
                        query.setWhere(condition);
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
                            SqlVisitor sqlVisitor = new SqlVisitor(config, query, index++);
                            ISqlExpression expression = sqlVisitor.visit(lambdaExpression);
                            ISqlConditionsExpression condition = factory.condition();
                            condition.addCondition(factory.parens(conditions));
                            condition.addCondition(expression);
                            join.setConditions(condition);
                        }
                    }
                }
            }
        });
        return copy;
    }

    void addIgnoreFilterId(String filterId);

    void setIgnoreFilterAll(boolean condition);
}
