package io.github.kiryu1223.drink.api.crud.read.group;


import io.github.kiryu1223.drink.api.crud.builder.QuerySqlBuilder;
import io.github.kiryu1223.drink.api.crud.read.LQuery;
import io.github.kiryu1223.drink.api.crud.read.QueryBase;
import io.github.kiryu1223.drink.core.context.SqlContext;
import io.github.kiryu1223.drink.core.context.SqlOrderContext;
import io.github.kiryu1223.drink.core.visitor.HavingVisitor;
import io.github.kiryu1223.drink.core.visitor.SelectVisitor;
import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;


public class GroupedQuery3<Key, T1, T2, T3> extends QueryBase
{
    public GroupedQuery3(QuerySqlBuilder sqlBuilder)
    {
        super(sqlBuilder);
    }

    // region [HAVING]
    public GroupedQuery3<Key, T1, T2, T3> having(@Expr Func1<Group3<Key, T1, T2, T3>, Boolean> func)
    {
        throw new RuntimeException();
    }

    public GroupedQuery3<Key, T1, T2, T3> having(ExprTree<Func1<Group3<Key, T1, T2, T3>, Boolean>> expr)
    {
        HavingVisitor havingVisitor = new HavingVisitor(getSqlBuilder().getGroupBy());
        SqlContext context = havingVisitor.visit(expr.getTree());
        getSqlBuilder().addHaving(context);
        return this;
    }
    // endregion

    // region [ORDER BY]
    public <R> GroupedQuery3<Key, T1, T2, T3> orderBy(@Expr Func1<Group3<Key, T1, T2, T3>, R> expr, boolean asc)
    {
        throw new RuntimeException();
    }

    public <R> GroupedQuery3<Key, T1, T2, T3> orderBy(ExprTree<Func1<Group3<Key, T1, T2, T3>, R>> expr, boolean asc)
    {
        HavingVisitor havingVisitor = new HavingVisitor(getSqlBuilder().getGroupBy());
        SqlContext context = havingVisitor.visit(expr.getTree());
        getSqlBuilder().addOrderBy(new SqlOrderContext(asc, context));
        return this;
    }

    public <R> GroupedQuery3<Key, T1, T2, T3> orderBy(@Expr Func1<Group3<Key, T1, T2, T3>, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> GroupedQuery3<Key, T1, T2, T3> orderBy(ExprTree<Func1<Group3<Key, T1, T2, T3>, R>> expr)
    {
        orderBy(expr, true);
        return this;
    }
    // endregion

    // region [SELECT]
    public <R> LQuery<R> select(@Expr Func1<Group3<Key, T1, T2, T3>, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> LQuery<R> select(ExprTree<Func1<Group3<Key, T1, T2, T3>, R>> expr)
    {
        SelectVisitor selectVisitor = new SelectVisitor(getSqlBuilder().getGroupBy());
        SqlContext context = selectVisitor.visit(expr.getTree());
        getSqlBuilder().setSelect(context);
        return new LQuery<>(this);
    }
    // endregion
}
