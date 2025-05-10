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
import io.github.kiryu1223.drink.base.expression.ISqlTypeCastExpression;
import io.github.kiryu1223.drink.base.sqlExt.BaseSqlExtension;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static io.github.kiryu1223.drink.base.util.DrinkUtil.*;

/**
 * Oracle类型转换函数扩展
 *
 * @author kiryu1223
 * @since 3.0
 */
public class OracleCastExtension extends BaseSqlExtension {
    @Override
    public ISqlExpression parse(IConfig config, Method method, List<ISqlExpression> args) {
        List<String> templates = new ArrayList<>();
        List<ISqlExpression> sqlExpressions = new ArrayList<>();
        ISqlExpression expression = args.get(1);
        ISqlTypeCastExpression typeExpression = (ISqlTypeCastExpression) expression;
        Class<?> type = typeExpression.getType();
        if (isBool(type)) {
            templates.add("CAST(");
            sqlExpressions.add(args.get(0));
            templates.add(" AS BOOLEAN)");
        }
        else if (isDate(type) || isDateTime(type)) {
            templates.add("CAST(");
            sqlExpressions.add(args.get(0));
            templates.add(" AS DATE)");
        }
        else if (isByte(type) || isShort(type) || isInt(type) || isLong(type) || isFloat(type) || isDouble(type) || isDecimal(type)) {
            templates.add("CAST(");
            sqlExpressions.add(args.get(0));
            templates.add(" AS NUMBER)");
        }
        else if (isString(type)) {
            templates.add("TO_CHAR(");
            sqlExpressions.add(args.get(0));
            templates.add(")");
        }
        else if (isChar(type)) {
            templates.add("SUBSTR(TO_CHAR(");
            sqlExpressions.add(args.get(0));
            templates.add("),1,1)");
        }
        else {
            throw new UnsupportedOperationException("不支持的Java类型:" + type.getName());
        }
        return config.getSqlExpressionFactory().template(templates, sqlExpressions);
    }
}
