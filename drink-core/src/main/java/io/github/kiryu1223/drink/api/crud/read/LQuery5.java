package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.api.crud.read.group.GroupedQuery5;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.JoinType;
import io.github.kiryu1223.expressionTree.delegate.Func4;
import io.github.kiryu1223.expressionTree.delegate.Func5;
import io.github.kiryu1223.expressionTree.delegate.Func6;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

public class LQuery5<T1, T2, T3, T4, T5> extends QueryBase
{
    // region [INIT]

    public LQuery5(Config config)
    {
        super(config);
    }

    public LQuery5(Config config, Class<?> c1, Class<?> c2, Class<?> c3, Class<?> c4, Class<?> c5)
    {
        super(config);
        getSqlBuilder().addFrom(c1, c2, c3, c4, c5);
    }

    public LQuery5(QueryBase q1, QueryBase q2, QueryBase q3, QueryBase q4, QueryBase q5)
    {
        super(q1.getConfig());
        getSqlBuilder().addFrom(q1.getSqlBuilder(), q2.getSqlBuilder(), q3.getSqlBuilder(), q4.getSqlBuilder(), q5.getSqlBuilder());
    }

    // endregion

    //region [JOIN]

    @Override
    protected <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> joinNewQuery()
    {
        LQuery6<T1, T2, T3, T4, T5, Tn> query = new LQuery6<>(getConfig());
        query.getSqlBuilder().joinBy(getSqlBuilder());
        return query;
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> innerJoin(Class<Tn> target, @Expr Func6<T1, T2, T3, T4, T5, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> innerJoin(Class<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> innerJoin(LQuery<Tn> target, @Expr Func6<T1, T2, T3, T4, T5, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> innerJoin(LQuery<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> leftJoin(Class<Tn> target, @Expr Func6<T1, T2, T3, T4, T5, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> leftJoin(Class<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> leftJoin(LQuery<Tn> target, @Expr Func6<T1, T2, T3, T4, T5, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> leftJoin(LQuery<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> rightJoin(Class<Tn> target, @Expr Func6<T1, T2, T3, T4, T5, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> rightJoin(Class<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> rightJoin(LQuery<Tn> target, @Expr Func6<T1, T2, T3, T4, T5, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> rightJoin(LQuery<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return joinNewQuery();
    }

    // endregion

    // region [WHERE]
    public LQuery5<T1, T2, T3, T4, T5> where(@Expr Func5<T1, T2, T3, T4, T5, Boolean> func)
    {
        throw new RuntimeException();
    }

    public LQuery5<T1, T2, T3, T4, T5> where(ExprTree<Func5<T1, T2, T3, T4, T5, Boolean>> expr)
    {
        where(expr.getTree());
        return this;
    }
    // endregion

    // region [ORDER BY]
    public <R> LQuery5<T1, T2, T3, T4, T5> orderBy(@Expr Func5<T1, T2, T3, T4, T5, R> expr, boolean asc)
    {
        throw new RuntimeException();
    }

    public <R> LQuery5<T1, T2, T3, T4, T5> orderBy(ExprTree<Func5<T1, T2, T3, T4, T5, R>> expr, boolean asc)
    {
        orderBy(expr.getTree(), asc);
        return this;
    }

    public <R> LQuery5<T1, T2, T3, T4, T5> orderBy(@Expr Func5<T1, T2, T3, T4, T5, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> LQuery5<T1, T2, T3, T4, T5> orderBy(ExprTree<Func5<T1, T2, T3, T4, T5, R>> expr)
    {
        orderBy(expr, true);
        return this;
    }
    // endregion

    // region [LIMIT]
    public LQuery5<T1, T2, T3, T4, T5> limit(long rows)
    {
        limit0(rows);
        return this;
    }

    public LQuery5<T1, T2, T3, T4, T5> limit(long offset, long rows)
    {
        limit0(offset, rows);
        return this;
    }
    // endregion

    // region [GROUP BY]
    public <Key> GroupedQuery5<Key, T1, T2, T3, T4, T5> groupBy(@Expr Func5<T1, T2, T3, T4, T5, Key> expr)
    {
        throw new RuntimeException();
    }

    public <Key> GroupedQuery5<Key, T1, T2, T3, T4, T5> groupBy(ExprTree<Func5<T1, T2, T3, T4, T5, Key>> expr)
    {
        groupBy(expr.getTree());
        return new GroupedQuery5<>(getSqlBuilder());
    }
    // endregion

    // region [SELECT]
    public LQuery<T1> select()
    {
        return new LQuery<>(this);
    }
    public <R> EndQuery<R> select(Class<R> r)
    {
        return super.select(r);
    }
    public <R> LQuery<R> select(@Expr Func5<T1, T2, T3, T4, T5, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> LQuery<R> select(ExprTree<Func5<T1, T2, T3, T4, T5, R>> expr)
    {
        boolean single = select(expr.getTree());
        singleCheck(single);
        return new LQuery<>(this);
    }
    public <R> EndQuery<R> selectSingle(@Expr Func5<T1, T2, T3, T4, T5, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> EndQuery<R> selectSingle(ExprTree<Func5<T1, T2, T3, T4, T5, R>> expr)
    {
        select(expr.getTree());
        return new EndQuery<>(this);
    }
    // endregion

    //region [OTHER]

    public LQuery5<T1, T2, T3, T4, T5> distinct()
    {
        getSqlBuilder().setDistinct(true);
        return this;
    }

    public LQuery5<T1, T2, T3, T4, T5> distinct(boolean condition)
    {
        getSqlBuilder().setDistinct(condition);
        return this;
    }

    //endregion

    // region [toAny]
    public String toSql()
    {
        return getSqlBuilder().getSql();
    }
    // endregion
}
