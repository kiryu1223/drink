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
package io.github.kiryu1223.drink.db.h2;


import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.expression.impl.DefaultSqlExpressionFactory;

import java.util.Collection;

/**
 * H2表达式工厂
 *
 * @author kiryu1223
 * @since 3.0
 */
public class H2ExpressionFactory extends DefaultSqlExpressionFactory {
    public H2ExpressionFactory(IConfig config)
    {
        super(config);
    }

    @Override
    public ISqlPivotExpression pivot(ISqlQueryableExpression tableExpression, ISqlTemplateExpression aggregationColumn, Class<?> aggregationType, ISqlColumnExpression transColumn, Collection<Object> transColumnValues, ISqlTableRefExpression tempRefExpression, ISqlTableRefExpression pivotRefExpression) {
        return new H2PivotExpression(tableExpression, aggregationColumn, aggregationType, transColumn, transColumnValues, tempRefExpression, pivotRefExpression);
    }
}
