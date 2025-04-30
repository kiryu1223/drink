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
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.List;

/**
 * @author kiryu1223
 * @since 3.0
 */
public class SqlFromExpression implements ISqlFromExpression {
    protected final ISqlTableExpression sqlTableExpression;
    protected AsName asName;

    public SqlFromExpression(ISqlTableExpression sqlTableExpression, AsName asName) {
        this.sqlTableExpression = sqlTableExpression;
        this.asName = asName;
    }

    @Override
    public ISqlTableExpression getSqlTableExpression() {
        return sqlTableExpression;
    }

    @Override
    public AsName getAsName() {
        return asName;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        if (isEmptyTable()) return "";
        IDialect disambiguation = config.getDisambiguation();
        StringBuilder builder = new StringBuilder();
        if (sqlTableExpression instanceof ISqlWithExpression) {
            ISqlWithExpression withExpression = (ISqlWithExpression) sqlTableExpression;
            builder.append(disambiguation.disambiguationTableName(withExpression.withTableName()));
        }
        else {
            builder.append(sqlTableExpression.getSqlAndValue(config, values));
        }

        if (sqlTableExpression instanceof ISqlQueryableExpression) {
            builder.insert(0, "(");
            builder.append(")");
        }

        if (asName != null) {
            return "FROM " + builder + " AS " + disambiguation.disambiguation(asName.getName());
        }
        else {
            return "FROM " + builder;
        }
    }
}
