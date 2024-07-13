package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.api.crud.base.CRUD;
import io.github.kiryu1223.drink.api.crud.builder.QuerySqlBuilder;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.builder.*;
import io.github.kiryu1223.drink.core.context.*;
import io.github.kiryu1223.drink.core.visitor.GroupByVisitor;
import io.github.kiryu1223.drink.core.visitor.HavingVisitor;
import io.github.kiryu1223.drink.core.visitor.SelectVisitor;
import io.github.kiryu1223.drink.core.visitor.WhereVisitor;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;
import io.github.kiryu1223.expressionTree.expressions.LambdaExpression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public abstract class QueryBase extends CRUD
{
    private final QuerySqlBuilder sqlBuilder;

    public QueryBase(Config config)
    {
        sqlBuilder = new QuerySqlBuilder(config);
    }

    public QueryBase(QuerySqlBuilder sqlBuilder)
    {
        this.sqlBuilder = sqlBuilder;
    }

    protected QuerySqlBuilder getSqlBuilder()
    {
        return sqlBuilder;
    }

    protected Config getConfig()
    {
        return sqlBuilder.getConfig();
    }

    protected boolean any()
    {
        SqlSession session = SqlSessionBuilder.getSession(getConfig());
        List<Object> values = new ArrayList<>();
        String sql = sqlBuilder.getSqlAndValue(values);
        return session.executeQuery(f -> f.next(), "SELECT 1 FROM (" + sql + ") LIMIT 1", values);
    }

    protected <R> EndQuery<R> select(Class<R> r)
    {
        select0(r);
        return new EndQuery<>(this);
    }

    protected <T> List<T> toList()
    {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        List<PropertyMetaData> mappingData = sqlBuilder.getMappingData(atomicBoolean);
        List<Object> values = new ArrayList<>();
        String sql = sqlBuilder.getSqlAndValue(values);
        System.out.println("===> " + sql);
        String key = getConfig().getDsKey();
        System.out.println("使用数据源: " + (key == null ? "默认" : key));
        SqlSession session = SqlSessionBuilder.getSession(getConfig());
        return session.executeQuery(
                r -> ObjectBuilder.start(r, getConfig(), (Class<T>) sqlBuilder.getTargetClass(), mappingData, atomicBoolean.get()).createList(),
                sql,
                values
        );
    }

    // region [123]

    protected void distinct0(boolean condition)
    {
        sqlBuilder.setDistinct(condition);
    }

    protected boolean select(LambdaExpression<?> lambda)
    {
        SelectVisitor selectVisitor = new SelectVisitor(getSqlBuilder().getGroupBy(), getConfig());
        SqlContext context = selectVisitor.visit(lambda);
        getSqlBuilder().setSelect(context);
        getSqlBuilder().setTargetClass(lambda.getReturnType());
        //propertyMetaData = selectVisitor.getPropertyMetaData();
        //System.out.println(propertyMetaData.size());
        return !(context instanceof SqlSelectorContext);
    }

    protected void select0()
    {
        sqlBuilder.setTypeSelect();
    }

    protected void select0(Class<?> c)
    {
        getSqlBuilder().setTargetClass(c);
        sqlBuilder.setTypeSelect();
    }

    protected <Tn> QueryBase joinNewQuery()
    {
        return null;
    }

    protected void join(JoinType joinType, Class<?> target, ExprTree<?> expr)
    {
        WhereVisitor whereVisitor = new WhereVisitor(getConfig());
        SqlContext onContext = whereVisitor.visit(expr.getTree());
        SqlTableContext tableContext = new SqlRealTableContext(target);
        getSqlBuilder().addJoin(target, joinType, tableContext, onContext);
    }

    protected void join(JoinType joinType, QueryBase target, ExprTree<?> expr)
    {
        WhereVisitor whereVisitor = new WhereVisitor(getConfig());
        SqlContext onContext = whereVisitor.visit(expr.getTree());
        SqlTableContext tableContext = new SqlVirtualTableContext(target.getSqlBuilder());
        getSqlBuilder().addJoin(target.getSqlBuilder().getTargetClass(), joinType, tableContext, onContext);
    }

    protected void where(LambdaExpression<?> lambda)
    {
        WhereVisitor whereVisitor = new WhereVisitor(getConfig());
        SqlContext where = whereVisitor.visit(lambda);
        getSqlBuilder().addWhere(where);
    }

    protected void orWhere(LambdaExpression<?> lambda)
    {
        WhereVisitor whereVisitor = new WhereVisitor(getConfig());
        SqlContext right = whereVisitor.visit(lambda);
        getSqlBuilder().addOrWhere(right);
    }

    protected void exists(Class<?> table, LambdaExpression<?> lambda, boolean not)
    {
        WhereVisitor whereVisitor = new WhereVisitor(getConfig());
        SqlContext where = whereVisitor.visit(lambda);
        QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(getConfig());
        querySqlBuilder.addMoveCount(getSqlBuilder().getOrderedClass().size());
        querySqlBuilder.setSelect(new SqlConstString("1"));
        querySqlBuilder.addFrom(table);
        querySqlBuilder.addWhere(where);
        SqlUnaryContext exists = new SqlUnaryContext(SqlOperator.EXISTS, new SqlParensContext(new SqlVirtualTableContext(querySqlBuilder)));
        if (not)
        {
            getSqlBuilder().addWhere(new SqlUnaryContext(SqlOperator.NOT, exists));
        }
        else
        {
            getSqlBuilder().addWhere(exists);
        }
    }

    protected void exists(QueryBase queryBase, LambdaExpression<?> lambda, boolean not)
    {
        WhereVisitor whereVisitor = new WhereVisitor(getConfig());
        SqlContext where = whereVisitor.visit(lambda);
        QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(getConfig());
        querySqlBuilder.addMoveCount(getSqlBuilder().getOrderedClass().size());
        querySqlBuilder.setSelect(new SqlConstString("1"));
        querySqlBuilder.addFrom(queryBase.sqlBuilder);
        querySqlBuilder.addWhere(where);
        SqlUnaryContext exists = new SqlUnaryContext(SqlOperator.EXISTS, new SqlParensContext(new SqlVirtualTableContext(querySqlBuilder)));
        if (not)
        {
            getSqlBuilder().addWhere(new SqlUnaryContext(SqlOperator.NOT, exists));
        }
        else
        {
            getSqlBuilder().addWhere(exists);
        }
    }

    protected void groupBy(LambdaExpression<?> lambda)
    {
        GroupByVisitor groupByVisitor = new GroupByVisitor(getConfig());
        SqlContext context = groupByVisitor.visit(lambda);
        getSqlBuilder().setGroupBy(context);
        getSqlBuilder().setTargetClass(lambda.getReturnType());
    }

    protected void having(LambdaExpression<?> lambda)
    {
        HavingVisitor havingVisitor = new HavingVisitor(getSqlBuilder().getGroupBy(), getConfig());
        SqlContext context = havingVisitor.visit(lambda);
        getSqlBuilder().addHaving(context);
    }

    protected void orderBy(LambdaExpression<?> lambda, boolean asc)
    {
        HavingVisitor havingVisitor = new HavingVisitor(getSqlBuilder().getGroupBy(), getConfig());
        SqlContext context = havingVisitor.visit(lambda);
        getSqlBuilder().addOrderBy(new SqlOrderContext(asc, context));
    }

    protected void limit0(long rows)
    {
        getSqlBuilder().setLimit(new SqlLimitContext(rows));
    }

    protected void limit0(long offset, long rows)
    {
        getSqlBuilder().setLimit(new SqlLimitContext(offset, rows));
    }

    // endregion

    protected void singleCheck(boolean single)
    {
        if (single)
        {
            throw new RuntimeException("query.select(Func<T1,T2..., R> expr) 不允许传入单个元素, 单元素请使用selectSingle");
        }
    }

    public String toSql()
    {
        //setDefaultSelect();
        return sqlBuilder.getSql();
    }

    protected QuerySqlBuilder boxedQuerySqlBuilder()
    {
        QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(getConfig());
        querySqlBuilder.addFrom(getSqlBuilder());
        return querySqlBuilder;
    }
}
