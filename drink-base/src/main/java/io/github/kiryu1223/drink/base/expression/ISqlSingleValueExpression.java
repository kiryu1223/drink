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
package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.List;


/**
 * 单个值表达式
 *
 * @author kiryu1223
 * @since 3.0
 */
public interface ISqlSingleValueExpression extends ISqlValueExpression {
    /**
     * 获取值
     */
    Object getValue();

    void setValue(Object value);

    @Override
    default Class<?> getType() {
        Object value = getValue();
        return value == null ? void.class : value.getClass();
    }

    @Override
    String getSqlAndValue(IConfig config, List<SqlValue> values);

    @Override
    default ISqlSingleValueExpression copy(IConfig config) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.value(getValue());
    }
}
