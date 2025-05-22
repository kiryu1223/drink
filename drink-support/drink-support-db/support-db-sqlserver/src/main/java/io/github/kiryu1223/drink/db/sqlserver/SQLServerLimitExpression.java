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
package io.github.kiryu1223.drink.db.sqlserver;


import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.impl.SqlLimitExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.List;

/**
 * SqlServer Limit表达式
 *
 * @author kiryu1223
 * @since 3.0
 */
public class SQLServerLimitExpression extends SqlLimitExpression {
    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        if (hasRowsAndOffset()) {
            return String.format("OFFSET %d ROWS FETCH NEXT %d ROWS ONLY", offset, rows);
        }
        return "";
    }
}
