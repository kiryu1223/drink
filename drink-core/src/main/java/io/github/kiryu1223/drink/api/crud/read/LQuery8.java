package io.github.kiryu1223.drink.api.crud.read;


import io.github.kiryu1223.drink.api.crud.read.group.GroupedQuery8;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.JoinType;
import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.delegate.Func8;
import io.github.kiryu1223.expressionTree.delegate.Func9;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> extends QueryBase
{
    // region [INIT]

    public LQuery8(Config config)
    {
        super(config);
    }

    public LQuery8(Config config, Class<?> c1, Class<?> c2, Class<?> c3, Class<?> c4, Class<?> c5, Class<?> c6, Class<?> c7, Class<?> c8)
    {
        super(config);
        getSqlBuilder().addFrom(c1, c2, c3, c4, c5, c6, c7, c8);
    }

    public LQuery8(QueryBase q1, QueryBase q2, QueryBase q3, QueryBase q4, QueryBase q5, QueryBase q6, QueryBase q7, QueryBase q8)
    {
        super(q1.getConfig());
        getSqlBuilder().addFrom(q1.getSqlBuilder(), q2.getSqlBuilder(), q3.getSqlBuilder(), q4.getSqlBuilder(), q5.getSqlBuilder(), q6.getSqlBuilder(), q7.getSqlBuilder(), q8.getSqlBuilder());
    }

    // endregion

    //region [JOIN]

    @Override
    protected <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> joinNewQuery()
    {
        LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> query = new LQuery9<>(getConfig());
        query.getSqlBuilder().joinBy(getSqlBuilder());
        return query;
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> innerJoin(Class<Tn> target, @Expr Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> innerJoin(Class<Tn> target, ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> innerJoin(LQuery<Tn> target, @Expr Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> innerJoin(LQuery<Tn> target, ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> leftJoin(Class<Tn> target, @Expr Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> leftJoin(Class<Tn> target, ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> leftJoin(LQuery<Tn> target, @Expr Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> leftJoin(LQuery<Tn> target, ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> rightJoin(Class<Tn> target, @Expr Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> rightJoin(Class<Tn> target, ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> rightJoin(LQuery<Tn> target, @Expr Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> rightJoin(LQuery<Tn> target, ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return joinNewQuery();
    }

    // endregion

    // region [WHERE]
    public LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> where(@Expr Func8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean> func)
    {
        throw new RuntimeException();
    }

    public LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> where(ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean>> expr)
    {
        where(expr.getTree());
        return this;
    }
    // endregion

    // region [ORDER BY]
    public <R> LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> orderBy(@Expr Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> expr, boolean asc)
    {
        throw new RuntimeException();
    }

    public <R> LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> orderBy(ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, R>> expr, boolean asc)
    {
        orderBy(expr.getTree(), asc);
        return this;
    }

    public <R> LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> orderBy(@Expr Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> orderBy(ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, R>> expr)
    {
        orderBy(expr.getTree(), true);
        return this;
    }
    // endregion

    // region [LIMIT]
    public LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> limit(long rows)
    {
        limit0(rows);
        return this;
    }

    public LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> limit(long offset, long rows)
    {
        limit0(offset, rows);
        return this;
    }
    // endregion

    // region [GROUP BY]
    public <Key> GroupedQuery8<Key, T1, T2, T3, T4, T5, T6, T7, T8> groupBy(@Expr Func8<T1, T2, T3, T4, T5, T6, T7, T8, Key> expr)
    {
        throw new RuntimeException();
    }

    public <Key> GroupedQuery8<Key, T1, T2, T3, T4, T5, T6, T7, T8> groupBy(ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, Key>> expr)
    {
        groupBy(expr.getTree());
        return new GroupedQuery8<>(getSqlBuilder());
    }
    // endregion

    // region [SELECT]
    public LQuery<T1> select()
    {
        return new LQuery<>(this);
    }

    public <R> LQuery<R> select(Class<R> r)
    {
        getSqlBuilder().setTargetClass(r);
        return new LQuery<>(this);
    }

    public <R> LQuery<R> select(@Expr Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> LQuery<R> select(ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, R>> expr)
    {
        select(expr.getTree());
        return new LQuery<>(this);
    }

    // endregion

    //region [OTHER]

    public LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> distinct()
    {
        getSqlBuilder().setDistinct(true);
        return this;
    }

    public LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> distinct(boolean condition)
    {
        getSqlBuilder().setDistinct(condition);
        return this;
    }

    //endregion

}
