package io.github.kiryu1223.drink.core.api.crud.read;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.session.SqlSession;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.toBean.build.ObjectBuilder;
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

    public List<T> toList() {
        IConfig config = getConfig();
        List<SqlValue> sqlValues = new ArrayList<>();
        boolean single = unionBuilder.isSingle();
        List<FieldMetaData> mappingData = single ? Collections.emptyList() : unionBuilder.getMappingData();
        String sql = unionBuilder.getSqlAndValue(sqlValues);
        printSql(sql);
        printValues(sqlValues);
        Class<T> targetClass = unionBuilder.getTargetClass();
        SqlSession session = config.getSqlSessionFactory().getSession();
        List<T> result = session.executeQuery(
                r -> ObjectBuilder.start(r, targetClass, mappingData, single, config).createList(),
                sql,
                sqlValues
        ).getResult();
        printTotal(result.size());
        return result;
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
