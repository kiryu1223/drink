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
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.sqlExt.ISqlKeywords;
import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author kiryu1223
 * @since 3.0
 */
public class SqlSetExpression implements ISqlSetExpression {
    private final ISqlColumnExpression column;
    private final ISqlExpression value;

    protected SqlSetExpression(ISqlColumnExpression column, ISqlExpression value) {
        this.column = column;
        this.value = value;
    }

    @Override
    public ISqlColumnExpression getColumn() {
        return column;
    }

    @Override
    public ISqlExpression getValue() {
        return value;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        List<String> strings = new ArrayList<>();
        strings.add(column.getSqlAndValue(config, values));
        strings.add("=");
        FieldMetaData fieldMetaData = column.getFieldMetaData();
        ITypeHandler<?> typeHandler = fieldMetaData.getTypeHandler();
        if (value instanceof ISqlValueExpression) {
            if (value instanceof ISqlSingleValueExpression) {
                ISqlSingleValueExpression sqlSingleValueExpression = (ISqlSingleValueExpression) value;
                Object value1 = sqlSingleValueExpression.getValue();
                if (value1 instanceof ISqlKeywords) {
                    ISqlKeywords iSqlKeywords = (ISqlKeywords) value1;
                    strings.add(iSqlKeywords.getKeyword(config));
                }
                else {
                    if (values != null) {
                        values.add(new SqlValue(value1, typeHandler));
                        strings.add("?");
                    }
                    else {
                        strings.add("NULL");
                    }
                }
            }
            else {
                if (values != null) {
                    ISqlCollectedValueExpression sqlCollectedValueExpression = (ISqlCollectedValueExpression) value;
                    Collection<?> collection = sqlCollectedValueExpression.getCollection();
                    values.add(new SqlValue(collection, typeHandler));
                    List<String> ss = new ArrayList<>(collection.size());
                    for (Object o : collection) {
                        ss.add("?");
                    }
                    strings.add(String.join(",", ss));
                }
                else {
                    strings.add("NULL");
                }
            }
        }
        else {
            strings.add(value.getSqlAndValue(config, values));
        }
        return String.join(" ", strings);
    }
}
