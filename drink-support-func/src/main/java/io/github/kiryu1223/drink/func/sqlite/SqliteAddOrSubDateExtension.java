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
package io.github.kiryu1223.drink.func.sqlite;

import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlSingleValueExpression;
import io.github.kiryu1223.drink.base.sqlExt.BaseSqlExtension;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Sqlite时间运算函数扩展
 *
 * @author kiryu1223
 * @since 3.0
 */
public class SqliteAddOrSubDateExtension extends BaseSqlExtension {
    @Override
    public ISqlExpression parse(IConfig config, Method method, List<ISqlExpression> args,boolean[] useSuper) {
        List<String> templates = new ArrayList<>();
        List<ISqlExpression> sqlExpressions = new ArrayList<>();
        boolean isPlus = method.getName().equals("addDate");
        if (method.getParameterTypes()[0] == LocalDate.class) {
            templates.add("DATE(");
        }
        else {
            templates.add("DATETIME(");
        }
        sqlExpressions.add(args.get(0));
        if (method.getParameterCount() == 2) {
            ISqlExpression num = args.get(1);
            if (num instanceof ISqlSingleValueExpression) {
                ISqlSingleValueExpression valueExpression = (ISqlSingleValueExpression) num;
                templates.add(",'" + (isPlus ? "" : "-") + valueExpression.getValue() + " DAY')");
            }
            else {
                throw new DrinkException(DbType.SQLite.name());
            }
        }
        else {
            ISqlExpression num = args.get(2);
            if (num instanceof ISqlSingleValueExpression) {
                ISqlSingleValueExpression valueExpression = (ISqlSingleValueExpression) num;
                templates.add(",'" + (isPlus ? "" : "-") + valueExpression.getValue() + " ");
                sqlExpressions.add(args.get(1));
                templates.add("')");
            }
            else {
                throw new DrinkException(DbType.SQLite.name());
            }
        }
        return config.getSqlExpressionFactory().template(templates, sqlExpressions);
    }
}
