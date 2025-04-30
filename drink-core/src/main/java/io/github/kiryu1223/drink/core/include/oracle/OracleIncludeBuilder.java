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
package io.github.kiryu1223.drink.core.include.oracle;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlQueryableExpression;
import io.github.kiryu1223.drink.base.session.SqlSession;
import io.github.kiryu1223.drink.base.toBean.Include.IncludeBuilder;
import io.github.kiryu1223.drink.base.toBean.Include.IncludeSet;

import java.util.Collection;
import java.util.List;

/**
 * oracle对象抓取器
 *
 * @author kiryu1223
 * @since 3.0
 */
public class OracleIncludeBuilder<T> extends IncludeBuilder<T> {
    public OracleIncludeBuilder(IConfig config, SqlSession session, Class<T> targetClass, Collection<T> sources, List<IncludeSet> includes, ISqlQueryableExpression queryable) {
        super(config, session, targetClass, sources, includes, queryable);
    }

    @Override
    protected void rowNumber(List<String> rowNumberFunction, List<ISqlExpression> rowNumberParams) {
        rowNumberFunction.add("ROW_NUMBER() OVER (PARTITION BY ");
        rowNumberFunction.add(" ORDER BY ");
        if (rowNumberParams.size() <= 1) {
            rowNumberParams.add(rowNumberParams.get(0));
        }
        for (int i = 0; i < rowNumberParams.size(); i++) {
            if (i < rowNumberParams.size() - 2) rowNumberFunction.add(",");
        }
        rowNumberFunction.add(")");
    }
}
