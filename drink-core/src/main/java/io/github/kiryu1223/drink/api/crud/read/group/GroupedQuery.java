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

public class GroupedQuery<Key, T> extends QueryBase
{
    public GroupedQuery(QuerySqlBuilder sqlBuilder)
    {
        super(sqlBuilder);
    }

    // region [HAVING]

    public GroupedQuery<Key, T> having(@Expr Func1<Group<Key, T>, Boolean> func)
    {
        throw new RuntimeException();
    }

    public GroupedQuery<Key, T> having(ExprTree<Func1<Group<Key, T>, Boolean>> expr)
    {
        having(expr.getTree());
        return this;
    }

    // endregion

    // region [ORDER BY]

    public <R> GroupedQuery<Key, T> orderBy(@Expr Func1<Group<Key, T>, R> expr, boolean asc)
    {
        throw new RuntimeException();
    }

    public <R> GroupedQuery<Key, T> orderBy(ExprTree<Func1<Group<Key, T>, R>> expr, boolean asc)
    {
        orderBy(expr.getTree(),asc);
        return this;
    }


    public <R> GroupedQuery<Key, T> orderBy(@Expr Func1<Group<Key, T>, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> GroupedQuery<Key, T> orderBy(ExprTree<Func1<Group<Key, T>, R>> expr)
    {
        orderBy(expr, true);
        return this;
    }
    // endregion

    // region [SELECT]

    public <R> LQuery<R> select(@Expr Func1<Group<Key, T>, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> LQuery<R> select(ExprTree<Func1<Group<Key, T>, R>> expr)
    {
        select(expr.getTree());
        return new LQuery<>(this);
    }

    // endregion
}
