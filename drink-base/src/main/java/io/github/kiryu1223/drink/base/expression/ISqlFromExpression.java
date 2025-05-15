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
package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.metaData.MetaDataCache;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.List;

/**
 * from表达式
 *
 * @author kiryu1223
 * @since 3.0
 */
public interface ISqlFromExpression extends ISqlExpression {
    /**
     * 获取表表达式(可能是实体表也可能是虚拟表)
     */
    ISqlTableExpression getSqlTableExpression();

    /**
     * 判断是否为无from查询
     */
    default boolean isEmptyTable() {
        Class<?> tableClass = getSqlTableExpression().getMainTableClass();
        MetaData metaData = MetaDataCache.getMetaData(tableClass);
        return metaData.isEmptyTable();
    }

    default String getTableName() {
        Class<?> mainTableClass = getSqlTableExpression().getMainTableClass();
        MetaData metaData = MetaDataCache.getMetaData(mainTableClass);
        return metaData.getTableName();
    }

    ISqlTableRefExpression getTableRefExpression();

    @Override
    default Class<?> getType() {
        return getSqlTableExpression().getType();
    }

    String normalTable(IConfig config, List<SqlValue> values);

    String emptyTable(IConfig config, List<SqlValue> values);

    @Override
    default ISqlFromExpression copy(IConfig config) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.from(getSqlTableExpression().copy(config), getTableRefExpression());
    }
}
