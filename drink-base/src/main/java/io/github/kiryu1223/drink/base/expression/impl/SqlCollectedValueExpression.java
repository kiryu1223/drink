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
import io.github.kiryu1223.drink.base.expression.ISqlCollectedValueExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;
import io.github.kiryu1223.drink.base.toBean.handler.TypeHandlerManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author kiryu1223
 * @since 3.0
 */
public class SqlCollectedValueExpression implements ISqlCollectedValueExpression {
    private final Collection<?> collection;
    private String delimiter = ",";

    protected SqlCollectedValueExpression(Collection<?> collection) {
        this.collection = collection;
    }

    @Override
    public Collection<?> getCollection() {
        return collection;
    }

    @Override
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public String getDelimiter() {
        return delimiter;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        List<String> strings = new ArrayList<>(getCollection().size());
        ITypeHandler<?> typeHandler = null;
        for (Object obj : collection) {
            strings.add("?");
            if (values != null) {
                if (typeHandler == null) {
                    typeHandler = obj == null ? TypeHandlerManager.getUnKnowTypeHandler() : TypeHandlerManager.get(obj.getClass());
                }
                values.add(new SqlValue(obj, typeHandler));
            }
        }
        return "(" + String.join(delimiter, strings) + ")";
    }
}
