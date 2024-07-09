package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.api.crud.read.group.GroupedQuery7;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.JoinType;
import io.github.kiryu1223.expressionTree.delegate.Func7;
import io.github.kiryu1223.expressionTree.delegate.Func8;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

public class LQuery7<T1, T2, T3, T4, T5, T6, T7> extends QueryBase
{
    // region [INIT]

    public LQuery7(Config config)
    {
        super(config);
    }

    public LQuery7(Config config, Class<?> c1, Class<?> c2, Class<?> c3, Class<?> c4, Class<?> c5, Class<?> c6, Class<?> c7)
    {
        super(config);
        getSqlBuilder().addFrom(c1, c2, c3, c4, c5, c6, c7);
    }

    public LQuery7(QueryBase q1, QueryBase q2, QueryBase q3, QueryBase q4, QueryBase q5, QueryBase q6, QueryBase q7)
    {
        super(q1.getConfig());
        getSqlBuilder().addFrom(q1.getSqlBuilder(), q2.getSqlBuilder(), q3.getSqlBuilder(), q4.getSqlBuilder(), q5.getSqlBuilder(), q6.getSqlBuilder(), q7.getSqlBuilder());
    }

    // endregion

    //region [JOIN]

    @Override
    protected <Tn> LQuery8<T1, T2, T3, T4, T5, T6, T7, Tn> joinNewQuery()
    {
        LQuery8<T1, T2, T3, T4, T5, T6, T7, Tn> query = new LQuery8<>(getConfig());
        query.getSqlBuilder().joinBy(getSqlBuilder());
        return query;
    }

    public <Tn> LQuery8<T1, T2, T3, T4, T5, T6, T7, Tn> innerJoin(Class<Tn> target, @Expr Func8<T1, T2, T3, T4, T5, T6, T7, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery8<T1, T2, T3, T4, T5, T6, T7, Tn> innerJoin(Class<Tn> target, ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery8<T1, T2, T3, T4, T5, T6, T7, Tn> innerJoin(LQuery<Tn> target, @Expr Func8<T1, T2, T3, T4, T5, T6, T7, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery8<T1, T2, T3, T4, T5, T6, T7, Tn> innerJoin(LQuery<Tn> target, ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery8<T1, T2, T3, T4, T5, T6, T7, Tn> leftJoin(Class<Tn> target, @Expr Func8<T1, T2, T3, T4, T5, T6, T7, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery8<T1, T2, T3, T4, T5, T6, T7, Tn> leftJoin(Class<Tn> target, ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery8<T1, T2, T3, T4, T5, T6, T7, Tn> leftJoin(LQuery<Tn> target, @Expr Func8<T1, T2, T3, T4, T5, T6, T7, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery8<T1, T2, T3, T4, T5, T6, T7, Tn> leftJoin(LQuery<Tn> target, ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery8<T1, T2, T3, T4, T5, T6, T7, Tn> rightJoin(Class<Tn> target, @Expr Func8<T1, T2, T3, T4, T5, T6, T7, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery8<T1, T2, T3, T4, T5, T6, T7, Tn> rightJoin(Class<Tn> target, ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery8<T1, T2, T3, T4, T5, T6, T7, Tn> rightJoin(LQuery<Tn> target, @Expr Func8<T1, T2, T3, T4, T5, T6, T7, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery8<T1, T2, T3, T4, T5, T6, T7, Tn> rightJoin(LQuery<Tn> target, ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return joinNewQuery();
    }

    // endregion

    // region [WHERE]
    public LQuery7<T1, T2, T3, T4, T5, T6, T7> where(@Expr Func7<T1, T2, T3, T4, T5, T6, T7, Boolean> func)
    {
        throw new RuntimeException();
    }

    public LQuery7<T1, T2, T3, T4, T5, T6, T7> where(ExprTree<Func7<T1, T2, T3, T4, T5, T6, T7, Boolean>> expr)
    {
        where(expr.getTree());
        return this;
    }

    public LQuery7<T1, T2, T3, T4, T5, T6, T7> orWhere(@Expr Func7<T1, T2, T3, T4, T5, T6, T7, Boolean> func)
    {
        throw new RuntimeException();
    }

    public LQuery7<T1, T2, T3, T4, T5, T6, T7> orWhere(ExprTree<Func7<T1, T2, T3, T4, T5, T6, T7, Boolean>> expr)
    {
        orWhere(expr.getTree());
        return this;
    }

    public <E> LQuery7<T1, T2, T3, T4, T5, T6, T7> exists(Class<E> table, @Expr Func8<T1, T2, T3, T4, T5, T6, T7, E, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <E> LQuery7<T1, T2, T3, T4, T5, T6, T7> exists(Class<E> table, ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, E, Boolean>> expr)
    {
        exists(table, expr.getTree(),false);
        return this;
    }

    public <E> LQuery7<T1, T2, T3, T4, T5, T6, T7> exists(LQuery<E> query, @Expr Func8<T1, T2, T3, T4, T5, T6, T7, E, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <E> LQuery7<T1, T2, T3, T4, T5, T6, T7> exists(LQuery<E> query, ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, E, Boolean>> expr)
    {
        exists(query, expr.getTree(),false);
        return this;
    }

    public <E> LQuery7<T1, T2, T3, T4, T5, T6, T7> notExists(Class<E> table, @Expr Func8<T1, T2, T3, T4, T5, T6, T7, E, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <E> LQuery7<T1, T2, T3, T4, T5, T6, T7> notExists(Class<E> table, ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, E, Boolean>> expr)
    {
        exists(table, expr.getTree(),true);
        return this;
    }

    public <E> LQuery7<T1, T2, T3, T4, T5, T6, T7> notExists(LQuery<E> query, @Expr Func8<T1, T2, T3, T4, T5, T6, T7, E, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <E> LQuery7<T1, T2, T3, T4, T5, T6, T7> notExists(LQuery<E> query, ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, E, Boolean>> expr)
    {
        exists(query, expr.getTree(),true);
        return this;
    }
    // endregion

    // region [ORDER BY]
    public <R> LQuery7<T1, T2, T3, T4, T5, T6, T7> orderBy(@Expr Func7<T1, T2, T3, T4, T5, T6, T7, R> expr, boolean asc)
    {
        throw new RuntimeException();
    }

    public <R> LQuery7<T1, T2, T3, T4, T5, T6, T7> orderBy(ExprTree<Func7<T1, T2, T3, T4, T5, T6, T7, R>> expr, boolean asc)
    {
        orderBy(expr.getTree(), asc);
        return this;
    }

    public <R> LQuery7<T1, T2, T3, T4, T5, T6, T7> orderBy(@Expr Func7<T1, T2, T3, T4, T5, T6, T7, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> LQuery7<T1, T2, T3, T4, T5, T6, T7> orderBy(ExprTree<Func7<T1, T2, T3, T4, T5, T6, T7, R>> expr)
    {
        orderBy(expr.getTree(), true);
        return this;
    }
    // endregion

    // region [LIMIT]
    public LQuery7<T1, T2, T3, T4, T5, T6, T7> limit(long rows)
    {
        limit0(rows);
        return this;
    }

    public LQuery7<T1, T2, T3, T4, T5, T6, T7> limit(long offset, long rows)
    {
        limit0(offset, rows);
        return this;
    }
    // endregion

    // region [GROUP BY]
    public <Key> GroupedQuery7<Key, T1, T2, T3, T4, T5, T6, T7> groupBy(@Expr Func7<T1, T2, T3, T4, T5, T6, T7, Key> expr)
    {
        throw new RuntimeException();
    }

    public <Key> GroupedQuery7<Key, T1, T2, T3, T4, T5, T6, T7> groupBy(ExprTree<Func7<T1, T2, T3, T4, T5, T6, T7, Key>> expr)
    {
        groupBy(expr.getTree());
        return new GroupedQuery7<>(getSqlBuilder());
    }
    // endregion

    // region [SELECT]
    public <R> EndQuery<R> select(Class<R> r)
    {
        return super.select(r);
    }

    public <R> LQuery<R> select(@Expr Func7<T1, T2, T3, T4, T5, T6, T7, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> LQuery<R> select(ExprTree<Func7<T1, T2, T3, T4, T5, T6, T7, R>> expr)
    {
        boolean single = select(expr.getTree());
        singleCheck(single);
        return new LQuery<>(this);
    }

    public <R> EndQuery<R> selectSingle(@Expr Func7<T1, T2, T3, T4, T5, T6, T7, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> EndQuery<R> selectSingle(ExprTree<Func7<T1, T2, T3, T4, T5, T6, T7, R>> expr)
    {
        select(expr.getTree());
        return new EndQuery<>(this);
    }
    // endregion

    //region [OTHER]

    public LQuery7<T1, T2, T3, T4, T5, T6, T7> distinct()
    {
        getSqlBuilder().setDistinct(true);
        return this;
    }

    public LQuery7<T1, T2, T3, T4, T5, T6, T7> distinct(boolean condition)
    {
        getSqlBuilder().setDistinct(condition);
        return this;
    }

    //endregion
}
