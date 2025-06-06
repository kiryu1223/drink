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
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.util.DrinkUtil;

import java.util.*;

/**
 * 表达式工厂,所有表达式都应该从工厂创建
 *
 * @author kiryu1223
 * @since 3.0
 */
public interface SqlExpressionFactory {

    IConfig getConfig();

    /**
     * 创建别名表达式
     *
     * @param expression 表达式
     * @param asName     别名
     */
    ISqlAsExpression as(ISqlExpression expression, String asName);

    /**
     * 创建列表达式
     *
     * @param fieldMetaData 字段元数据
     * @param tableRefExpression   表别名
     */
    ISqlColumnExpression column(FieldMetaData fieldMetaData, ISqlTableRefExpression tableRefExpression);

    /**
     * 创建条件表达式
     */
    ISqlConditionsExpression condition(boolean and);

    default ISqlConditionsExpression condition() {
        return condition(true);
    }

    default ISqlConditionsExpression condition(Collection<ISqlExpression> expressions) {
        return condition(expressions,true);
    }

    default ISqlConditionsExpression condition(Collection<ISqlExpression> expressions,boolean and) {
        ISqlConditionsExpression conditions = condition();
        for (ISqlExpression expression : expressions) {
            conditions.addCondition(expression);
        }
        return conditions;
    }

    /**
     * 创建from表达式
     *
     * @param sqlTable 表表达式
     * @param tableRefExpression   表别名
     */
    ISqlFromExpression from(ISqlTableExpression sqlTable, ISqlTableRefExpression tableRefExpression);

    /**
     * 创建分组group by表达式
     */
    ISqlGroupByExpression groupBy(LinkedHashMap<String, ISqlExpression> columns);

    default ISqlGroupByExpression groupBy() {
        return groupBy(new LinkedHashMap<>());
    }

    /**
     * 创建having表达式
     */
    ISqlHavingExpression having(ISqlConditionsExpression conditionsExpression);

    default ISqlHavingExpression having() {
        return having(condition());
    }

    /**
     * 创建join表达式
     *
     * @param joinType   join类型
     * @param joinTable  join表
     * @param asName     join别名
     */
    ISqlJoinExpression join(JoinType joinType, ISqlTableExpression joinTable, ISqlConditionsExpression conditions, ISqlTableRefExpression asName);

    default ISqlJoinExpression join(JoinType joinType, ISqlTableExpression joinTable, ISqlTableRefExpression tableRefExpression) {
        return join(joinType, joinTable, condition(), tableRefExpression);
    }

    default ISqlJoinExpression join(JoinType joinType, Class<?> joinTable) {
        return join(joinType, table(joinTable), tableRef(joinTable));
    }

    default ISqlJoinExpression join(JoinType joinType, ISqlQueryableExpression queryable) {
        return join(joinType, queryable, tableRef(queryable.getFrom().getTableRefExpression().getName()));
    }

    /**
     * 创建join集合表达式
     */
    ISqlJoinsExpression Joins();

    /**
     * 创建limit表达式
     */
    ISqlLimitExpression limit();

    /**
     * 创建limit表达式
     *
     * @param offset 偏移量
     * @param rows   行数
     */
    default ISqlLimitExpression limit(long offset, long rows) {
        ISqlLimitExpression limit = limit();
        limit.setOffset(offset);
        limit.setRows(rows);
        return limit;
    }

    /**
     * 创建order by表达式
     */
    ISqlOrderByExpression orderBy();

    /**
     * 创建order表达式
     *
     * @param expression 表达式
     */
    default ISqlOrderExpression order(ISqlExpression expression) {
        return order(expression, true);
    }

    /**
     * 创建order表达式
     *
     * @param expression 表达式
     * @param asc        是否为升序
     */
    ISqlOrderExpression order(ISqlExpression expression, boolean asc);

    /**
     * 创建查询表达式
     *
     * @param target 目标表
     */
    default ISqlQueryableExpression queryable(Class<?> target, ISqlTableRefExpression tableRefExpression) {
        return queryable(from(table(target), tableRefExpression));
    }

    default ISqlQueryableExpression queryable(Class<?> target) {
        return queryable(from(table(target), tableRef(target)));
    }

    default ISqlQueryableExpression queryable(ISqlQueryableExpression queryable) {
        return queryable(from(queryable, tableRef(queryable.getFrom().getTableRefExpression().getName())));
    }

    default ISqlQueryableExpression queryable(ISqlWithExpression with) {
        return queryable(from(with, tableRef(with.withTableName())));
    }

    /**
     * 创建查询表达式
     *
     * @param from from表达式
     */
    default ISqlQueryableExpression queryable(ISqlFromExpression from) {
        return queryable(select(from.getSqlTableExpression().getMainTableClass(), from.getTableRefExpression()), from, Joins(), where(), groupBy(), having(), orderBy(), limit());
    }

    /**
     * 创建查询表达式
     *
     * @param select select表达式
     * @param from   from表达式
     */
    default ISqlQueryableExpression queryable(ISqlSelectExpression select, ISqlFromExpression from) {
        return queryable(select, from,   Joins(), where(), groupBy(), having(), orderBy(), limit());
    }

    /**
     * 创建查询表达式
     *
     * @param table 表表达式
     */
    default ISqlQueryableExpression queryable(ISqlTableExpression table, ISqlTableRefExpression tableRefExpression) {
        return queryable(from(table, tableRefExpression));
    }

    /**
     * 创建查询表达式
     *
     * @param select  select表达式
     * @param from    from表达式
     * @param joins   join表达式集合
     * @param where   where表达式
     * @param groupBy 组表达式
     * @param having  having表达式
     * @param orderBy 排序表达式
     * @param limit   limit表达式
     */
    ISqlQueryableExpression queryable(ISqlSelectExpression select, ISqlFromExpression from, ISqlJoinsExpression joins, ISqlWhereExpression where, ISqlGroupByExpression groupBy, ISqlHavingExpression having, ISqlOrderByExpression orderBy, ISqlLimitExpression limit);

    /**
     * 创建表表达式
     *
     * @param tableClass 实体表类型
     */
    ISqlRealTableExpression table(Class<?> tableClass);

    /**
     * 创建select表达式
     *
     * @param target 目标类
     */
    default ISqlSelectExpression select(Class<?> target, ISqlTableRefExpression tableRefExpression) {
        return select(getColumnByClass(target, tableRefExpression), target, false, false);
    }

    /**
     * 创建select表达式
     *
     * @param column 选择的列
     * @param target 目标类
     */
    default ISqlSelectExpression select(List<ISqlExpression> column, Class<?> target) {
        return select(column, target, false, false);
    }

    default ISqlSelectExpression singleSelect(ISqlExpression column, Class<?> target) {
        return select(Collections.singletonList(column), target, true, false);
    }

    /**
     * 创建select表达式
     *
     * @param column     选择的列
     * @param target     目标类
     * @param isSingle   是否为单列查询
     * @param isDistinct 是否为去重查询
     */
    ISqlSelectExpression select(List<ISqlExpression> column, Class<?> target, boolean isSingle, boolean isDistinct);

    /**
     * 创建where表达式
     */
    default ISqlWhereExpression where() {
        return where(condition());
    }

    /**
     * 创建where表达式
     *
     * @param conditions 条件表达式
     */
    ISqlWhereExpression where(ISqlConditionsExpression conditions);

    /**
     * 创建set表达式
     *
     * @param column 需要set的列
     * @param value  需要set的值
     */
    ISqlSetExpression set(ISqlColumnExpression column, ISqlExpression value);

    /**
     * 创建set集合表达式
     */
    ISqlSetsExpression sets();

    /**
     * 创建值表达式
     *
     * @param value 值或值集合
     */
    default ISqlValueExpression AnyValue(Object value) {
        // 如果是集合就返回集合值表达式
        if (value instanceof Collection<?>) {
            return value((Collection<?>) value);
        }
        // 否则就返回单个值表达式
        else {
            return value(value);
        }
    }

    /**
     * 创建值表达式
     *
     * @param value 值
     */
    ISqlSingleValueExpression value(Object value);

    default ISqlSingleValueExpression value() {
        return value((Object) null);
    }

    /**
     * 创建集合值表达式
     *
     * @param value 值集合
     */
    ISqlCollectedValueExpression value(Collection<?> value);

    /**
     * 创建模板表达式
     *
     * @param templates   字符串模板列表
     * @param expressions 表达式列表
     */
    ISqlTemplateExpression template(List<String> templates, List<? extends ISqlExpression> expressions);

    /**
     * 创建二元表达式
     *
     * @param operator SQL运算符
     * @param left     左表达式
     * @param right    右表达式
     */
    ISqlBinaryExpression binary(SqlOperator operator, ISqlExpression left, ISqlExpression right);

    /**
     * 创建一元表达式
     *
     * @param operator   SQL运算符
     * @param expression 表达式
     */
    ISqlUnaryExpression unary(SqlOperator operator, ISqlExpression expression);

    /**
     * 创建括号表达式
     *
     * @param expression 表达式
     */
    ISqlParensExpression parens(ISqlExpression expression);

    /**
     * 创建常量字符串表达式
     *
     * @param s 字符串
     */
    ISqlConstStringExpression constString(String s);

    default ISqlConstStringExpression constString(Object o) {
        return constString(String.valueOf(o));
    }

    /**
     * 创建类型表达式
     *
     * @param c 类型
     */
    ISqlTypeCastExpression typeCast(Class<?> c, ISqlExpression expression);

    ISqlWithExpression with(ISqlQueryableExpression queryable, String name);

    ISqlUnionQueryableExpression unionQueryable(List<ISqlQueryableExpression> queryable, List<Boolean> unions);

    default ISqlUnionQueryableExpression unionQueryable(List<ISqlQueryableExpression> queryable, boolean all) {
        return unionQueryable(queryable, Collections.nCopies(queryable.size() - 1, all));
    }

    ISqlRecursionExpression recursion(ISqlQueryableExpression queryable, FieldMetaData parentId, FieldMetaData childId, int level);

    ISqlUpdateExpression update(ISqlFromExpression from, ISqlJoinsExpression joins, ISqlSetsExpression sets, ISqlWhereExpression where);

    default ISqlUpdateExpression update(Class<?> target, ISqlTableRefExpression tableRefExpression) {
        return update(from(table(target), tableRefExpression), Joins(), sets(), where());
    }

    default ISqlUpdateExpression update(Class<?> target) {
        return update(target, tableRef(target));
    }

    ISqlDeleteExpression delete(ISqlFromExpression from, ISqlJoinsExpression joins, ISqlWhereExpression where);

    default ISqlDeleteExpression delete(Class<?> target, ISqlTableRefExpression tableRefExpression) {
        return delete(from(table(target), tableRefExpression), Joins(), where());
    }

    default ISqlDeleteExpression delete(Class<?> target) {
        return delete(target, tableRef(target));
    }

    ISqlDynamicColumnExpression dynamicColumn(String column, Class<?> type, ISqlTableRefExpression tableISqlTableRefExpression);

    /**
     * 将实体类转换为列表达式集合
     */
    default List<ISqlExpression> getColumnByClass(Class<?> target, ISqlTableRefExpression tableRefExpression) {
        MetaData metaData = getConfig().getMetaData(target);
        List<FieldMetaData> fieldMetaDataList = metaData.getNotIgnoreAndNavigateFields();
        List<ISqlExpression> columns = new ArrayList<>(fieldMetaDataList.size());
        for (FieldMetaData data : fieldMetaDataList) {
            columns.add(as(column(data, tableRefExpression), data.getFieldName()));
        }
        return columns;
    }

    ISqlTableRefExpression tableRef(String name);

    default ISqlTableRefExpression tableRef(Class<?> c) {
        return tableRef(DrinkUtil.getFirst(getConfig(), c));
    }

    ISqlStarExpression star(ISqlTableRefExpression tableRefExpression);

    default ISqlStarExpression star() {
        return star(null);
    }

    ISqlPivotExpression pivot(ISqlExpression aggregationColumn, ISqlColumnExpression groupColumn, List<ISqlExpression> selectColumnValues, ISqlTableRefExpression tableRefExpression);

    default ISqlPivotExpression pivot(ISqlExpression aggregationColumn, ISqlColumnExpression groupColumn, List<ISqlExpression> selectColumnValues) {
        return pivot(aggregationColumn, groupColumn, selectColumnValues, tableRef("_pivot"));
    }
}
