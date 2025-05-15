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
    protected final ISqlTableRefExpression tableRefExpression;

    public SqlFromExpression(ISqlTableExpression sqlTableExpression, ISqlTableRefExpression tableRefExpression) {
        this.sqlTableExpression = sqlTableExpression;
        this.tableRefExpression = tableRefExpression;
    }

    @Override
    public ISqlTableExpression getSqlTableExpression() {
        return sqlTableExpression;
    }

    @Override
    public ISqlTableRefExpression getTableRefExpression() {
        return tableRefExpression;
    }

    @Override
    public String normalTable(IConfig config, List<SqlValue> values) {
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

        return "FROM " + builder + " AS " + disambiguation.disambiguation(tableRefExpression.getDisPlayName());
    }

    @Override
    public String emptyTable(IConfig config, List<SqlValue> values) {
        return "";
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        if (isEmptyTable()) {
            return emptyTable(config, values);
        }
        else {
            return normalTable(config, values);
        }
    }
}
