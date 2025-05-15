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
package io.github.kiryu1223.drink.core.api.crud.read;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.annotation.RelationType;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.metaData.*;
import io.github.kiryu1223.drink.base.session.SqlSession;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.toBean.Include.IncludeFactory;
import io.github.kiryu1223.drink.base.toBean.Include.IncludeSet;
import io.github.kiryu1223.drink.base.toBean.build.ObjectBuilder;
import io.github.kiryu1223.drink.base.transform.Transformer;
import io.github.kiryu1223.drink.core.api.crud.CRUD;
import io.github.kiryu1223.drink.core.exception.SqLinkException;
import io.github.kiryu1223.drink.core.page.PagedResult;
import io.github.kiryu1223.drink.core.page.Pager;
import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;
import io.github.kiryu1223.drink.core.visitor.SqlVisitor;
import io.github.kiryu1223.expressionTree.expressions.LambdaExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;


/**
 * @author kiryu1223
 * @since 3.0
 */
public abstract class QueryBase<C, R> extends CRUD<C> {
    public final static Logger log = LoggerFactory.getLogger(QueryBase.class);

    private final QuerySqlBuilder sqlBuilder;

    public QueryBase(QuerySqlBuilder sqlBuilder) {
        this.sqlBuilder = sqlBuilder;
    }

    protected QuerySqlBuilder getSqlBuilder() {
        return sqlBuilder;
    }

    protected void withTempQuery() {
        sqlBuilder.boxed();
    }

    protected IConfig getConfig() {
        return sqlBuilder.getConfig();
    }

    // region [any]

    /**
     * 检查表中是否存在至少一条数据
     */
    public boolean any() {
        return any(null);
    }

    /**
     * 检查表中是否存在至少一条数据<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     */
    protected boolean any(LambdaExpression<?> lambda) {
        IConfig config = getConfig();
        //获取拷贝的查询对象
        ISqlQueryableExpression queryableCopy = getSqlBuilder().getQueryable().copy(config);
        QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(config, queryableCopy);
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        // SELECT 1
        querySqlBuilder.setSelect(factory.select(Collections.singletonList(factory.constString("1")), int.class));
        // LIMIT 1
        querySqlBuilder.setLimit(0, 1);
        // WHERE ...
        if (lambda != null) {
            SqlVisitor sqlVisitor = new SqlVisitor(config, queryableCopy);
            ISqlExpression cond = sqlVisitor.visit(lambda);
            querySqlBuilder.addWhere(cond);
        }
        //查询
        SqlSession session = config.getSqlSessionFactory().getSession(config);
        List<SqlValue> values = new ArrayList<>();
        String sql = querySqlBuilder.getSqlAndValue(values);
        tryPrintSql(log, sql);
        return session.executeQuery(rs -> rs.next(), sql, values);
    }

    // endregion

    // region [toList]

    protected List<R> toList() {
        IConfig config = getConfig();
        Class<R> targetClass = sqlBuilder.getTargetClass();
        List<SqlValue> values = new ArrayList<>();
        String sql = sqlBuilder.getSqlAndValue(values);
        boolean single = sqlBuilder.isSingle();
        List<FieldMetaData> mappingData = single ? Collections.emptyList() : sqlBuilder.getMappingData();
        tryPrintSql(log, sql);
        SqlSession session = config.getSqlSessionFactory().getSession(config);
        List<R> ts = session.executeQuery(
                r -> ObjectBuilder.start(r, targetClass, mappingData, single, config).createList(),
                sql,
                values
        );
        if (!sqlBuilder.getIncludeSets().isEmpty()) {
            try {
                IncludeFactory includeFactory = config.getIncludeFactory();
                includeFactory.getBuilder(getConfig(), session, targetClass, ts, sqlBuilder.getIncludeSets(), sqlBuilder.getQueryable()).include();
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return ts;
    }

    // endregion

    // region [first]

    protected R first() {
        LQuery<R> lQuery = new LQuery<>(getSqlBuilder().getCopy());
        List<R> list = lQuery.limit(1).toList();
        return list.isEmpty() ? null : list.get(0);
    }

    // endregion

    // region [distinct]

    public final C distinct(boolean condition) {
        sqlBuilder.setDistinct(condition);
        return (C) this;
    }

    public final C distinct() {
        return distinct(true);
    }

    // endregion

    // region [select]

    /**
     * 设置select，根据指定的类型的字段匹配去生成选择的sql字段
     *
     * @param r   指定的返回类型
     * @return 终结查询过程
     */
    protected <N> EndQuery<N> select(Class<N> r) {
        select0(r);
        return new EndQuery<>(getSqlBuilder());
    }

    protected boolean select(LambdaExpression<?> lambda) {
        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), sqlBuilder.getQueryable());
        ISqlSelectExpression select = sqlVisitor.toSelect(lambda);
        sqlBuilder.setSelect(select);
        return sqlBuilder.isSingle();
    }

    protected void select0(Class<?> c) {
        sqlBuilder.setSelect(c);
    }

    // endregion

    // region [selectMany]

    protected QuerySqlBuilder toMany(LambdaExpression<?> tree) {
        ISqlQueryableExpression queryable = sqlBuilder.getQueryable();
        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), queryable);
        ISqlColumnExpression column = sqlVisitor.toColumn(tree);
        FieldMetaData fieldMetaData = column.getFieldMetaData();
        if (!fieldMetaData.hasNavigate()) {
            throw new DrinkException(String.format("%s字段需要被@Navigate修饰", fieldMetaData.getField()));
        }
        NavigateData navigateData = fieldMetaData.getNavigateData();
        relationTypeCheck(navigateData);
        RelationType relationType = navigateData.getRelationType();
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        ISqlQueryableExpression boxedQueryable;
        Class<?> targetType = navigateData.getNavigateTargetType();
        FieldMetaData target = MetaDataCache.getMetaData(targetType).getFieldMetaDataByFieldName(navigateData.getTargetFieldName());
        FieldMetaData self = MetaDataCache.getMetaData(queryable.getMainTableClass()).getFieldMetaDataByFieldName(navigateData.getSelfFieldName());
        if (relationType == RelationType.OneToMany) {
            // SELECT s.* FROM selfTable s ...
            ISqlQueryableExpression copy = queryable.copy(getConfig());
            // SELECT s.selfField FROM selfTable ...
            copy.setSelect(factory.select(Collections.singletonList(factory.column(self, copy.getFrom().getTableRefExpression())), self.getType()));
            // SELECT t.* FROM targetTable t
            boxedQueryable = factory.queryable(targetType);
            ISqlTableRefExpression boxedTableRef = boxedQueryable.getFrom().getTableRefExpression();
            // SELECT t.* FROM targetTable t WHERE t.targetField IN (SELECT s.selfField FROM selfTable ...)
            boxedQueryable.addWhere(factory.binary(SqlOperator.IN, factory.column(target, boxedTableRef), copy));
        }
        else if (relationType == RelationType.ManyToMany) {
            Class<? extends IMappingTable> mappingType = navigateData.getMappingTableType();
            String selfMappingName = navigateData.getSelfMappingFieldName();
            String targetMappingName = navigateData.getTargetMappingFieldName();
            MetaData mappingData = MetaDataCache.getMetaData(mappingType);
            FieldMetaData selfMapping = mappingData.getFieldMetaDataByFieldName(selfMappingName);
            FieldMetaData targetMapping = mappingData.getFieldMetaDataByFieldName(targetMappingName);
            // SELECT s.* FROM selfTable s ...
            ISqlQueryableExpression copy = queryable.copy(getConfig());
            // SELECT s.selfField FROM selfTable ...
            copy.setSelect(factory.select(Collections.singletonList(factory.column(self, copy.getFrom().getTableRefExpression())), self.getType()));
            // SELECT m.targetMappingField FROM mappingTable m WHERE m.selfMappingField IN (SELECT s.selfField FROM selfTable ...)
            ISqlQueryableExpression mappingQuery = factory.queryable(mappingType);
            ISqlTableRefExpression mappingAsName = mappingQuery.getFrom().getTableRefExpression();
            mappingQuery.addWhere(factory.binary(SqlOperator.IN, factory.column(selfMapping, mappingAsName), copy));
            mappingQuery.setSelect(factory.select(Collections.singletonList(factory.column(targetMapping, mappingAsName)), targetMapping.getType()));
            // SELECT t.*
            // FROM targetTable t
            // WHERE t.targetField IN
            // (SELECT m.targetMappingField FROM mappingTable m WHERE m.selfMappingField IN (SELECT s.selfField FROM selfTable ...))
            boxedQueryable = factory.queryable(targetType);
            ISqlTableRefExpression asName = boxedQueryable.getFrom().getTableRefExpression();
            boxedQueryable.addWhere(factory.binary(SqlOperator.IN, factory.column(target, asName), mappingQuery));
        }
        else {
            throw new DrinkException(relationType.name());
        }
        return new QuerySqlBuilder(getConfig(), boxedQueryable);
    }

    //endregion

    // region [join]

    protected void join(JoinType joinType, Class<?> target, LambdaExpression<?> lambda) {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), sqlBuilder.getQueryable());
        ISqlExpression on = sqlVisitor.visit(lambda);
        ISqlTableExpression table = factory.table(target);
        sqlBuilder.addJoin(joinType, table, factory.condition(Collections.singletonList(on)));
    }

    protected void join(JoinType joinType, QueryBase<?, ?> target, LambdaExpression<?> lambda) {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), sqlBuilder.getQueryable());
        ISqlExpression on = sqlVisitor.visit(lambda);
        ISqlTableExpression table = target.getSqlBuilder().getQueryable();
        sqlBuilder.addJoin(joinType, table, factory.condition(Collections.singletonList(on)));
    }

    //    protected void joinWith(JoinType joinType, QueryBase<?,?> target, LambdaExpression<?> lambda) {
//        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
//        ISqlQueryableExpression queryable = sqlBuilder.getQueryable();
//        Set<String> stringSet = new HashSet<>(queryable.getJoins().getJoins().size() + 1);
//        stringSet.add(queryable.getFrom().getAsName().getName());
//        for (ISqlJoinExpression join : queryable.getJoins().getJoins()) {
//            stringSet.add(join.getAsName().getName());
//        }
//        String first = getFirst(target.getSqlBuilder().getTargetClass());
//        AsName asName = doGetAsName(first, stringSet);
//        MetaData metaData = MetaDataCache.getMetaData(target.getSqlBuilder().getTargetClass());
//        ISqlJoinExpression join = factory.join(joinType, factory.with(target.getSqlBuilder().getQueryable(), metaData.getTableName()), asName);
//        queryable.addJoin(join);
//        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), queryable);
//        ISqlExpression cond = sqlVisitor.visit(lambda);
//        join.addConditions(cond);
//    }

    // endregion

    // region [where]

    protected void where(LambdaExpression<?> lambda) {
        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), sqlBuilder.getQueryable());
        ISqlExpression where = sqlVisitor.visit(lambda);
        sqlBuilder.addWhere(where);
    }

    protected void orWhere(LambdaExpression<?> lambda) {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), sqlBuilder.getQueryable());
        ISqlExpression where = sqlVisitor.visit(lambda);
        sqlBuilder.addOrWhere(where);
    }

//    protected void exists(Class<?> table, LambdaExpression<?> lambda, boolean not) {
//        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
//        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(),sqlBuilder.getQueryable());
//        ISqlExpression where = sqlVisitor.visit(lambda);
//        int offset = sqlBuilder.getQueryable().getOrderedCount();
//        QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(getConfig(), factory.queryable(factory.from(factory.table(table), offset)));
//        querySqlBuilder.setSelect(factory.select(Collections.singletonList(factory.constString("1")), table, true, false));
//        querySqlBuilder.addWhere(where);
//        ISqlUnaryExpression exists = factory.unary(SqlOperator.EXISTS, querySqlBuilder.getQueryable());
//        if (not) {
//            sqlBuilder.addWhere(factory.unary(SqlOperator.NOT, exists));
//        }
//        else {
//            sqlBuilder.addWhere(exists);
//        }
//    }
//
//    protected void exists(QueryBase queryBase, LambdaExpression<?> lambda, boolean not) {
//        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
//        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(),sqlBuilder.getQueryable());
//        ISqlExpression where = sqlVisitor.visit(lambda);
//        ISqlQueryableExpression queryable = queryBase.getSqlBuilder().getQueryable();
//        int offset = sqlBuilder.getQueryable().getOrderedCount();
//        QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(getConfig(), factory.queryable(factory.from(queryable, offset)));
//        querySqlBuilder.setSelect(factory.select(Collections.singletonList(factory.constString("1")), queryable.getTableClass(), true, false));
//        querySqlBuilder.addWhere(where);
//        ISqlUnaryExpression exists = factory.unary(SqlOperator.EXISTS, querySqlBuilder.getQueryable());
//        if (not) {
//            sqlBuilder.addWhere(factory.unary(SqlOperator.NOT, exists));
//        }
//        else {
//            sqlBuilder.addWhere(exists);
//        }
//    }

    // endregion

    // region [groupBy]

    protected void groupBy(LambdaExpression<?> lambda) {
        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), sqlBuilder.getQueryable());
        ISqlGroupByExpression group = sqlVisitor.toGroup(lambda);
        sqlBuilder.setGroup(group);
        // 同时设置select以便直接返回grouper对象
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        LinkedHashMap<String, ISqlExpression> columns = group.getColumns();
        List<ISqlExpression> values = new ArrayList<>(columns.size());
        // 手动设置别名
        for (Map.Entry<String, ISqlExpression> entry : columns.entrySet()) {
            values.add(factory.as(entry.getValue(), entry.getKey()));
        }
        sqlBuilder.setSelect(factory.select(values, lambda.getReturnType()));
    }

    // endregion

    // region [having]

    protected void having(LambdaExpression<?> lambda) {
        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), sqlBuilder.getQueryable());
        ISqlExpression expression = sqlVisitor.visit(lambda);
        sqlBuilder.addHaving(expression);
    }

    // endregion

    // region [orderBy]

    protected void orderBy(LambdaExpression<?> lambda, boolean asc) {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), sqlBuilder.getQueryable());
        ISqlExpression expression = sqlVisitor.visit(lambda);
        sqlBuilder.addOrder(factory.order(expression, asc));
    }

    // endregion

    // region [limit]

    public final C limit(long rows) {
        return limit(0, rows);
    }

    public final C limit(long offset, long rows) {
        sqlBuilder.setLimit(offset, rows);
        return (C) this;
    }

    // endregion

    // region [toSql]

    public String toSql() {
        return sqlBuilder.getSql();
    }

    // endregion

    protected void singleCheck(boolean single) {
        if (single) {
            throw new RuntimeException("query.select(Func<T1,T2..., R> expr) 不允许传入单个元素, 单元素请使用endSelect");
        }
    }

    // region [include]

    protected void include(LambdaExpression<?> lambda, ISqlExpression cond, List<IncludeSet> includeSets) {
        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), sqlBuilder.getQueryable());
        ISqlColumnExpression columnExpression = sqlVisitor.toColumn(lambda);
        if (!columnExpression.getFieldMetaData().hasNavigate()) {
            throw new RuntimeException("include指定的字段需要被@Navigate修饰");
        }
        relationTypeCheck(columnExpression.getFieldMetaData().getNavigateData());
        IncludeSet includeSet;
        if (cond != null) {
            SqlVisitor coVisitor = new SqlVisitor(getConfig(), sqlBuilder.getQueryable());
            //ISqlExpression condition = coVisitor.visit(cond);
            includeSet = new IncludeSet(columnExpression, cond);
        }
        else {
            includeSet = new IncludeSet(columnExpression);
        }
        includeSets.add(includeSet);
    }

    protected void include(LambdaExpression<?> lambda, ISqlExpression cond) {
        include(lambda, cond, sqlBuilder.getIncludeSets());
    }

    protected void include(LambdaExpression<?> lambda) {
        include(lambda, null, sqlBuilder.getIncludeSets());
    }

    // endregion

    protected void relationTypeCheck(NavigateData navigateData) {
        RelationType relationType = navigateData.getRelationType();
        switch (relationType) {
            case OneToOne:
            case ManyToOne:
                if (navigateData.isCollectionWrapper()) {
                    throw new SqLinkException(relationType + "不支持集合");
                }
                break;
            case OneToMany:
                if (!navigateData.isCollectionWrapper()) {
                    if (!(List.class.isAssignableFrom(navigateData.getCollectionWrapperType()) || Set.class.isAssignableFrom(navigateData.getCollectionWrapperType()))) {
                        throw new SqLinkException(relationType + "只支持List和Set");
                    }
                    throw new SqLinkException(relationType + "只支持集合");
                }
                break;
            case ManyToMany:
                if (navigateData.getMappingTableType() == IMappingTable.class
                    || navigateData.getSelfMappingFieldName().isEmpty()
                    || navigateData.getTargetMappingFieldName().isEmpty()) {
                    throw new SqLinkException(relationType + "下@Navigate注解的midTable和SelfMapping和TargetMapping字段都不能为空");
                }
                if (!navigateData.isCollectionWrapper()) {
                    if (!(List.class.isAssignableFrom(navigateData.getCollectionWrapperType()) || Set.class.isAssignableFrom(navigateData.getCollectionWrapperType()))) {
                        throw new SqLinkException(relationType + "只支持List和Set");
                    }
                    throw new RuntimeException(relationType + "只支持集合");
                }
                break;
        }
    }

    // region [page]

    protected <T> PagedResult<T> toPagedResult0(long pageIndex, long pageSize, Pager pager) {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        QuerySqlBuilder boxedQuerySqlBuilder = sqlBuilder.getCopy();
        boxedQuerySqlBuilder.boxed();
        Transformer transformer = getConfig().getTransformer();
        //SELECT COUNT(*) ...
        boxedQuerySqlBuilder.setSelect(factory.select(Collections.singletonList(transformer.count()), long.class, true, false));
        LQuery<Long> countQuery = new LQuery<>(boxedQuerySqlBuilder);
        long total = countQuery.toList().get(0);
        QuerySqlBuilder dataQuerySqlBuilder = new QuerySqlBuilder(getConfig(), getSqlBuilder().getQueryable().copy(getConfig()));
        //拷贝Include
        dataQuerySqlBuilder.getIncludeSets().addAll(dataQuerySqlBuilder.getIncludeSets());
        LQuery<T> dataQuery = new LQuery<>(dataQuerySqlBuilder);
        long take = Math.max(pageSize, 1);
        long index = Math.max(pageIndex, 1);
        long offset = (index - 1) * take;
        List<T> list = dataQuery.limit(offset, take).toList();
        return pager.getPagedResult(total, list);
    }

    // endregion

    // region [agg]

    protected long count0(LambdaExpression<?> lambda) {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        Transformer transformer = getConfig().getTransformer();
        List<ISqlExpression> countList;
        if (lambda == null) {
            ISqlTemplateExpression count = transformer.count();
            countList = Collections.singletonList(count);
        }
        else {
            SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), sqlBuilder.getQueryable());
            ISqlTemplateExpression count = transformer.count(sqlVisitor.visit(lambda));
            countList = Collections.singletonList(count);
        }
        ISqlQueryableExpression copy = sqlBuilder.getQueryable().copy(getConfig());
        QuerySqlBuilder copyQuerySqlBuilder = new QuerySqlBuilder(getConfig(), copy);
        copyQuerySqlBuilder.setSelect(factory.select(countList, long.class, true, false));
        LQuery<Long> countQuery = new LQuery<>(copyQuerySqlBuilder);
        return countQuery.toList().get(0);
    }

    protected List<Long> groupByCount0(LambdaExpression<?> lambda) {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        Transformer transformer = getConfig().getTransformer();
        List<ISqlExpression> countList;
        if (lambda == null) {
            ISqlTemplateExpression count = transformer.count();
            countList = Collections.singletonList(count);
        }
        else {
            SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), sqlBuilder.getQueryable());
            ISqlTemplateExpression count = transformer.count(sqlVisitor.visit(lambda));
            countList = Collections.singletonList(count);
        }
        ISqlQueryableExpression copy = sqlBuilder.getQueryable().copy(getConfig());
        QuerySqlBuilder copyQuerySqlBuilder = new QuerySqlBuilder(getConfig(), copy);
        copyQuerySqlBuilder.setSelect(factory.select(countList, long.class, true, false));
        LQuery<Long> countQuery = new LQuery<>(copyQuerySqlBuilder);
        return countQuery.toList();
    }

    protected <T extends Number> T sum0(LambdaExpression<?> lambda) {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), sqlBuilder.getQueryable());
        Transformer transformer = getConfig().getTransformer();
        ISqlTemplateExpression sum = transformer.sum(sqlVisitor.visit(lambda));
        List<ISqlExpression> sumList = Collections.singletonList(sum);
        ISqlQueryableExpression copy = sqlBuilder.getQueryable().copy(getConfig());
        QuerySqlBuilder copyQuerySqlBuilder = new QuerySqlBuilder(getConfig(), copy);
        copyQuerySqlBuilder.setSelect(factory.select(sumList, lambda.getReturnType(), true, false));
        LQuery<T> sumQuery = new LQuery<>(copyQuerySqlBuilder);
        return sumQuery.toList().get(0);
    }

    protected <T extends Number> List<T> groupBySum0(LambdaExpression<?> lambda) {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), sqlBuilder.getQueryable());
        Transformer transformer = getConfig().getTransformer();
        ISqlTemplateExpression sum = transformer.sum(sqlVisitor.visit(lambda));
        List<ISqlExpression> sumList = Collections.singletonList(sum);
        ISqlQueryableExpression copy = sqlBuilder.getQueryable().copy(getConfig());
        QuerySqlBuilder copyQuerySqlBuilder = new QuerySqlBuilder(getConfig(), copy);
        copyQuerySqlBuilder.setSelect(factory.select(sumList, lambda.getReturnType(), true, false));
        LQuery<T> sumQuery = new LQuery<>(copyQuerySqlBuilder);
        return sumQuery.toList();
    }

    protected BigDecimal avg0(LambdaExpression<?> lambda) {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), sqlBuilder.getQueryable());
        Transformer transformer = getConfig().getTransformer();
        ISqlTemplateExpression avg = transformer.avg(sqlVisitor.visit(lambda));
        List<ISqlExpression> avgList = Collections.singletonList(avg);
        ISqlQueryableExpression copy = sqlBuilder.getQueryable().copy(getConfig());
        QuerySqlBuilder avgQuerySqlBuilder = new QuerySqlBuilder(getConfig(), copy);
        avgQuerySqlBuilder.setSelect(factory.select(avgList, BigDecimal.class, true, false));
        LQuery<BigDecimal> avgQuery = new LQuery<>(avgQuerySqlBuilder);
        return avgQuery.toList().get(0);
    }

    protected List<BigDecimal> groupByAvg0(LambdaExpression<?> lambda) {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), sqlBuilder.getQueryable());
        Transformer transformer = getConfig().getTransformer();
        ISqlTemplateExpression avg = transformer.avg(sqlVisitor.visit(lambda));
        List<ISqlExpression> avgList = Collections.singletonList(avg);
        ISqlQueryableExpression copy = sqlBuilder.getQueryable().copy(getConfig());
        QuerySqlBuilder avgQuerySqlBuilder = new QuerySqlBuilder(getConfig(), copy);
        avgQuerySqlBuilder.setSelect(factory.select(avgList, BigDecimal.class, true, false));
        LQuery<BigDecimal> avgQuery = new LQuery<>(avgQuerySqlBuilder);
        return avgQuery.toList();
    }

    protected <T extends Number> T max0(LambdaExpression<?> lambda) {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), sqlBuilder.getQueryable());
        Transformer transformer = getConfig().getTransformer();
        ISqlTemplateExpression max = transformer.max(sqlVisitor.visit(lambda));
        List<ISqlExpression> maxList = Collections.singletonList(max);
        ISqlQueryableExpression copy = sqlBuilder.getQueryable().copy(getConfig());
        QuerySqlBuilder maxQuerySqlBuilder = new QuerySqlBuilder(getConfig(), copy);
        maxQuerySqlBuilder.setSelect(factory.select(maxList, lambda.getReturnType(), true, false));
        LQuery<T> maxQuery = new LQuery<>(maxQuerySqlBuilder);
        return maxQuery.toList().get(0);
    }

    protected <T extends Number> List<T> groupByMax0(LambdaExpression<?> lambda) {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), sqlBuilder.getQueryable());
        Transformer transformer = getConfig().getTransformer();
        ISqlTemplateExpression max = transformer.max(sqlVisitor.visit(lambda));
        List<ISqlExpression> maxList = Collections.singletonList(max);
        ISqlQueryableExpression copy = sqlBuilder.getQueryable().copy(getConfig());
        QuerySqlBuilder maxQuerySqlBuilder = new QuerySqlBuilder(getConfig(), copy);
        maxQuerySqlBuilder.setSelect(factory.select(maxList, lambda.getReturnType(), true, false));
        LQuery<T> maxQuery = new LQuery<>(maxQuerySqlBuilder);
        return maxQuery.toList();
    }

    protected <T extends Number> T min0(LambdaExpression<?> lambda) {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), sqlBuilder.getQueryable());
        Transformer transformer = getConfig().getTransformer();
        ISqlTemplateExpression min = transformer.min(sqlVisitor.visit(lambda));
        List<ISqlExpression> minList = Collections.singletonList(min);
        ISqlQueryableExpression copy = sqlBuilder.getQueryable().copy(getConfig());
        QuerySqlBuilder minQuerySqlBuilder = new QuerySqlBuilder(getConfig(), copy);
        minQuerySqlBuilder.setSelect(factory.select(minList, lambda.getReturnType(), true, false));
        LQuery<T> minQuery = new LQuery<>(minQuerySqlBuilder);
        return minQuery.toList().get(0);
    }

    protected <T extends Number> List<T> groupByMin0(LambdaExpression<?> lambda) {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), sqlBuilder.getQueryable());
        Transformer transformer = getConfig().getTransformer();
        ISqlTemplateExpression min = transformer.min(sqlVisitor.visit(lambda));
        List<ISqlExpression> minList = Collections.singletonList(min);
        ISqlQueryableExpression copy = sqlBuilder.getQueryable().copy(getConfig());
        QuerySqlBuilder minQuerySqlBuilder = new QuerySqlBuilder(getConfig(), copy);
        minQuerySqlBuilder.setSelect(factory.select(minList, lambda.getReturnType(), true, false));
        LQuery<T> minQuery = new LQuery<>(minQuerySqlBuilder);
        return minQuery.toList();
    }

    // endregion
}
