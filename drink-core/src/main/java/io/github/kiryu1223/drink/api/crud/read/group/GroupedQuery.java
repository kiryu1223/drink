package io.github.kiryu1223.drink.api.crud.read.group;

import io.github.kiryu1223.drink.api.crud.builder.QuerySqlBuilder;
import io.github.kiryu1223.drink.api.crud.read.EndQuery;
import io.github.kiryu1223.drink.api.crud.read.LQuery;
import io.github.kiryu1223.drink.api.crud.read.QueryBase;
import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

import java.util.List;

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
        orderBy(expr.getTree(), asc);
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
        singleCheck(select(expr.getTree()));
        return new LQuery<>(getSqlBuilder());
    }

    public <R> EndQuery<R> selectSingle(@Expr Func1<Group<Key, T>, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> EndQuery<R> selectSingle(ExprTree<Func1<Group<Key, T>, R>> expr)
    {
        select(expr.getTree());
        return new EndQuery<>(this);
    }

    // endregion

    @Override
    public List<Key> toList()
    {
        return super.toList();
    }

    public GroupedQuery<Key, T> distinct()
    {
        distinct0(true);
        return this;
    }

    public GroupedQuery<Key, T> distinct(boolean condition)
    {
        distinct0(condition);
        return this;
    }
}
