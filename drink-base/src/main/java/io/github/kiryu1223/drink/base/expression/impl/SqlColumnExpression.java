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
import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTableRefExpression;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.List;

/**
 * @author kiryu1223
 * @since 3.0
 */
public class SqlColumnExpression implements ISqlColumnExpression {
    private final FieldMetaData fieldMetaData;
    private ISqlTableRefExpression tableRefExpression;

    protected SqlColumnExpression(FieldMetaData fieldMetaData, ISqlTableRefExpression tableRefExpression) {
        this.fieldMetaData = fieldMetaData;
        this.tableRefExpression = tableRefExpression;
    }

    @Override
    public FieldMetaData getFieldMetaData() {
        return fieldMetaData;
    }

    @Override
    public ISqlTableRefExpression getTableRefExpression() {
        return tableRefExpression;
    }

    @Override
    public void setTableRefExpression(ISqlTableRefExpression tableRefExpression) {
        this.tableRefExpression = tableRefExpression;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        IDialect dialect = config.getDisambiguation();
        String columnName = dialect.disambiguation(getFieldMetaData().getColumnName());
        return tableRefExpression.getSqlAndValue(config,values) + "." + columnName;
    }
}
