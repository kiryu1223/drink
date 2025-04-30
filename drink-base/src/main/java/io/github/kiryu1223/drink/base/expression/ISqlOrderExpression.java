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

/**
 * 单个order（因为可能会指定升降序）
 *
 * @author kiryu1223
 * @since 3.0
 */
public interface ISqlOrderExpression extends ISqlExpression {
    /**
     * 获取排序的目标
     */
    ISqlExpression getExpression();

    /**
     * 是否为升序
     */
    boolean isAsc();

    @Override
    default ISqlOrderExpression copy(IConfig config) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.order(getExpression().copy(config), isAsc());
    }
}
