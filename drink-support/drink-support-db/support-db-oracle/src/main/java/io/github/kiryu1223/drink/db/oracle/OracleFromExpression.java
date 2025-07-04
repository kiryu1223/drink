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
package io.github.kiryu1223.drink.db.oracle;


import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlQueryableExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTableExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTableRefExpression;
import io.github.kiryu1223.drink.base.expression.ISqlWithExpression;
import io.github.kiryu1223.drink.base.expression.impl.SqlFromExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.util.DrinkUtil;

import java.util.List;

/**
 * Oracle From 表达式
 *
 * @author kiryu1223
 * @since 3.0
 */
public class OracleFromExpression extends SqlFromExpression {
    protected OracleFromExpression(ISqlTableExpression sqlTableExpression, ISqlTableRefExpression tableRefExpression) {
        super(sqlTableExpression, tableRefExpression);
    }

    @Override
    public String emptyTable(IConfig config, List<SqlValue> values) {
        // oracle 不支持 无 from 查询
        // 所以我们要加上DUAL表
        return "FROM " + config.getDisambiguation().disambiguationTableName("DUAL");
    }

    @Override
    public String normalTable(IConfig config, List<SqlValue> values) {
        StringBuilder builder = new StringBuilder();
        if (sqlTableExpression instanceof ISqlWithExpression) {
            ISqlWithExpression withExpression = (ISqlWithExpression) sqlTableExpression;
            builder.append(withExpression.withTableName());
        }
        else {
            builder.append(sqlTableExpression.getSqlAndValue(config, values));
        }

        if (sqlTableExpression instanceof ISqlQueryableExpression) {
            builder.insert(0, "(");
            builder.append(")");
        }

        return "FROM " + builder + tableRefExpression.getSqlAndValue(config,values);
    }
}
