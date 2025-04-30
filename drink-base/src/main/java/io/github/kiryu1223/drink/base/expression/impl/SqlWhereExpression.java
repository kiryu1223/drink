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

import io.github.kiryu1223.drink.base.Filter;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.SqlOption;
import io.github.kiryu1223.drink.base.SqlOptions;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.visitor.ISqlVisitor;
import io.github.kiryu1223.expressionTree.expressions.LambdaExpression;

import java.util.List;

/**
 * @author kiryu1223
 * @since 3.0
 */
public class SqlWhereExpression implements ISqlWhereExpression {
    private final ISqlConditionsExpression conditions;

    SqlWhereExpression(ISqlConditionsExpression conditions) {
        this.conditions = conditions;
    }

    public void addCondition(ISqlExpression condition) {
        conditions.getConditions().add(condition);
    }

    public boolean isEmpty() {
        return conditions.isEmpty();
    }

    public ISqlConditionsExpression getConditions() {
        return conditions;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        SqlOption option = SqlOptions.getOption();
        if (!option.isIgnoreFilterAll()) {
            ISqlQueryableExpression current = SqlOptions.getCurrentQueryable().peek();
            ISqlFromExpression from = current.getFrom();
            ISqlTableExpression sqlTableExpression = from.getSqlTableExpression();
            if (sqlTableExpression instanceof ISqlRealTableExpression) {
                ISqlRealTableExpression realTable = (ISqlRealTableExpression) sqlTableExpression;
                Class<?> type = realTable.getType();
                Filter filter = config.getFilter();
                ISqlWhereExpression whereCopy = copy(config);
                List<LambdaExpression<?>> applyList = filter.getApplyList(type, option.getIgnoreFilterIds());
                for (LambdaExpression<?> lambdaExpression : applyList) {
                    ISqlVisitor sqlVisitor = config.getSqlVisitor(from, current.getJoins(), -1);
                    ISqlExpression expression = sqlVisitor.visit(lambdaExpression);
                    whereCopy.addCondition(expression);
                }
                return whereCopy.getSqlAndValue(config, values);
            }
        }
        return "WHERE " + getConditions().getSqlAndValue(config, values);
    }
}
