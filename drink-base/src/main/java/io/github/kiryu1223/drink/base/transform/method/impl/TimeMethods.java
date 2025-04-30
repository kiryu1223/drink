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
package io.github.kiryu1223.drink.base.transform.method.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.expression.SqlOperator;
import io.github.kiryu1223.drink.base.transform.method.ITimeMethods;
import io.github.kiryu1223.drink.base.transform.Transform;

/**
 * 时间相关函数
 *
 * @author kiryu1223
 * @since 3.0
 */
public class TimeMethods extends Transform implements ITimeMethods {

    public TimeMethods(IConfig config) {
        super(config);
    }

    /**
     * 左时间大于右时间表达式
     */
    public ISqlExpression isAfter(ISqlExpression thiz, ISqlExpression that) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.binary(SqlOperator.GT, thiz, that);
    }

    /**
     * 左时间小于右时间表达式
     */
    public ISqlExpression isBefore(ISqlExpression thiz, ISqlExpression that) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.binary(SqlOperator.LT, thiz, that);
    }

    /**
     * 左时间等于右时间表达式
     */
    public ISqlExpression isEqual(ISqlExpression thiz, ISqlExpression that) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.binary(SqlOperator.EQ, thiz, that);
    }
}
