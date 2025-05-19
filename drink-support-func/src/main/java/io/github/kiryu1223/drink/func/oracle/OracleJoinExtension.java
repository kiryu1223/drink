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
package io.github.kiryu1223.drink.func.oracle;


import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.sqlExt.BaseSqlExtension;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Oracle join函数扩展
 *
 * @author kiryu1223
 * @since 3.0
 */
public class OracleJoinExtension extends BaseSqlExtension {
    @Override
    public ISqlExpression parse(IConfig config, Method method, List<ISqlExpression> args,boolean[] useSuper) {
        List<String> templates = new ArrayList<>();
        List<ISqlExpression> sqlExpressions = new ArrayList<>();
        ISqlExpression separator = args.get(0);
        templates.add("(");
        for (int i = 1; i < args.size(); i++) {
            sqlExpressions.add(args.get(i));
            if (i < args.size() - 1) {
                templates.add("||");
                sqlExpressions.add(separator);
                templates.add("||");
            }
        }
        templates.add(")");
        return config.getSqlExpressionFactory().template(templates, sqlExpressions);
    }
}
