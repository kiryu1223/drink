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


import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.expression.impl.SqlQueryableExpression;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Oracle查询表达式
 *
 * @author kiryu1223
 * @since 3.0
 */
public class OracleQueryableExpression extends SqlQueryableExpression {
    protected OracleQueryableExpression(ISqlSelectExpression select, ISqlFromExpression from, ISqlJoinsExpression joins, ISqlWhereExpression where, ISqlGroupByExpression groupBy, ISqlHavingExpression having, ISqlOrderByExpression orderBy, ISqlLimitExpression limit) {
        super(select, from, joins, where, groupBy, having, orderBy, limit);
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
            List<String> strings = new ArrayList<>();
//        if (!from.isEmptyTable() && (limit.onlyHasRows() || limit.hasRowsAndOffset()))
//        {
//            strings.add("SELECT * FROM (SELECT t.*,ROWNUM AS \"-ROWNUM-\" FROM (");
//        }
            tryWith(config, strings, values);
            String fromSqlAndValue = from.getSqlAndValue(config, values);
            if (!fromSqlAndValue.isEmpty()) strings.add(fromSqlAndValue);
            String joinsSqlAndValue = joins.getSqlAndValue(config, values);
            if (!joinsSqlAndValue.isEmpty()) strings.add(joinsSqlAndValue);
            String whereSqlAndValue = where.getSqlAndValue(config, values);
            if (!whereSqlAndValue.isEmpty()) strings.add(whereSqlAndValue);
            String groupBySqlAndValue = groupBy.getSqlAndValue(config, values);
            if (!groupBySqlAndValue.isEmpty()) strings.add(groupBySqlAndValue);
            String havingSqlAndValue = having.getSqlAndValue(config, values);
            if (!havingSqlAndValue.isEmpty()) strings.add(havingSqlAndValue);
            String orderBySqlAndValue = orderBy.getSqlAndValue(config, values);
            if (!orderBySqlAndValue.isEmpty()) strings.add(orderBySqlAndValue);
            if (!from.isEmptyTable(config)) {
                limitAndOrderCheck(strings, values, config);
                String limitSqlAndValue = limit.getSqlAndValue(config, values);
                if (!limitSqlAndValue.isEmpty()) strings.add(limitSqlAndValue);
            }
//        if (!from.isEmptyTable() && (limit.onlyHasRows() || limit.hasRowsAndOffset()))
//        {
//            strings.add(") t) WHERE \"-ROWNUM-\"");
//            if (limit.onlyHasRows())
//            {
//                strings.add("<= " + limit.getRows());
//            }
//            else
//            {
//                strings.add(String.format("BETWEEN %d AND %d", limit.getOffset() + 1, limit.getOffset() + limit.getRows()));
//            }
//        }
            return String.join(" ", strings);

    }

    private void limitAndOrderCheck(List<String> strings, List<SqlValue> values, IConfig config) {
        if (limit.hasRowsOrOffset() && orderBy.isEmpty()) {
            MetaData metaData = config.getMetaData(from.getSqlTableExpression().getMainTableClass());
            FieldMetaData primary = metaData.getPrimary();
            if (primary == null) {
                throw new DrinkException(DbType.Oracle.name());
            }
            SqlExpressionFactory factory = config.getSqlExpressionFactory();
            ISqlOrderByExpression sqlOrderByExpression = factory.orderBy();
            sqlOrderByExpression.addOrder(factory.order(factory.column(primary,from.getTableRefExpression())));
            strings.add(sqlOrderByExpression.getSqlAndValue(config, values));
        }
    }
}
