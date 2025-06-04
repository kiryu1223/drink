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
package io.github.kiryu1223.drink.core.sqlBuilder;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.MetaData;

import java.util.*;


/**
 * @author kiryu1223
 * @since 3.0
 */
public class QuerySqlBuilder implements ISqlBuilder {
    private final IConfig config;
    private ISqlQueryableExpression queryable;
    private final SqlExpressionFactory factory;
    private final List<String> ignoreFilterIds = new ArrayList<>();
    private boolean ignoreFilterAll = false;
    private final List<IncludeBuilder> includes =new ArrayList<>();

//    private final Map<String, ISqlQueryableExpression> subQueryMap=new HashMap<>();

    public QuerySqlBuilder(IConfig config, ISqlQueryableExpression queryable) {
        this.config = config;
        this.queryable = queryable;
        this.factory = config.getSqlExpressionFactory();

    }

    public List<IncludeBuilder> getIncludes() {
        return includes;
    }

    public void addSubQuery(IncludeBuilder subQuery) {
        includes.add(subQuery);
    }

    public void addAndOrWhere(ISqlExpression cond,boolean isAnd) {
        ISqlWhereExpression where = queryable.getWhere();
        ISqlConditionsExpression conditions = where.getConditions();
        if (conditions.isAnd()!=isAnd)
        {
            conditions.addCondition(cond);
        }
        else
        {
            List<ISqlExpression> list = new ArrayList<>(Arrays.asList(conditions, cond));
            where.setConditions(factory.condition(list,isAnd));
        }
    }

    public void addJoin(JoinType joinType, ISqlTableExpression table, ISqlConditionsExpression on) {
        ISqlJoinExpression join = factory.join(
                joinType,
                table,
                on,
                factory.tableRef(table.getMainTableClass())
        );
        queryable.addJoin(join);
    }

    public void setGroup(ISqlGroupByExpression group) {
        queryable.setGroup(group);
    }

    public void addHaving(ISqlExpression cond) {
        queryable.addHaving(cond);
    }

    public void addOrder(ISqlOrderExpression order) {
        queryable.addOrder(order);
    }

    public void setSelect(ISqlSelectExpression select) {
        queryable.setSelect(select);
    }

    public void setSelect(Class<?> c) {
        MetaData metaData = config.getMetaData(c);
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        ISqlFromExpression from = queryable.getFrom();
        ISqlJoinsExpression joins = queryable.getJoins();
        List<ISqlExpression> expressions = new ArrayList<>();
        if (from.getSqlTableExpression().getMainTableClass() == c) {
            for (FieldMetaData notIgnoreProperty : metaData.getNotIgnoreAndNavigateFields()) {
                expressions.add(factory.column(notIgnoreProperty, from.getTableRefExpression()));
            }
        }
        else if (joins.getJoins().stream().anyMatch(join -> join.getJoinTable().getMainTableClass() == c)) {
            for (ISqlJoinExpression join : joins.getJoins()) {
                if (join.getJoinTable().getMainTableClass() == c) {
                    for (FieldMetaData notIgnoreProperty : metaData.getNotIgnoreAndNavigateFields()) {
                        expressions.add(factory.column(notIgnoreProperty, join.getTableRefExpression()));
                    }
                    break;
                }
            }
        }
        else {
            GOTO:
            for (FieldMetaData sel : metaData.getNotIgnoreAndNavigateFields()) {
                MetaData mainTableMetaData = config.getMetaData(from.getSqlTableExpression().getMainTableClass());
                for (FieldMetaData noi : mainTableMetaData.getNotIgnoreAndNavigateFields()) {
                    if (noi.getColumn().equals(sel.getColumn()) && noi.getType().equals(sel.getType())) {
                        expressions.add(factory.column(sel, from.getTableRefExpression()));
                        break GOTO;
                    }
                }
                for (ISqlJoinExpression join : joins.getJoins()) {
                    MetaData joinTableMetaData = config.getMetaData(join.getJoinTable().getMainTableClass());
                    for (FieldMetaData noi : joinTableMetaData.getNotIgnoreAndNavigateFields()) {
                        if (noi.getColumn().equals(sel.getColumn()) && noi.getType().equals(sel.getType())) {
                            expressions.add(factory.column(sel, join.getTableRefExpression()));
                            break GOTO;
                        }
                    }
                }
            }
        }
        queryable.setSelect(factory.select(expressions, c));
    }

    public void setLimit(long offset, long rows) {
        queryable.setLimit(offset, rows);
    }

    public void setDistinct(boolean distinct) {
        queryable.setDistinct(distinct);
    }

    @Override
    public IConfig getConfig() {
        return config;
    }

    public List<FieldMetaData> getMappingData() {
        return queryable.getMappingData(config);
    }

    public boolean isSingle() {
        return queryable.getSelect().isSingle();
    }

    public <T> Class<T> getTargetClass() {
        return (Class<T>) queryable.getMainTableClass();
    }

    public ISqlQueryableExpression getQueryable() {
        return queryable;
    }

    public void setQueryable(ISqlQueryableExpression queryable) {
        this.queryable = queryable;
    }

//    public List<IncludeSet> getIncludeSets() {
//        return includeSets;
//    }

//    public IncludeSet getLastIncludeSet() {
//        return includeSets.get(includeSets.size() - 1);
//    }

    @Override
    public void setIgnoreFilterAll(boolean ignoreFilterAll) {
        this.ignoreFilterAll = ignoreFilterAll;
    }

    @Override
    public ISqlFromExpression getForm() {
        return queryable.getFrom();
    }

    @Override
    public ISqlJoinsExpression getJoins() {
        return queryable.getJoins();
    }

    @Override
    public List<String> getIgnoreFilterIds() {
        return ignoreFilterIds;
    }

    @Override
    public void addIgnoreFilterId(String filterId) {
        ignoreFilterIds.add(filterId);
    }

    @Override
    public boolean isIgnoreFilterAll() {
        return ignoreFilterAll;
    }

    public void boxed() {
        ISqlTableRefExpression tableRef = queryable.getFrom().getTableRefExpression();
        queryable = factory.queryable(queryable, factory.tableRef(tableRef.getName()));
    }

    public QuerySqlBuilder getCopy() {
        QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(config, queryable.copy(config));
        querySqlBuilder.includes.addAll(includes);
        querySqlBuilder.ignoreFilterAll = ignoreFilterAll;
        querySqlBuilder.ignoreFilterIds.addAll(ignoreFilterIds);
        return querySqlBuilder;
    }

    @Override
    public ISqlExpression getSqlExpression() {
        return queryable;
    }

    public DeleteSqlBuilder toDeleteSqlBuilder() {
        MetaData metaData = config.getMetaData(queryable.getMainTableClass());
        FieldMetaData primary = metaData.getPrimary();
        ISqlDeleteExpression delete = factory.delete(queryable.getMainTableClass(), factory.tableRef(queryable.getFrom().getTableRefExpression().getName()));
        ISqlQueryableExpression copy = queryable.copy(config);
        ISqlTableRefExpression copyTableRef = copy.getFrom().getTableRefExpression();
        // 某些数据库不支持 a.xx in (select b.xx from b), 所以需要在外边包一层 a.xx in (select b.xx from (select * from b))
        ISqlSelectExpression select = factory.select(Collections.singletonList(factory.column(primary, copyTableRef)), primary.getType());
        ISqlQueryableExpression warpQuery = factory.queryable(select, factory.from(copy, copyTableRef));
        delete.addWhere(factory.binary(SqlOperator.IN, factory.column(primary, delete.getFrom().getTableRefExpression()), warpQuery));
        return new DeleteSqlBuilder(config, delete);
    }

    public UpdateSqlBuilder toUpdateSqlBuilder() {
        MetaData metaData = config.getMetaData(queryable.getMainTableClass());
        FieldMetaData primary = metaData.getPrimary();
        ISqlUpdateExpression update = factory.update(queryable.getMainTableClass(), factory.tableRef(queryable.getFrom().getTableRefExpression().getName()));
        ISqlQueryableExpression copy = queryable.copy(getConfig());
        ISqlTableRefExpression copyTableRef = copy.getFrom().getTableRefExpression();
        ISqlSelectExpression select = factory.select(Collections.singletonList(factory.column(primary, copyTableRef)), copy.getMainTableClass());
        // 某些数据库不支持 a.xx in (select b.xx from b), 所以需要在外边包一层 a.xx in (select b.xx from (select * from b))
        ISqlQueryableExpression warpQuery = factory.queryable(select, factory.from(copy, copyTableRef));
        update.addWhere(factory.binary(SqlOperator.IN, factory.column(primary, update.getFrom().getTableRefExpression()), warpQuery));
        return new UpdateSqlBuilder(getConfig(), update);
    }
}
