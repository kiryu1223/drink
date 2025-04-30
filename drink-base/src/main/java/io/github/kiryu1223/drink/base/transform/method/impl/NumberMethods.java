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
import io.github.kiryu1223.drink.base.transform.method.INumberMethods;
import io.github.kiryu1223.drink.base.transform.Transform;

import java.util.Arrays;
import java.util.List;

/**
 * BigNumber方法
 *
 * @author kiryu1223
 * @since 3.0
 */
public class NumberMethods extends Transform implements INumberMethods {
    public NumberMethods(IConfig config) {
        super(config);
    }

    /**
     * 取模运算
     */
    public ISqlExpression remainder(ISqlExpression left, ISqlExpression right) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        List<ISqlExpression> sqlExpressions;
        switch (config.getDbType()) {
            case Oracle:
                function = Arrays.asList("MOD(", ",", ")");
                break;
            default:
                function = Arrays.asList("(", " % ", ")");
        }
        return factory.template(function, Arrays.asList(left, right));
    }
}
