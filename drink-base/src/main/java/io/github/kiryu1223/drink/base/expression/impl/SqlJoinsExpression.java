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
import io.github.kiryu1223.drink.base.expression.ISqlJoinExpression;
import io.github.kiryu1223.drink.base.expression.ISqlJoinsExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kiryu1223
 * @since 3.0
 */
public class SqlJoinsExpression implements ISqlJoinsExpression {
    private final List<ISqlJoinExpression> joins = new ArrayList<>();

    @Override
    public void addJoin(ISqlJoinExpression join) {
        joins.add(join);
    }

    public List<ISqlJoinExpression> getJoins() {
        return joins;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        if (getJoins().isEmpty()) return "";
        List<String> strings = new ArrayList<>(getJoins().size());
        for (ISqlJoinExpression join : getJoins()) {
            strings.add(join.getSqlAndValue(config, values));
        }
        return String.join(" ", strings);
    }
}
