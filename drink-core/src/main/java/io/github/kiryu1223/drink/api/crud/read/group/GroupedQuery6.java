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

public class GroupedQuery6<Key, T1, T2, T3, T4, T5, T6> extends QueryBase
{
    public GroupedQuery6(QuerySqlBuilder sqlBuilder)
    {
        super(sqlBuilder);
    }

    // region [HAVING]
    public GroupedQuery6<Key, T1, T2, T3, T4, T5, T6> having(@Expr Func1<Group6<Key, T1, T2, T3, T4, T5, T6>, Boolean> func)
    {
        throw new RuntimeException();
    }

    public GroupedQuery6<Key, T1, T2, T3, T4, T5, T6> having(ExprTree<Func1<Group6<Key, T1, T2, T3, T4, T5, T6>, Boolean>> expr)
    {
        having(expr.getTree());
        return this;
    }

    // endregion

    // region [ORDER BY]
    public <R> GroupedQuery6<Key, T1, T2, T3, T4, T5, T6> orderBy(@Expr Func1<Group6<Key, T1, T2, T3, T4, T5, T6>, R> expr, boolean asc)
    {
        throw new RuntimeException();
    }

    public <R> GroupedQuery6<Key, T1, T2, T3, T4, T5, T6> orderBy(ExprTree<Func1<Group6<Key, T1, T2, T3, T4, T5, T6>, R>> expr, boolean asc)
    {
        orderBy(expr.getTree(),asc);
        return this;
    }

    public <R> GroupedQuery6<Key, T1, T2, T3, T4, T5, T6> orderBy(@Expr Func1<Group6<Key, T1, T2, T3, T4, T5, T6>, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> GroupedQuery6<Key, T1, T2, T3, T4, T5, T6> orderBy(ExprTree<Func1<Group6<Key, T1, T2, T3, T4, T5, T6>, R>> expr)
    {
        orderBy(expr, true);
        return this;
    }
    // endregion

    // region [SELECT]
    public <R> LQuery<R> select(@Expr Func1<Group6<Key, T1, T2, T3, T4, T5, T6>, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> LQuery<R> select(ExprTree<Func1<Group6<Key, T1, T2, T3, T4, T5, T6>, R>> expr)
    {
        select(expr.getTree());
        return new LQuery<>(this);
    }
    // endregion
}
