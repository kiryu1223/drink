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
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.transform.Transformer;

import java.util.List;

import static io.github.kiryu1223.drink.base.util.DrinkUtil.isString;

/**
 * @author kiryu1223
 * @since 3.0
 */
public class SqlBinaryExpression implements ISqlBinaryExpression {
    private final SqlOperator operator;
    private final ISqlExpression left;
    private final ISqlExpression right;

    public SqlBinaryExpression(SqlOperator operator, ISqlExpression left, ISqlExpression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public SqlOperator getOperator() {
        return operator;
    }

    @Override
    public ISqlExpression getLeft() {
        return left;
    }

    @Override
    public ISqlExpression getRight() {
        return right;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> sqlValues) {
        StringBuilder sb = new StringBuilder();
        Transformer transformer = config.getTransformer();
        if (operator == SqlOperator.PLUS) {
            if (isString(getType())) {
                ISqlTemplateExpression concat = transformer.concat(left, right);
                sb.append(concat.getSqlAndValue(config, sqlValues));
            }
            else {
                sb.append(build(config, sqlValues));
            }
        }
        else {
            sb.append(build(config, sqlValues));
        }
        return sb.toString();
    }

    private String build(IConfig config, List<SqlValue> sqlValues) {
        return String.join(" ", left.getSqlAndValue(config, sqlValues), operator.getOperator(), right.getSqlAndValue(config, sqlValues));
    }
}
