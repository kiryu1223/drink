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
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.IMappingTable;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.metaData.NavigateData;
import io.github.kiryu1223.drink.base.page.PagedResult;
import io.github.kiryu1223.drink.base.session.SqlSession;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.toBean.beancreator.AbsBeanCreator;
import io.github.kiryu1223.drink.base.toBean.beancreator.IGetterCaller;
import io.github.kiryu1223.drink.base.toBean.build.JdbcResult;
import io.github.kiryu1223.drink.base.toBean.build.ObjectBuilder;
import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;
import io.github.kiryu1223.drink.base.transform.Transformer;
import io.github.kiryu1223.drink.base.util.DrinkUtil;
import io.github.kiryu1223.drink.core.api.crud.CRUD;
import io.github.kiryu1223.drink.core.exception.SqLinkException;
import io.github.kiryu1223.drink.core.sqlBuilder.IncludeBuilder;
import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;
import io.github.kiryu1223.drink.core.visitor.QuerySqlVisitor;
import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.expressions.LambdaExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.*;

import static io.github.kiryu1223.drink.base.util.DrinkUtil.cast;
import static io.github.kiryu1223.drink.core.util.ExpressionUtil.buildTree;


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

    public QuerySqlBuilder getSqlBuilder() {
        return sqlBuilder;
    }

    protected void withTempQuery() {
        sqlBuilder.boxed();
    }

    protected IConfig getConfig() {
        return sqlBuilder.getConfig();
    }

    //  region [as]

    public C as(String alisaName) {
        sqlBuilder.as(alisaName);
        return (C) this;
    }

    // endregion

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
            QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(config, queryableCopy);
            ISqlExpression cond = sqlVisitor.visit(lambda);
            querySqlBuilder.addAndOrWhere(cond, true);
        }
        //查询
        SqlSession session = config.getSqlSessionFactory().getSession();
        List<SqlValue> values = new ArrayList<>();
        String sql = querySqlBuilder.getSqlAndValue(values);
        printSql(sql);
        printValues(values);
        boolean any= session.executeQuery(rs -> rs.next(), sql, values);
        printTotal(1);
        return any;
    }

    // endregion

    protected ResultSet toResultSet(int fetchSize)
    {
        IConfig config = getConfig();
        Class<R> targetClass = sqlBuilder.getTargetClass();
        List<SqlValue> values = new ArrayList<>();
        String sql = sqlBuilder.getSqlAndValue(values);
        boolean single = sqlBuilder.isSingle();
        List<FieldMetaData> mappingData = single ? Collections.emptyList() : sqlBuilder.getMappingData();
        ITypeHandler<R> typeHandler = getSingleTypeHandler(single);
        printSql(sql);
        SqlSession session = config.getSqlSessionFactory().getSession();
        ExValues exValues = sqlBuilder.getQueryable().getSelect().getExValues();
        return session.executeQuery(sql, values, fetchSize);
    }

    // region [toList]
    protected List<R> toList() {
        IConfig config = getConfig();
        Class<R> targetClass = sqlBuilder.getTargetClass();
        List<SqlValue> values = new ArrayList<>();
        String sql = sqlBuilder.getSqlAndValue(values);
        boolean single = sqlBuilder.isSingle();
        List<FieldMetaData> mappingData = single ? Collections.emptyList() : sqlBuilder.getMappingData();
        ITypeHandler<R> typeHandler = getSingleTypeHandler(single);
        printSql(sql);
        printValues(values);
        SqlSession session = config.getSqlSessionFactory().getSession();
        ExValues exValues = sqlBuilder.getQueryable().getSelect().getExValues();
        JdbcResult<R> result = session.executeQuery(
                r -> ObjectBuilder.start(r, targetClass, mappingData, single, config, typeHandler)
                        .createList(exValues),
                sql,
                values
        );
        List<R> jdbcResult = result.getResult();
        printTotal(jdbcResult.size());

        if (!single) {
            List<IncludeBuilder> includes = sqlBuilder.getIncludes();
            if (!includes.isEmpty()) {
                try {
                    for (IncludeBuilder include : includes) {
                        include.include(session, jdbcResult);
                    }
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            List<SubQueryBuilder> subQueryBuilders = sqlBuilder.getQueryable().getSelect().getSubQueryBuilders();
            if (!subQueryBuilders.isEmpty()) {
                try {
                    for (SubQueryBuilder subQueryBuilder : subQueryBuilders) {
                        subQueryBuilder.subQuery(session, new ArrayList<>(Collections.singletonList(result)));
                    }
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return jdbcResult;
    }

    protected ITypeHandler<R> getSingleTypeHandler(boolean single) {
        ITypeHandler<R> typeHandler = null;
        if (single) {
            List<ISqlExpression> columns = sqlBuilder.getQueryable().getSelect().getColumns();
            for (ISqlExpression column : columns) {
                if (column instanceof ISqlColumnExpression) {
                    ISqlColumnExpression columnExpression = (ISqlColumnExpression) column;
                    typeHandler = (ITypeHandler<R>) columnExpression.getFieldMetaData().getTypeHandler();
                }
            }
        }
        return typeHandler;
    }

    protected List<R> toTreeList(LambdaExpression<?> lambdaExpression,Func1<R, Collection<R>> getter)
    {
        IConfig config = getConfig();
        MetaData metaData = config.getMetaData(sqlBuilder.getQueryable().getMainTableClass());
        AbsBeanCreator<R> beanCreator = config.getBeanCreatorFactory().get((Class<R>) metaData.getType());
        FieldMetaData navigateField;
        IGetterCaller<R, Collection<R>> navigateGetter;
        if (lambdaExpression == null)
        {
            navigateField = metaData.getChildrenField();
            navigateGetter = (IGetterCaller<R, Collection<R>>) beanCreator.getBeanGetter(navigateField.getFieldName());
        }
        else
        {
            QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(config, sqlBuilder.getQueryable());
            navigateField = sqlVisitor.toField(lambdaExpression);
            navigateGetter =r->getter.invoke(r);
            if (!navigateField.hasNavigate())
            {
                throw new SqLinkException("toTreeList指定的字段需要被@Navigate修饰");
            }
        }
        NavigateData navigateData = navigateField.getNavigateData();
        try
        {
            return buildTree(
                    toList(),
                    beanCreator.getBeanGetter(navigateData.getSelfFieldName()),
                    beanCreator.getBeanGetter(navigateData.getTargetFieldName()),
                    beanCreator.getBeanSetter(navigateField.getFieldName()),
                    navigateGetter
            );
        }
        catch (InvocationTargetException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    // endregion

    // region [first]

    protected R first() {
        ISqlLimitExpression limit = sqlBuilder.getQueryable().getLimit();
        long offset = limit.getOffset();
        long rows = limit.getRows();
        limit.setOffset(0);
        limit.setRows(1);
        List<R> list = toList();
        limit.setOffset(offset);
        limit.setRows(rows);
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
        Class<R> targetClass = sqlBuilder.getTargetClass();
        // 非继承类需要清空Include
        if (!targetClass.isAssignableFrom(r)) {
            sqlBuilder.getIncludes().clear();
        }
        return new EndQuery<>(sqlBuilder);
    }

    protected void select(LambdaExpression<?> lambda) {
        QuerySqlVisitor querySqlVisitor = new QuerySqlVisitor(getConfig(), sqlBuilder.getQueryable());
        ISqlSelectExpression select = querySqlVisitor.toSelect(lambda);
        sqlBuilder.setSelect(select);
//        Map<String, ISqlQueryableExpression> subQueryMap = sqlVisitor.getSubQueryMap();
//        if (!subQueryMap.isEmpty()) {
//            sqlBuilder.getSubQueryMap().putAll(subQueryMap);
//        }
    }

    protected void select0(Class<?> c) {
        sqlBuilder.setSelect(c);
    }

    // endregion

    // region [selectMany]

    protected QuerySqlBuilder toMany(LambdaExpression<?> tree) {
        ISqlQueryableExpression queryable = sqlBuilder.getQueryable();
        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), queryable);
        FieldMetaData fieldMetaData = sqlVisitor.toField(tree);
        if (!fieldMetaData.hasNavigate()) {
            throw new DrinkException(String.format("%s字段需要被@Navigate修饰", fieldMetaData.getField()));
        }
        NavigateData navigateData = fieldMetaData.getNavigateData();
        relationTypeCheck(navigateData);
        RelationType relationType = navigateData.getRelationType();
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        ISqlQueryableExpression boxedQueryable;
        Class<?> targetType = navigateData.getNavigateTargetType();
        FieldMetaData target = getConfig().getMetaData(targetType).getFieldMetaDataByFieldName(navigateData.getTargetFieldName());
        FieldMetaData self = getConfig().getMetaData(queryable.getMainTableClass()).getFieldMetaDataByFieldName(navigateData.getSelfFieldName());
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
            MetaData mappingData = getConfig().getMetaData(mappingType);
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
        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), sqlBuilder.getQueryable());
        ISqlExpression on = sqlVisitor.visit(lambda);
        ISqlTableExpression table = factory.table(target);
        sqlBuilder.addJoin(joinType, table, factory.condition(Collections.singletonList(on)));
    }

    protected void join(JoinType joinType, QueryBase<?, ?> target, LambdaExpression<?> lambda) {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), sqlBuilder.getQueryable());
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
        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), sqlBuilder.getQueryable());
        ISqlExpression where = sqlVisitor.visit(lambda);
        sqlBuilder.addAndOrWhere(where, true);
    }

    protected void orWhere(LambdaExpression<?> lambda) {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), sqlBuilder.getQueryable());
        ISqlExpression where = sqlVisitor.visit(lambda);
        sqlBuilder.addAndOrWhere(where, false);
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
        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), sqlBuilder.getQueryable());
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
        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), sqlBuilder.getQueryable());
        ISqlExpression expression = sqlVisitor.visit(lambda);
        sqlBuilder.addHaving(expression);
    }

    // endregion

    // region [orderBy]

    protected void orderBy(LambdaExpression<?> lambda, boolean asc) {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), sqlBuilder.getQueryable());
        ISqlColumnExpression expression = sqlVisitor.toColumn(lambda);
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

    // region [cte]

    protected void asTreeCte(LambdaExpression<?> lambdaExpression,int level)
    {
        QuerySqlBuilder sqlBuilder = getSqlBuilder();
        ISqlQueryableExpression queryable = sqlBuilder.getQueryable();
        FieldMetaData navigateField;

        if (lambdaExpression == null)
        {
            MetaData metaData = getConfig().getMetaData(queryable.getMainTableClass());
            navigateField = metaData.getChildrenField();
        }
        else
        {
            QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), queryable);
            navigateField = sqlVisitor.toField(lambdaExpression);
            if (!navigateField.hasNavigate())
            {
                throw new SqLinkException("asTreeCTE指定的字段需要被@Navigate修饰");
            }
        }

        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        NavigateData navigateData = navigateField.getNavigateData();
        MetaData metaData = getConfig().getMetaData(navigateField.getParentType());
        FieldMetaData parent = metaData.getFieldMetaDataByFieldName(navigateData.getTargetFieldName());
        FieldMetaData child = metaData.getFieldMetaDataByFieldName(navigateData.getSelfFieldName());
        ISqlSelectExpression select = queryable.getSelect().copy(getConfig());
        ISqlTableRefExpression tableRef = queryable.getFrom().getTableRefExpression();
        ISqlRecursionExpression recursion = factory.recursion(queryable, parent, child, level);
        ISqlQueryableExpression newQuery = factory.queryable(select, factory.from(recursion, tableRef));
        sqlBuilder.setQueryable(newQuery);
    }

    // endregion

    // region [toSql]

    public String toSql() {
        return sqlBuilder.getSql();
    }

    // endregion

    // region [include]

//    protected void include(LambdaExpression<?> lambda, ISqlExpression cond, List<IncludeSet> includeSets) {
//        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), sqlBuilder.getQueryable());
//        FieldMetaData fieldMetaData = sqlVisitor.toField(lambda);
//        if (!fieldMetaData.hasNavigate()) {
//            throw new RuntimeException("include指定的字段需要被@Navigate修饰");
//        }
//        relationTypeCheck(fieldMetaData.getNavigateData());
//        IncludeSet includeSet;
//        if (cond != null) {
//            SqlVisitor coVisitor = new SqlVisitor(getConfig(), sqlBuilder.getQueryable());
//            //ISqlExpression condition = coVisitor.visit(cond);
//            includeSet = new IncludeSet(fieldMetaData, cond);
//        }
//        else {
//            includeSet = new IncludeSet(fieldMetaData);
//        }
//        includeSets.add(includeSet);
//    }
//
//    protected void include(LambdaExpression<?> lambda, ISqlExpression cond) {
//        include(lambda, cond, sqlBuilder.getIncludeSets());
//    }

    protected void include(FieldMetaData includeField, ISqlTableRefExpression targetRef, ISqlQueryableExpression query) {
        NavigateData navigateData = includeField.getNavigateData();
        relationTypeCheck(navigateData);
        RelationType relationType = navigateData.getRelationType();
        IConfig config = getConfig();
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        FieldMetaData selfField = config.getMetaData(includeField.getParentType()).getFieldMetaDataByFieldName(navigateData.getSelfFieldName());
        FieldMetaData targetField = config.getMetaData(navigateData.getNavigateTargetType()).getFieldMetaDataByFieldName(navigateData.getTargetFieldName());
        ISqlQueryableExpression targetQuery = factory.queryable(navigateData.getNavigateTargetType(), targetRef);
        //ISqlTableRefExpression targetRef = targetQuery.getFrom().getTempRefExpression();
        // 等待后续填充
        ISqlCollectedValueExpression ids = factory.value(new ArrayList<>());
        String mappingKeyName = "<mappingKeyName>";
        // A.B
        switch (relationType) {
            // 一对一，一对多，多对一的场合
            // SELECT B.* FROM B WHERE B.targetField IN (...)
            case OneToOne:
            case ManyToOne:
            case OneToMany: {
                targetQuery.addWhere(factory.binary(SqlOperator.IN, factory.column(targetField, targetRef), ids));
            }
            break;
            // SELECT B.*, M.aid
            // FROM B
            // INNER JOIN M ON M.bid = B.id
            // INNER JOIN (SELECT A.* FROM A WHERE A.id IN (...)) ON A.id = M.aid

            // SELECT B.*, M.aid
            // FROM B
            // INNER JOIN M ON M.bid = B.id AND M.aid IN (...)
            case ManyToMany: {
                FieldMetaData selfMappingField = navigateData.getSelfMappingFieldMetaData(config);
                FieldMetaData targetMappingField = navigateData.getTargetMappingFieldMetaData(config);

//                ISqlJoinExpression mappingJoin = factory.join(JoinType.INNER, navigateData.getMappingTableType());
//                ISqlTableRefExpression mappingRef = mappingJoin.getTempRefExpression();
//                mappingJoin.addConditions(factory.binary(SqlOperator.EQ, factory.column(targetMappingField, mappingRef), factory.column(targetField, targetRef)));
//                targetQuery.addJoin(mappingJoin);
//
//                ISqlQueryableExpression selfTempQuery = factory.queryable(includeField.getParentType());
//                ISqlTableRefExpression selfRef = selfTempQuery.getFrom().getTempRefExpression();
//                selfTempQuery.addWhere(factory.binary(SqlOperator.IN, factory.column(selfField, selfRef), ids));
//
//                ISqlJoinExpression selfJoin = factory.join(JoinType.INNER, selfTempQuery);
//                ISqlTableRefExpression selfJoinRef = selfJoin.getTempRefExpression();
//                selfJoin.addConditions(factory.binary(SqlOperator.EQ, factory.column(selfField, selfJoinRef), factory.column(selfMappingField, mappingRef)));
//
//                targetQuery.addJoin(selfJoin);

                ISqlJoinExpression mappingJoin = factory.join(JoinType.INNER, navigateData.getMappingTableType());
                ISqlTableRefExpression mappingRef = mappingJoin.getTableRefExpression();
                ISqlBinaryExpression eq = factory.binary(SqlOperator.EQ, factory.column(targetMappingField, mappingRef), factory.column(targetField, targetRef));
                ISqlBinaryExpression in = factory.binary(SqlOperator.IN, factory.column(selfMappingField, mappingRef), ids);
                mappingJoin.addConditions(eq);
                mappingJoin.addConditions(in);
                targetQuery.addJoin(mappingJoin);
                targetQuery.getSelect().addColumn(factory.as(factory.column(selfMappingField, mappingRef), mappingKeyName));
            }
            break;
        }
        if (query != null) {
            ISqlWhereExpression where = query.getWhere();
            if (!where.isEmpty()) {
                targetQuery.addWhere(where.getConditions());
            }
            ISqlOrderByExpression orderBy = query.getOrderBy();
            if (!orderBy.isEmpty()) {
                for (ISqlOrderExpression order : orderBy.getSqlOrders()) {
                    targetQuery.addOrder(order);
                }
            }
            // limit需要包装成窗口函数逻辑才能正确
            // SELECT window.* FROM
            // (SELECT
            //  ...,
            //  ROW_NUMBER() OVER () AS `<rn>`
            //  ...
            //  ) as window
            // WHERE window.`<rn>` BETWEEN 1 AND 10
            ISqlLimitExpression limit = query.getLimit();
            if (limit.hasRowsOrOffset()) {
                targetQuery = warpToRowNumber(targetQuery, limit.getOffset(), limit.getRows());
            }
        }
        sqlBuilder.addSubQuery(new IncludeBuilder(config, targetQuery, ids, includeField, mappingKeyName));
    }

    private ISqlQueryableExpression warpToRowNumber(ISqlQueryableExpression sourceQuery, long offset, long rows) {
        String rowNumberAsName = "<rn>";
        IConfig config = getConfig();
        Transformer transformer = config.getTransformer();
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        ISqlTemplateExpression over = transformer.over(Collections.emptyList());
        ISqlConstStringExpression rowNumber = transformer.rowNumber();
        // ISqlTemplateExpression merged = DrinkUtil.mergeTemplates(config,factory.template(Collections.singletonList(rowNumber.getString()), Collections.emptyList()), over);
        ISqlTemplateExpression merged = factory.template(Arrays.asList(""," ",""),Arrays.asList(rowNumber,over));
        sourceQuery.getSelect().addColumn(factory.as(merged, rowNumberAsName));

        ISqlQueryableExpression warpedQuery = factory.queryable(sourceQuery);
        ISqlTableRefExpression warpedRef = warpedQuery.getFrom().getTableRefExpression();
        // 设置为 SELECT * 防止丢失映射表字段信息
        warpedQuery.setSelect(factory.select(Collections.singletonList(factory.star(warpedRef)), warpedQuery.getMainTableClass()));
        warpedQuery.addWhere(factory.binary(
                SqlOperator.BETWEEN,
                factory.dynamicColumn(rowNumberAsName, long.class, warpedRef),
                factory.binary(SqlOperator.AND, factory.constString(offset + 1), factory.constString(offset + rows))
        ));

        return warpedQuery;
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

    protected PagedResult<R> toPagedResult0(long pageIndex, long pageSize) {
        IConfig config = getConfig();

        //SELECT COUNT(*) ...
        long total = count0(null);

        ISqlLimitExpression limit = sqlBuilder.getQueryable().getLimit();
        long tempRows = limit.getRows();
        long tempOffset = limit.getOffset();
        long take = Math.max(pageSize, 1);
        long index = Math.max(pageIndex, 1);
        long offset = (index - 1) * take;

        limit(offset, take);
        List<R> list = toList();
        limit(tempOffset, tempRows);

        return config.getPager().getPagedResult(total, list);
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
            QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), sqlBuilder.getQueryable());
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
            QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), sqlBuilder.getQueryable());
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
        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), sqlBuilder.getQueryable());
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
        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), sqlBuilder.getQueryable());
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
        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), sqlBuilder.getQueryable());
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
        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), sqlBuilder.getQueryable());
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
        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), sqlBuilder.getQueryable());
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
        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), sqlBuilder.getQueryable());
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
        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), sqlBuilder.getQueryable());
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
        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), sqlBuilder.getQueryable());
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

    // region [列转行]

    protected void pivot(
            // 聚合列
            LambdaExpression<?> aggColumnLambda,
            // 转换列
            LambdaExpression<?> transColumnLambda,
            // 转换列值
            Collection<?> transColumnValues,
            // 转换后的表结构
            LambdaExpression<?> resultLambda
    ) {
        IConfig config = getConfig();
        SqlExpressionFactory factory = config.getSqlExpressionFactory();

        // SELECT * FROM <table> AS t WHERE ...
        // SELECT t.new{...},t.聚合列,t.转换列 FROM <table> AS t WHERE ...
        // SELECT * FROM (列转行对象<(SELECT t.new{...},t.聚合列,t.转换列 FROM <table> AS t WHERE ...)>) AS t1
        ISqlQueryableExpression queryable = sqlBuilder.getQueryable();

        QuerySqlVisitor aggVisitor = new QuerySqlVisitor(config, queryable);
        ISqlTemplateExpression agg0 = aggVisitor.toAgg(aggColumnLambda);
        ISqlExpression aggColumn = agg0.getExpressions().get(0);

        QuerySqlVisitor transVisitor = new QuerySqlVisitor(config, queryable);
        ISqlColumnExpression trans0 = transVisitor.toColumn(transColumnLambda);

        QuerySqlVisitor q1 = new QuerySqlVisitor(config, queryable);
        ISqlSelectExpression select = q1.toSelect(resultLambda);
        select.addColumn(aggColumn);
        select.addColumn(trans0);
        sqlBuilder.setSelect(select);

        ISqlQueryableExpression temp = factory.queryable(queryable);
        QuerySqlVisitor q2 = new QuerySqlVisitor(config, temp);
        ISqlTemplateExpression agg = q2.toAgg(aggColumnLambda);
        ISqlColumnExpression trans = q2.toColumn(transColumnLambda);

        ISqlTableRefExpression tempRef = temp.getFrom().getTableRefExpression();
        ISqlPivotExpression pivot = factory.pivot(queryable, agg, aggColumnLambda.getReturnType(), trans, cast(transColumnValues), tempRef);

        sqlBuilder.setQueryable(factory.queryable(pivot, factory.tableRef(tempRef.getName())));
    }

    // endregion

    // region [行转列]

    protected void unPivot(LambdaExpression<?> result, List<LambdaExpression<?>> transColumns) {
        unPivot("nameColumn", "valueColumn", result, transColumns);
    }

    protected void unPivot(
            String newNameColumn,
            String newValueColumn,
            // 转换后的表对象
            LambdaExpression<?> result,
            // 转换列
            List<LambdaExpression<?>> transColumns
    ) {
        IConfig config = getConfig();
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        ISqlQueryableExpression queryable = sqlBuilder.getQueryable();
        QuerySqlVisitor q1 = new QuerySqlVisitor(config, queryable);
        ISqlSelectExpression select = q1.toSelect(result);
        for (LambdaExpression<?> transColumn : transColumns) {
            ISqlColumnExpression column = q1.toColumn(transColumn);
            select.addColumn(column);
        }
        sqlBuilder.setSelect(select);

        ISqlQueryableExpression temp = factory.queryable(queryable);
        ISqlTableRefExpression tempRef = temp.getFrom().getTableRefExpression();
        QuerySqlVisitor q2 = new QuerySqlVisitor(config, temp);
        List<ISqlColumnExpression> transColumnList = new ArrayList<>();
        for (LambdaExpression<?> transColumn : transColumns) {
            ISqlColumnExpression column = q2.toColumn(transColumn);
            transColumnList.add(column);
        }
        ISqlUnPivotExpression unPivot = factory.unPivot(
                queryable,
                newNameColumn,
                newValueColumn,
                transColumns.get(0).getReturnType(),
                transColumnList,
                tempRef
        );

        sqlBuilder.setQueryable(factory.queryable(unPivot, factory.tableRef(tempRef.getName())));
    }

    protected void ignoreColumn(LambdaExpression<?> lambdaExpression)
    {
        ISqlQueryableExpression queryable = sqlBuilder.getQueryable();
        QuerySqlVisitor visitor = new QuerySqlVisitor(getConfig(), queryable);
        ISqlColumnExpression column = visitor.toColumn(lambdaExpression);
        String selectFieldName = column.getFieldMetaData().getFieldName();
        ISqlSelectExpression select = queryable.getSelect();
        // 匹配移除
        select.getColumns().removeIf(s ->
        {
            if(s instanceof ISqlAsExpression)
            {
                s=((ISqlAsExpression) s).getExpression();
            }
            if (s instanceof ISqlColumnExpression)
            {
                ISqlColumnExpression sqlExpression = (ISqlColumnExpression) s;
                String f1 = sqlExpression.getFieldMetaData().getFieldName();
                return f1.equals(selectFieldName);
            }
            return false;
        });
    }

    // endregion
}
