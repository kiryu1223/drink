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

import static io.github.kiryu1223.drink.base.util.DrinkUtil.isString;
import static io.github.kiryu1223.drink.base.util.DrinkUtil.isVoid;

/**
 * 二元运算表达式
 *
 * @author kiryu1223
 * @since 3.0
 */
public interface ISqlBinaryExpression extends ISqlExpression {
    /**
     * 左表达式
     */
    ISqlExpression getLeft();

    /**
     * 右表达式
     */
    ISqlExpression getRight();

    /**
     * SQL运算符
     */
    SqlOperator getOperator();

    @Override
    default Class<?> getType() {
        Class<?> leftType = getLeft().getType();
        Class<?> rightType = getRight().getType();
        if (leftType == rightType) {
            return leftType;
        }
        else if (isString(leftType) || isString(rightType)) {
            return String.class;
        }
        else if (isVoid(leftType) && !isVoid(rightType)) {
            return rightType;
        }
        else if (!isVoid(leftType) && isVoid(rightType)) {
            return leftType;
        }
        else {
            return ISqlExpression.super.getType();
        }
    }

    @Override
    default ISqlBinaryExpression copy(IConfig config) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.binary(getOperator(), getLeft().copy(config), getRight().copy(config));
    }
}
