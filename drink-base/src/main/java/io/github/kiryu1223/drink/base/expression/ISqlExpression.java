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
 * sql表达式基类
 *
 * @author kiryu1223
 * @since 3.0
 */
public interface ISqlExpression {

    default Class<?> getType() {
        return void.class;
    }

    /**
     * 访问者
     */
    default void accept(SqlTreeVisitor visitor) {
        visitor.visit(this);
    }

    default void accept(SqlTreeTransformer transformer) {
        transformer.visit(this);
    }
    /**
     * 获取sql和参数
     */
    String getSqlAndValue(IConfig config, List<SqlValue> values);

    /**
     * 获取sql
     */
    default String getSql(IConfig config) {
        return getSqlAndValue(config, null);
    }

    /**
     * 获取自己的拷贝
     */
    <T extends ISqlExpression> T copy(IConfig config);
}
