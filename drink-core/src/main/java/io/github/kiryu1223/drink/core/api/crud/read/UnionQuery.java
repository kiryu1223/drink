package io.github.kiryu1223.drink.core.api.crud.read;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.toBean.build.JdbcResult;
import io.github.kiryu1223.drink.base.toBean.build.ObjectBuilder;
import io.github.kiryu1223.drink.base.toBean.executor.CreateBeanExecutor;
import io.github.kiryu1223.drink.base.toBean.executor.JdbcExecutor;
import io.github.kiryu1223.drink.base.toBean.executor.JdbcQueryResultSet;
import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;
import io.github.kiryu1223.drink.base.toBean.handler.TypeHandlerManager;
import io.github.kiryu1223.drink.core.api.crud.CRUD;
import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;
import io.github.kiryu1223.drink.core.sqlBuilder.UnionBuilder;
import io.github.kiryu1223.drink.base.expression.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UnionQuery<T> extends CRUD<UnionQuery<T>> {
    private static final Logger log = LoggerFactory.getLogger(UnionQuery.class);
    private final UnionBuilder unionBuilder;

    public UnionBuilder getSqlBuilder() {
        return unionBuilder;
    }

    public UnionQuery(IConfig config, LQuery<T> q1, LQuery<T> q2, boolean all) {
        this(config, q1.getSqlBuilder().getQueryable(), q2.getSqlBuilder().getQueryable(), all);
    }

    public UnionQuery(IConfig config, EndQuery<T> q1, EndQuery<T> q2, boolean all) {
        this(config, q1.getSqlBuilder().getQueryable(), q2.getSqlBuilder().getQueryable(), all);
    }

    public UnionQuery(IConfig config, ISqlQueryableExpression q1, ISqlQueryableExpression q2, boolean all) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        ISqlUnionQueryableExpression unionQuery = factory.unionQueryable(new ArrayList<>(Arrays.asList(q1, q2)), new ArrayList<>(Collections.singletonList(all)));
        unionBuilder = new UnionBuilder(config, unionQuery);
    }

    protected IConfig getConfig() {
        return unionBuilder.getConfig();
    }

    // region [UNION]
    public UnionQuery<T> union(LQuery<T> query, boolean all) {
        ISqlQueryableExpression queryable = query.getSqlBuilder().getQueryable();
        unionBuilder.addUnion(queryable, all);
        return this;
    }

    public UnionQuery<T> union(LQuery<T> query) {
        return union(query, false);
    }

    public UnionQuery<T> unionAll(LQuery<T> query) {
        return union(query, true);
    }

    public UnionQuery<T> union(EndQuery<T> query, boolean all) {
        ISqlQueryableExpression queryable = query.getSqlBuilder().getQueryable();
        unionBuilder.addUnion(queryable, all);
        return this;
    }

    public UnionQuery<T> union(EndQuery<T> query) {
        return union(query, false);
    }

    public UnionQuery<T> unionAll(EndQuery<T> query) {
        return union(query, true);
    }

    // endregion

    // region [SQL]

    public String toSql() {
        return unionBuilder.getSql();
    }

    @Override
    public UnionQuery<T> DisableFilter(String filterId) {
        unionBuilder.addIgnoreFilterId(filterId);
        return this;
    }

    @Override
    public UnionQuery<T> DisableFilterAll(boolean condition) {
        unionBuilder.setIgnoreFilterAll(condition);
        return this;
    }

    @Override
    public UnionQuery<T> DisableFilterAll() {
        unionBuilder.setIgnoreFilterAll(true);
        return this;
    }

    public List<T> toList() {
        IConfig config = getConfig();
        List<SqlValue> sqlValues = new ArrayList<>();
        boolean single = unionBuilder.isSingle();
        String sql = unionBuilder.getSqlAndValue(sqlValues);
        Class<T> targetClass = unionBuilder.getTargetClass();
        List<T> result;
        try (JdbcQueryResultSet jdbcQueryResultSet = JdbcExecutor.executeQuery(config, sql, sqlValues)) {
            if (single) {
                ITypeHandler<T> singleTypeHandler = getSingleTypeHandler();
                result = CreateBeanExecutor.singleList(jdbcQueryResultSet, singleTypeHandler, targetClass);
            }
            else {
                JdbcResult<T> jdbcResult = CreateBeanExecutor.classList(config, jdbcQueryResultSet, targetClass);
                result = jdbcResult.getResult();
                config.getSqlLogger().printTotal(result.size());
            }
        }
        return result;
    }

    protected ITypeHandler<T> getSingleTypeHandler() {
        List<ISqlExpression> columns = unionBuilder.getUnionQueryable().getQueryable().get(0).getSelect().getColumns();
        ISqlExpression expression = columns.get(0);
        if (expression instanceof ISqlColumnExpression) {
            ISqlColumnExpression columnExpression = (ISqlColumnExpression) expression;
            return (ITypeHandler<T>) columnExpression.getFieldMetaData().getTypeHandler();
        }
        else  {
            return TypeHandlerManager.get(expression.getType());
        }
    }

    // endregion

    // region [WITH]

    public LQuery<T> withTemp() {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        ISqlTableRefExpression tableRefExpression = unionBuilder.getUnionQueryable().getQueryable().get(0).getFrom().getTableRefExpression();
        QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(getConfig(), factory.queryable(unionBuilder.getUnionQueryable(),factory.tableRef(tableRefExpression.getName())));
        return new LQuery<>(querySqlBuilder);
    }

    // endregion
}
