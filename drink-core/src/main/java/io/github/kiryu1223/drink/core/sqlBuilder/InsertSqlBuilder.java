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
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlFromExpression;
import io.github.kiryu1223.drink.base.expression.ISqlJoinsExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.Collections;
import java.util.List;

/**
 * 新增语句构建器
 *
 * @author kiryu1223
 * @since 3.0
 */
public class InsertSqlBuilder implements ISqlBuilder {
    private final IConfig config;

    public InsertSqlBuilder(IConfig config) {
        this.config = config;
    }


    @Override
    public String getSql() {
        return "";
    }

    @Override
    public String getSqlAndValue(List<SqlValue> values) {
        return "";
    }

    @Override
    public boolean isIgnoreFilterAll() {
        return false;
    }

    @Override
    public List<String> getIgnoreFilterIds() {
        return Collections.emptyList();
    }

    @Override
    public void addIgnoreFilterId(String filterId) {

    }

    @Override
    public void setIgnoreFilterAll(boolean condition) {

    }

    @Override
    public ISqlFromExpression getForm() {
        return null;
    }

    @Override
    public ISqlJoinsExpression getJoins() {
        return null;
    }

    @Override
    public void as(String alisaName) {

    }

    @Override
    public ISqlExpression getSqlExpression() {
        return null;
    }

    public IConfig getConfig() {
        return config;
    }
}
