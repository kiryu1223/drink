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
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.doGetAsName;
import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.getFirst;


/**
 * 删除语句生成器
 *
 * @author kiryu1223
 * @since 3.0
 */
public class DeleteSqlBuilder implements ISqlBuilder {
    private final IConfig config;
    private final ISqlDeleteExpression delete;
    private final SqlExpressionFactory factory;
    private final List<String> ignoreFilterIds = new ArrayList<>();
    private boolean ignoreFilterAll = false;

    public DeleteSqlBuilder(IConfig config, ISqlDeleteExpression delete) {
        this.config = config;
        this.delete = delete;
        factory = config.getSqlExpressionFactory();
    }

    public ISqlDeleteExpression getDelete() {
        return delete;
    }

    /**
     * 添加关联表
     *
     * @param joinType 关联类型
     * @param table    关联表
     * @param on       关联条件
     */
    public void addJoin(JoinType joinType, ISqlTableExpression table, ISqlConditionsExpression on) {
        String first = getFirst(table.getMainTableClass());
        Set<String> stringSet = new HashSet<>(delete.getJoins().getJoins().size() + 1);
        stringSet.add(delete.getFrom().getAsName().getName());
        for (ISqlJoinExpression join : delete.getJoins().getJoins()) {
            stringSet.add(join.getAsName().getName());
        }
        AsName asName = doGetAsName(first,stringSet);
        ISqlJoinExpression join = factory.join(
                joinType,
                table,
                on,
                asName
        );
        delete.addJoin(join);
    }

    /**
     * 添加指定删除的表
     */
//    public void addExclude(Class<?> c) {
//        excludes.add(orderedClasses.indexOf(c));
//    }

    /**
     * 添加删除的where条件
     */
    public void addWhere(ISqlExpression where) {
        delete.addWhere(where);
    }

    @Override
    public IConfig getConfig() {
        return config;
    }

    /**
     * 是否有where条件
     */
    public boolean hasWhere() {
        return !delete.getWhere().isEmpty();
    }

    @Override
    public String getSql() {
        return tryFilter(delete).getSql(null);
    }

    public String getSqlAndValue(List<SqlValue> values) {
        return tryFilter(delete).getSqlAndValue(config, values);
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
}
