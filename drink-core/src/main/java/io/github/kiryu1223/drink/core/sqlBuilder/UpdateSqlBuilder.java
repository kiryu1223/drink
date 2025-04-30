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
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.doGetAsName;
import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.getFirst;


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
    public void addJoin(JoinType joinType, ISqlTableExpression table, ISqlExpression on) {
        String first = getFirst(table.getMainTableClass());
        Set<String> stringSet = new HashSet<>(update.getJoins().getJoins().size() + 1);
        stringSet.add(update.getFrom().getAsName().getName());
        for (ISqlJoinExpression join : update.getJoins().getJoins()) {
            stringSet.add(join.getAsName().getName());
        }
        AsName asName = doGetAsName(first,stringSet);
        ISqlJoinExpression join = factory.join(
                joinType,
                table,
                on,
                asName
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
     * 添加条件
     */
    public void addWhere(ISqlExpression where) {
        update.addWhere(where);
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

    @Override
    public String getSql() {
        return getSqlAndValue(null);
    }

    @Override
    public String getSqlAndValue(List<SqlValue> sqlValues) {
        return update.getSqlAndValue(config, sqlValues);
    }

    public IConfig getConfig() {
        return config;
    }

    public ISqlUpdateExpression getUpdate() {
        return update;
    }
}
