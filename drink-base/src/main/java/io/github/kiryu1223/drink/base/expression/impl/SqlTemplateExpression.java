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
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTemplateExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.List;

/**
 * @author kiryu1223
 * @since 3.0
 */
public class SqlTemplateExpression implements ISqlTemplateExpression {
    private final List<String> functions;
    private final List<? extends ISqlExpression> expressions;

    protected SqlTemplateExpression(List<String> functions, List<? extends ISqlExpression> expressions) {
        this.functions = functions;
        this.expressions = expressions;
    }

    @Override
    public List<String> getTemplateStrings() {
        return functions;
    }

    @Override
    public List<? extends ISqlExpression> getExpressions() {
        return expressions;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getTemplateStrings().size(); i++) {
            String function = getTemplateStrings().get(i);
            sb.append(function);
            if (i < getExpressions().size()) {
                ISqlExpression expression = getExpressions().get(i);
                sb.append(expression.getSqlAndValue(config, values));
            }
        }
        return sb.toString();
    }
}
