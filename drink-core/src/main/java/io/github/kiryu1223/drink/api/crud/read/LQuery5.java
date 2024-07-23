package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;
import io.github.kiryu1223.drink.api.crud.read.group.GroupedQuery5;
import io.github.kiryu1223.drink.core.context.JoinType;
import io.github.kiryu1223.drink.exception.NotCompiledException;
import io.github.kiryu1223.expressionTree.delegate.Func5;
import io.github.kiryu1223.expressionTree.delegate.Func6;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

public class LQuery5<T1, T2, T3, T4, T5> extends QueryBase
{
    // region [INIT]

    public LQuery5(QuerySqlBuilder sqlBuilder)
    {
        super(sqlBuilder);
    }

    // endregion

    //region [JOIN]

    @Override
    protected <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> joinNewQuery()
    {
        return new LQuery6<>(getSqlBuilder());
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> innerJoin(Class<Tn> target, @Expr(Expr.BodyType.Expr) Func6<T1, T2, T3, T4, T5, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> innerJoin(Class<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> innerJoin(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func6<T1, T2, T3, T4, T5, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> innerJoin(LQuery<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> leftJoin(Class<Tn> target, @Expr(Expr.BodyType.Expr) Func6<T1, T2, T3, T4, T5, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> leftJoin(Class<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> leftJoin(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func6<T1, T2, T3, T4, T5, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> leftJoin(LQuery<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> rightJoin(Class<Tn> target, @Expr(Expr.BodyType.Expr) Func6<T1, T2, T3, T4, T5, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> rightJoin(Class<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> rightJoin(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func6<T1, T2, T3, T4, T5, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> rightJoin(LQuery<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return joinNewQuery();
    }

    // endregion

    // region [WHERE]
    public LQuery5<T1, T2, T3, T4, T5> where(@Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public LQuery5<T1, T2, T3, T4, T5> where(ExprTree<Func5<T1, T2, T3, T4, T5, Boolean>> expr)
    {
        where(expr.getTree());
        return this;
    }

    public LQuery5<T1, T2, T3, T4, T5> orWhere(@Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public LQuery5<T1, T2, T3, T4, T5> orWhere(ExprTree<Func5<T1, T2, T3, T4, T5, Boolean>> expr)
    {
        orWhere(expr.getTree());
        return this;
    }

    public <E> LQuery5<T1, T2, T3, T4, T5> exists(Class<E> table, @Expr(Expr.BodyType.Expr) Func6<T1, T2, T3, T4, T5, E, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <E> LQuery5<T1, T2, T3, T4, T5> exists(Class<E> table, ExprTree<Func6<T1, T2, T3, T4, T5, E, Boolean>> expr)
    {
        exists(table, expr.getTree(),false);
        return this;
    }

    public <E> LQuery5<T1, T2, T3, T4, T5> exists(LQuery<E> query, @Expr(Expr.BodyType.Expr) Func6<T1, T2, T3, T4, T5, E, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <E> LQuery5<T1, T2, T3, T4, T5> exists(LQuery<E> query, ExprTree<Func6<T1, T2, T3, T4, T5, E, Boolean>> expr)
    {
        exists(query, expr.getTree(),false);
        return this;
    }

    public <E> LQuery5<T1, T2, T3, T4, T5> notExists(Class<E> table, @Expr(Expr.BodyType.Expr) Func6<T1, T2, T3, T4, T5, E, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <E> LQuery5<T1, T2, T3, T4, T5> notExists(Class<E> table, ExprTree<Func6<T1, T2, T3, T4, T5, E, Boolean>> expr)
    {
        exists(table, expr.getTree(),true);
        return this;
    }

    public <E> LQuery5<T1, T2, T3, T4, T5> notExists(LQuery<E> query, @Expr(Expr.BodyType.Expr) Func6<T1, T2, T3, T4, T5, E, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <E> LQuery5<T1, T2, T3, T4, T5> notExists(LQuery<E> query, ExprTree<Func6<T1, T2, T3, T4, T5, E, Boolean>> expr)
    {
        exists(query, expr.getTree(),true);
        return this;
    }
    // endregion

    // region [ORDER BY]
    public <R> LQuery5<T1, T2, T3, T4, T5> orderBy(@Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, R> expr, boolean asc)
    {
        throw new NotCompiledException();
    }

    public <R> LQuery5<T1, T2, T3, T4, T5> orderBy(ExprTree<Func5<T1, T2, T3, T4, T5, R>> expr, boolean asc)
    {
        orderBy(expr.getTree(), asc);
        return this;
    }

    public <R> LQuery5<T1, T2, T3, T4, T5> orderBy(@Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, R> expr)
    {
        throw new NotCompiledException();
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
        throw new NotCompiledException();
    }

    public <Key> GroupedQuery5<Key, T1, T2, T3, T4, T5> groupBy(ExprTree<Func5<T1, T2, T3, T4, T5, Key>> expr)
    {
        groupBy(expr.getTree());
        return new GroupedQuery5<>(getSqlBuilder());
    }
    // endregion

    // region [SELECT]
    public <R> EndQuery<R> select(Class<R> r)
    {
        return super.select(r);
    }

    public <R> LQuery<? extends R> select(@Expr Func5<T1, T2, T3, T4, T5, ? extends R> expr)
    {
        throw new NotCompiledException();
    }

    public <R> LQuery<? extends R> select(ExprTree<Func5<T1, T2, T3, T4, T5, ? extends R>> expr)
    {
        boolean single = select(expr.getTree());
        singleCheck(single);
        return new LQuery<>(boxedQuerySqlBuilder());
    }

    public <R> EndQuery<R> endSelect(@Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, R> expr)
    {
        throw new NotCompiledException();
    }

    public <R> EndQuery<R> endSelect(ExprTree<Func5<T1, T2, T3, T4, T5, R>> expr)
    {
        select(expr.getTree());
        return new EndQuery<>(boxedQuerySqlBuilder());
    }
    // endregion

    //region [OTHER]

    public LQuery5<T1, T2, T3, T4, T5> distinct()
    {
        distinct0(true);
        return this;
    }

    public LQuery5<T1, T2, T3, T4, T5> distinct(boolean condition)
    {
        distinct0(condition);
        return this;
    }

    //endregion

    // region [toAny]
    // endregion
}
