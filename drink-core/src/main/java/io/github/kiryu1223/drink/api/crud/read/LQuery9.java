package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.api.crud.read.group.GroupedQuery9;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.JoinType;
import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.delegate.Func10;
import io.github.kiryu1223.expressionTree.delegate.Func9;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, T9> extends QueryBase
{
    // region [INIT]

    public LQuery9(Config config)
    {
        super(config);
    }

    public LQuery9(Config config, Class<?> c1, Class<?> c2, Class<?> c3, Class<?> c4, Class<?> c5, Class<?> c6, Class<?> c7, Class<?> c8, Class<?> c9)
    {
        super(config);
        getSqlBuilder().addFrom(c1, c2, c3, c4, c5, c6, c7, c8, c9);
    }

    public LQuery9(QueryBase q1, QueryBase q2, QueryBase q3, QueryBase q4, QueryBase q5, QueryBase q6, QueryBase q7, QueryBase q8, QueryBase q9)
    {
        super(q1.getConfig());
        getSqlBuilder().addFrom(q1.getSqlBuilder(), q2.getSqlBuilder(), q3.getSqlBuilder(), q4.getSqlBuilder(), q5.getSqlBuilder(), q6.getSqlBuilder(), q7.getSqlBuilder(), q8.getSqlBuilder(), q9.getSqlBuilder());
    }

    // endregion

    //region [JOIN]

    @Override
    protected <Tn> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> joinNewQuery()
    {
        LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> query = new LQuery10<>(getConfig());
        query.getSqlBuilder().joinBy(getSqlBuilder());
        return query;
    }

    public <Tn> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> innerJoin(Class<Tn> target, @Expr Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> innerJoin(Class<Tn> target, ExprTree<Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> innerJoin(LQuery<Tn> target, @Expr Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> innerJoin(LQuery<Tn> target, ExprTree<Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> leftJoin(Class<Tn> target, @Expr Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> leftJoin(Class<Tn> target, ExprTree<Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> leftJoin(LQuery<Tn> target, @Expr Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> leftJoin(LQuery<Tn> target, ExprTree<Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> rightJoin(Class<Tn> target, @Expr Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> rightJoin(Class<Tn> target, ExprTree<Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> rightJoin(LQuery<Tn> target, @Expr Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> rightJoin(LQuery<Tn> target, ExprTree<Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return joinNewQuery();
    }

    // endregion

    // region [WHERE]
    public LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, T9> where(@Expr Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Boolean> func)
    {
        throw new RuntimeException();
    }

    public LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, T9> where(ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Boolean>> expr)
    {
        where(expr.getTree());
        return this;
    }
    // endregion

    // region [ORDER BY]
    public <R> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, T9> orderBy(@Expr Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> expr, boolean asc)
    {
        throw new RuntimeException();
    }

    public <R> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, T9> orderBy(ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>> expr, boolean asc)
    {
        orderBy(expr.getTree(), asc);
        return this;
    }

    public <R> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, T9> orderBy(@Expr Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, T9> orderBy(ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>> expr)
    {
        orderBy(expr.getTree(), true);
        return this;
    }
    // endregion

    // region [LIMIT]
    public LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, T9> limit(long rows)
    {
        limit0(rows);
        return this;
    }

    public LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, T9> limit(long offset, long rows)
    {
        limit0(offset, rows);
        return this;
    }
    // endregion

    // region [GROUP BY]
    public <Key> GroupedQuery9<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9> groupBy(@Expr Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Key> expr)
    {
        throw new RuntimeException();
    }

    public <Key> GroupedQuery9<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9> groupBy(ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Key>> expr)
    {
        groupBy(expr.getTree());
        return new GroupedQuery9<>(getSqlBuilder());
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
    public <R> LQuery<R> select(@Expr Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> LQuery<R> select(ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>> expr)
    {
        boolean single = select(expr.getTree());
        singleCheck(single);
        return new LQuery<>(this);
    }

    // endregion

    //region [OTHER]

    public LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, T9> distinct()
    {
        getSqlBuilder().setDistinct(true);
        return this;
    }

    public LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, T9> distinct(boolean condition)
    {
        getSqlBuilder().setDistinct(condition);
        return this;
    }

    //endregion

    public String toSql()
    {
        return getSqlBuilder().getSql();
    }
}
