package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.api.crud.read.group.GroupedQuery6;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.JoinType;
import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.delegate.Func6;
import io.github.kiryu1223.expressionTree.delegate.Func7;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class LQuery6<T1, T2, T3, T4, T5, T6> extends QueryBase
{
    // region [INIT]

    public LQuery6(Config config)
    {
        super(config);
    }

    public LQuery6(Config config, Class<?> c1, Class<?> c2, Class<?> c3, Class<?> c4, Class<?> c5, Class<?> c6)
    {
        super(config);
        getSqlBuilder().addFrom(c1, c2, c3, c4, c5, c6);
    }

    public LQuery6(QueryBase q1, QueryBase q2, QueryBase q3, QueryBase q4, QueryBase q5, QueryBase q6)
    {
        super(q1.getConfig());
        getSqlBuilder().addFrom(q1.getSqlBuilder(), q2.getSqlBuilder(), q3.getSqlBuilder(), q4.getSqlBuilder(), q5.getSqlBuilder(), q6.getSqlBuilder());
    }

    // endregion

    //region [JOIN]

    @Override
    protected <Tn> LQuery7<T1, T2, T3, T4, T5, T6, Tn> joinNewQuery()
    {
        LQuery7<T1, T2, T3, T4, T5, T6, Tn> query = new LQuery7<>(getConfig());
        query.getSqlBuilder().joinBy(getSqlBuilder());
        return query;
    }

    public <Tn> LQuery7<T1, T2, T3, T4, T5, T6, Tn> innerJoin(Class<Tn> target, @Expr Func7<T1, T2, T3, T4, T5, T6, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery7<T1, T2, T3, T4, T5, T6, Tn> innerJoin(Class<Tn> target, ExprTree<Func7<T1, T2, T3, T4, T5, T6, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery7<T1, T2, T3, T4, T5, T6, Tn> innerJoin(LQuery<Tn> target, @Expr Func7<T1, T2, T3, T4, T5, T6, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery7<T1, T2, T3, T4, T5, T6, Tn> innerJoin(LQuery<Tn> target, ExprTree<Func7<T1, T2, T3, T4, T5, T6, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery7<T1, T2, T3, T4, T5, T6, Tn> leftJoin(Class<Tn> target, @Expr Func7<T1, T2, T3, T4, T5, T6, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery7<T1, T2, T3, T4, T5, T6, Tn> leftJoin(Class<Tn> target, ExprTree<Func7<T1, T2, T3, T4, T5, T6, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery7<T1, T2, T3, T4, T5, T6, Tn> leftJoin(LQuery<Tn> target, @Expr Func7<T1, T2, T3, T4, T5, T6, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery7<T1, T2, T3, T4, T5, T6, Tn> leftJoin(LQuery<Tn> target, ExprTree<Func7<T1, T2, T3, T4, T5, T6, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery7<T1, T2, T3, T4, T5, T6, Tn> rightJoin(Class<Tn> target, @Expr Func7<T1, T2, T3, T4, T5, T6, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery7<T1, T2, T3, T4, T5, T6, Tn> rightJoin(Class<Tn> target, ExprTree<Func7<T1, T2, T3, T4, T5, T6, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery7<T1, T2, T3, T4, T5, T6, Tn> rightJoin(LQuery<Tn> target, @Expr Func7<T1, T2, T3, T4, T5, T6, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery7<T1, T2, T3, T4, T5, T6, Tn> rightJoin(LQuery<Tn> target, ExprTree<Func7<T1, T2, T3, T4, T5, T6, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return joinNewQuery();
    }

    // endregion

    // region [WHERE]
    public LQuery6<T1, T2, T3, T4, T5, T6> where(@Expr Func6<T1, T2, T3, T4, T5, T6, Boolean> func)
    {
        throw new RuntimeException();
    }

    public LQuery6<T1, T2, T3, T4, T5, T6> where(ExprTree<Func6<T1, T2, T3, T4, T5, T6, Boolean>> expr)
    {
        where(expr.getTree());
        return this;
    }
    // endregion

    // region [ORDER BY]
    public <R> LQuery6<T1, T2, T3, T4, T5, T6> orderBy(@Expr Func6<T1, T2, T3, T4, T5, T6, R> expr, boolean asc)
    {
        throw new RuntimeException();
    }

    public <R> LQuery6<T1, T2, T3, T4, T5, T6> orderBy(ExprTree<Func6<T1, T2, T3, T4, T5, T6, R>> expr, boolean asc)
    {
        orderBy(expr.getTree(), asc);
        return this;
    }

    public <R> LQuery6<T1, T2, T3, T4, T5, T6> orderBy(@Expr Func6<T1, T2, T3, T4, T5, T6, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> LQuery6<T1, T2, T3, T4, T5, T6> orderBy(ExprTree<Func6<T1, T2, T3, T4, T5, T6, R>> expr)
    {
        orderBy(expr, true);
        return this;
    }
    // endregion

    // region [LIMIT]
    public LQuery6<T1, T2, T3, T4, T5, T6> limit(long rows)
    {
        limit0(rows);
        return this;
    }

    public LQuery6<T1, T2, T3, T4, T5, T6> limit(long offset, long rows)
    {
        limit0(offset, rows);
        return this;
    }
    // endregion

    // region [GROUP BY]
    public <Key> GroupedQuery6<Key, T1, T2, T3, T4, T5, T6> groupBy(@Expr Func6<T1, T2, T3, T4, T5, T6, Key> expr)
    {
        throw new RuntimeException();
    }

    public <Key> GroupedQuery6<Key, T1, T2, T3, T4, T5, T6> groupBy(ExprTree<Func6<T1, T2, T3, T4, T5, T6, Key>> expr)
    {
        groupBy(expr.getTree());
        return new GroupedQuery6<>(getSqlBuilder());
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

    public <R> LQuery<R> select(@Expr Func6<T1, T2, T3, T4, T5, T6, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> LQuery<R> select(ExprTree<Func6<T1, T2, T3, T4, T5, T6, R>> expr)
    {
        select(expr.getTree());
        return new LQuery<>(this);
    }

    // endregion

    //region [OTHER]

    public LQuery6<T1, T2, T3, T4, T5, T6> distinct()
    {
        getSqlBuilder().setDistinct(true);
        return this;
    }

    public LQuery6<T1, T2, T3, T4, T5, T6> distinct(boolean condition)
    {
        getSqlBuilder().setDistinct(condition);
        return this;
    }

    //endregion

    // region [toAny]

    // endregion
}
