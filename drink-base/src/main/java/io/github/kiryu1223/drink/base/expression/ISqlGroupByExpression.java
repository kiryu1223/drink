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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 分组表达式
 *
 * @author kiryu1223
 * @since 3.0
 */
public interface ISqlGroupByExpression extends ISqlExpression {
    /**
     * 设置分组选择的字段
     */
    void setColumns(LinkedHashMap<String, ISqlExpression> columns);

    /**
     * 获取分组选择的字段
     *
     * @return
     */
    LinkedHashMap<String, ISqlExpression> getColumns();

    default boolean hasColumns() {
        return !getColumns().isEmpty();
    }

    @Override
    default ISqlGroupByExpression copy(IConfig config) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        ISqlGroupByExpression groupByExpression = factory.groupBy();
        if (hasColumns()) {
            for (Map.Entry<String, ISqlExpression> entry : getColumns().entrySet()) {
                groupByExpression.getColumns().put(entry.getKey(), entry.getValue().copy(config));
            }
        }
        return groupByExpression;
    }
}
