package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.api.crud.base.CRUD;
import io.github.kiryu1223.drink.api.crud.builder.QuerySqlBuilder;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.SqlSession;
import io.github.kiryu1223.drink.core.context.*;
import io.github.kiryu1223.drink.core.visitor.GroupByVisitor;
import io.github.kiryu1223.drink.core.visitor.HavingVisitor;
import io.github.kiryu1223.drink.core.visitor.SelectVisitor;
import io.github.kiryu1223.drink.core.visitor.WhereVisitor;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;
import io.github.kiryu1223.expressionTree.expressions.LambdaExpression;

import java.util.ArrayList;
import java.util.List;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.getTableName;

public abstract class QueryBase implements CRUD
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

    @Override
    public QuerySqlBuilder getSqlBuilder()
    {
        return sqlBuilder;
    }

    public boolean any()
    {
        SqlSession sqlSession = new SqlSession();
        List<Object> values = new ArrayList<>();
        String sql = sqlBuilder.getSqlAndValue(values);
        return sqlSession.executeQuery(f -> f.next(), "SELECT 1 FROM (" + sql + ")", values);
    }


    public Config getConfig()
    {
        return sqlBuilder.getConfig();
    }

    protected void select(LambdaExpression<?> lambda)
    {
        SelectVisitor selectVisitor = new SelectVisitor(getSqlBuilder().getGroupBy());
        SqlContext context = selectVisitor.visit(lambda);
        getSqlBuilder().setSelect(context);
    }

    protected <Tn> QueryBase joinNewQuery()
    {
        return null;
    }

    protected void join(JoinType joinType, Class<?> target, ExprTree<?> expr)
    {
        WhereVisitor whereVisitor = new WhereVisitor();
        SqlContext context = whereVisitor.visit(expr.getTree());
        SqlJoinTableContext sqlJoin = new SqlJoinTableContext(joinType, context, getTableName(target));
        getSqlBuilder().addJoin(target, sqlJoin);
    }

    protected void join(JoinType joinType, QueryBase target, ExprTree<?> expr)
    {
        WhereVisitor whereVisitor = new WhereVisitor();
        SqlContext context = whereVisitor.visit(expr.getTree());
        SqlJoinQueryContext sqlJoin = new SqlJoinQueryContext(joinType, context, target.getSqlBuilder());
        getSqlBuilder().addJoin(target.getSqlBuilder().getTargetClass(), sqlJoin);
    }

    protected void where(LambdaExpression<?> lambda)
    {
        WhereVisitor whereVisitor = new WhereVisitor();
        SqlContext where = whereVisitor.visit(lambda);
        getSqlBuilder().addWhere(where);
    }

    protected void groupBy(LambdaExpression<?> lambda)
    {
        GroupByVisitor groupByVisitor = new GroupByVisitor();
        SqlContext context = groupByVisitor.visit(lambda);
        getSqlBuilder().setGroupBy(context);
    }

    protected void orderBy(LambdaExpression<?> lambda, boolean asc)
    {
        HavingVisitor havingVisitor = new HavingVisitor(getSqlBuilder().getGroupBy());
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
}
