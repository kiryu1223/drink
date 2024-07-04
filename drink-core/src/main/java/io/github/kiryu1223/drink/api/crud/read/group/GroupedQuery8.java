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

public class GroupedQuery8<Key, T1, T2, T3, T4, T5, T6, T7, T8> extends QueryBase
{
    public GroupedQuery8(QuerySqlBuilder sqlBuilder)
    {
        super(sqlBuilder);
    }

    // region [HAVING]
    public GroupedQuery8<Key, T1, T2, T3, T4, T5, T6, T7, T8> having(@Expr Func1<Group8<Key, T1, T2, T3, T4, T5, T6, T7, T8>, Boolean> func)
    {
        throw new RuntimeException();
    }

    public GroupedQuery8<Key, T1, T2, T3, T4, T5, T6, T7, T8> having(ExprTree<Func1<Group8<Key, T1, T2, T3, T4, T5, T6, T7, T8>, Boolean>> expr)
    {
        having(expr.getTree());
        return this;
    }

    // endregion

    // region [ORDER BY]
    public <R> GroupedQuery8<Key, T1, T2, T3, T4, T5, T6, T7, T8> orderBy(@Expr Func1<Group8<Key, T1, T2, T3, T4, T5, T6, T7, T8>, R> expr, boolean asc)
    {
        throw new RuntimeException();
    }

    public <R> GroupedQuery8<Key, T1, T2, T3, T4, T5, T6, T7, T8> orderBy(ExprTree<Func1<Group8<Key, T1, T2, T3, T4, T5, T6, T7, T8>, R>> expr, boolean asc)
    {
        orderBy(expr.getTree(),asc);
        return this;
    }

    public <R> GroupedQuery8<Key, T1, T2, T3, T4, T5, T6, T7, T8> orderBy(@Expr Func1<Group8<Key, T1, T2, T3, T4, T5, T6, T7, T8>, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> GroupedQuery8<Key, T1, T2, T3, T4, T5, T6, T7, T8> orderBy(ExprTree<Func1<Group8<Key, T1, T2, T3, T4, T5, T6, T7, T8>, R>> expr)
    {
        orderBy(expr, true);
        return this;
    }
    // endregion

    // region [SELECT]
    public <R> LQuery<R> select(@Expr Func1<Group8<Key, T1, T2, T3, T4, T5, T6, T7, T8>, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> LQuery<R> select(ExprTree<Func1<Group8<Key, T1, T2, T3, T4, T5, T6, T7, T8>, R>> expr)
    {
        singleCheck(select(expr.getTree()));
        return new LQuery<>(this);
    }
    // endregion
}
