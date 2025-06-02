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


import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 更新语句构造器
 *
 * @author kiryu1223
 * @since 3.0
 */
public class UpdateSqlBuilder implements ISqlBuilder {
    private final IConfig config;
    private final SqlExpressionFactory factory;
    private final ISqlUpdateExpression update;
    private final List<String> ignoreFilterIds = new ArrayList<>();
    private boolean ignoreFilterAll = false;

    public UpdateSqlBuilder(IConfig config, ISqlUpdateExpression update) {
        this.config = config;
        this.update = update;
        this.factory = config.getSqlExpressionFactory();
    }

    /**
     * 添加关联表
     *
     * @param joinType 关联类型
     * @param table    关联表
     * @param on       关联条件
     */
    public void addJoin(JoinType joinType, ISqlTableExpression table, ISqlConditionsExpression on) {
        ISqlJoinExpression join = factory.join(
                joinType,
                table,
                on,
                factory.tableRef(table.getMainTableClass())
        );
        update.addJoin(join);
    }

    /**
     * 添加需要更新的列
     */
    public void addSet(ISqlSetExpression set) {
        update.addSet(set);
    }

    /**
     * 是否有条件
     */
    public boolean hasWhere() {
        return !update.getWhere().isEmpty();
    }

    public boolean hasSet() {
        return !update.getSets().isEmpty();
    }

    public IConfig getConfig() {
        return config;
    }

    public ISqlUpdateExpression getUpdate() {
        return update;
    }

    @Override
    public List<String> getIgnoreFilterIds() {
        return ignoreFilterIds;
    }

    @Override
    public boolean isIgnoreFilterAll() {
        return ignoreFilterAll;
    }

    public void setIgnoreFilterAll(boolean ignoreFilterAll) {
        this.ignoreFilterAll = ignoreFilterAll;
    }

    public void addIgnoreFilterId(String filterId) {
        ignoreFilterIds.add(filterId);
    }

    @Override
    public ISqlExpression getSqlExpression() {
        return update;
    }

    public void addAndOrWhere(ISqlExpression cond,boolean isAnd) {
        ISqlWhereExpression where = update.getWhere();
        ISqlConditionsExpression conditions = where.getConditions();
        if (conditions.isAnd()!=isAnd)
        {
            conditions.addCondition(cond);
        }
        else
        {
            List<ISqlExpression> list = new ArrayList<>(Arrays.asList(conditions, cond));
            where.setConditions(factory.condition(list,isAnd));
        }
    }
}
