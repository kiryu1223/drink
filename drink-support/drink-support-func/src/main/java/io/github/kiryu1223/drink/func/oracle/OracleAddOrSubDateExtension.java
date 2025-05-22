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

import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlSingleValueExpression;
import io.github.kiryu1223.drink.base.sqlExt.BaseSqlExtension;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Oracle时间运算函数扩展
 *
 * @author kiryu1223
 * @since 3.0
 */
public class OracleAddOrSubDateExtension extends BaseSqlExtension {
    @Override
    public ISqlExpression parse(IConfig config, Method method, List<ISqlExpression> args,boolean[] useSuper) {
        List<String> templates = new ArrayList<>();
        List<ISqlExpression> sqlExpressions = new ArrayList<>();
        if (method.getParameterCount() == 2) {
            templates.add("(");
            sqlExpressions.add(args.get(0));
            ISqlExpression num = args.get(1);
            if (num instanceof ISqlSingleValueExpression) {
                ISqlSingleValueExpression valueExpression = (ISqlSingleValueExpression) num;
                if (method.getName().equals("addDate")) {
                    templates.add(" + INTERVAL '" + valueExpression.getValue() + "' DAY)");
                }
                else {
                    templates.add(" - INTERVAL '" + valueExpression.getValue() + "' DAY)");
                }
            }
            else {
                throw new DrinkException(DbType.Oracle.name());
            }
        }
        else {
            templates.add("(");
            sqlExpressions.add(args.get(0));
            sqlExpressions.add(args.get(1));
            ISqlExpression num = args.get(2);
            if (num instanceof ISqlSingleValueExpression) {
                ISqlSingleValueExpression valueExpression = (ISqlSingleValueExpression) num;
                if (method.getName().equals("addDate")) {
                    templates.add(" + INTERVAL '" + valueExpression.getValue() + "' ");
                }
                else {
                    templates.add(" - INTERVAL '" + valueExpression.getValue() + "' ");
                }
                templates.add(")");
            }
            else {
                throw new DrinkException(DbType.Oracle.name());
            }
        }
        return config.getSqlExpressionFactory().template(templates, sqlExpressions);
    }
}
