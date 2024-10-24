package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.base.expression.JoinType;
import io.github.kiryu1223.drink.sqlBuilder.QuerySqlBuilder;
import io.github.kiryu1223.drink.api.crud.read.group.GroupedQuery4;
import io.github.kiryu1223.drink.exception.NotCompiledException;
import io.github.kiryu1223.expressionTree.delegate.Func4;
import io.github.kiryu1223.expressionTree.delegate.Func5;
import io.github.kiryu1223.expressionTree.expressions.annos.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;
import io.github.kiryu1223.expressionTree.expressions.annos.Recode;

public class LQuery4<T1, T2, T3, T4> extends QueryBase
{
    // region [INIT]

    public LQuery4(QuerySqlBuilder sqlBuilder)
    {
        super(sqlBuilder);
    }

    // endregion

    //region [JOIN]

    @Override
    protected <Tn> LQuery5<T1, T2, T3, T4, Tn> joinNewQuery()
    {
        return new LQuery5<>(getSqlBuilder());
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> innerJoin(Class<Tn> target, @Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> innerJoin(Class<Tn> target, ExprTree<Func5<T1, T2, T3, T4, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> innerJoin(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> innerJoin(LQuery<Tn> target, ExprTree<Func5<T1, T2, T3, T4, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> leftJoin(Class<Tn> target, @Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> leftJoin(Class<Tn> target, ExprTree<Func5<T1, T2, T3, T4, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> leftJoin(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> leftJoin(LQuery<Tn> target, ExprTree<Func5<T1, T2, T3, T4, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> rightJoin(Class<Tn> target, @Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> rightJoin(Class<Tn> target, ExprTree<Func5<T1, T2, T3, T4, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> rightJoin(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> rightJoin(LQuery<Tn> target, ExprTree<Func5<T1, T2, T3, T4, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return joinNewQuery();
    }

    // endregion

    // region [WHERE]
    public LQuery4<T1, T2, T3, T4> where(@Expr(Expr.BodyType.Expr) Func4<T1, T2, T3, T4, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public LQuery4<T1, T2, T3, T4> where(ExprTree<Func4<T1, T2, T3, T4, Boolean>> expr)
    {
        where(expr.getTree());
        return this;
    }

    public LQuery4<T1, T2, T3, T4> orWhere(@Expr(Expr.BodyType.Expr) Func4<T1, T2, T3, T4, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public LQuery4<T1, T2, T3, T4> orWhere(ExprTree<Func4<T1, T2, T3, T4, Boolean>> expr)
    {
        orWhere(expr.getTree());
        return this;
    }

    public <E> LQuery4<T1, T2, T3, T4> exists(Class<E> table, @Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, E, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <E> LQuery4<T1, T2, T3, T4> exists(Class<E> table, ExprTree<Func5<T1, T2, T3, T4, E, Boolean>> expr)
    {
        exists(table, expr.getTree(),false);
        return this;
    }

    public <E> LQuery4<T1, T2, T3, T4> exists(LQuery<E> query, @Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, E, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <E> LQuery4<T1, T2, T3, T4> exists(LQuery<E> query, ExprTree<Func5<T1, T2, T3, T4, E, Boolean>> expr)
    {
        exists(query, expr.getTree(),false);
        return this;
    }

    public <E> LQuery4<T1, T2, T3, T4> notExists(Class<E> table, @Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, E, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <E> LQuery4<T1, T2, T3, T4> notExists(Class<E> table, ExprTree<Func5<T1, T2, T3, T4, E, Boolean>> expr)
    {
        exists(table, expr.getTree(),true);
        return this;
    }

    public <E> LQuery4<T1, T2, T3, T4> notExists(LQuery<E> query, @Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, E, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <E> LQuery4<T1, T2, T3, T4> notExists(LQuery<E> query, ExprTree<Func5<T1, T2, T3, T4, E, Boolean>> expr)
    {
        exists(query, expr.getTree(),true);
        return this;
    }
    // endregion

    // region [ORDER BY]
    public <R> LQuery4<T1, T2, T3, T4> orderBy(@Expr(Expr.BodyType.Expr) Func4<T1, T2, T3, T4, R> expr, boolean asc)
    {
        throw new NotCompiledException();
    }

    public <R> LQuery4<T1, T2, T3, T4> orderBy(ExprTree<Func4<T1, T2, T3, T4, R>> expr, boolean asc)
    {
        orderBy(expr.getTree(), asc);
        return this;
    }

    public <R> LQuery4<T1, T2, T3, T4> orderBy(@Expr(Expr.BodyType.Expr) Func4<T1, T2, T3, T4, R> expr)
    {
        throw new NotCompiledException();
    }

    public <R> LQuery4<T1, T2, T3, T4> orderBy(ExprTree<Func4<T1, T2, T3, T4, R>> expr)
    {
        orderBy(expr, true);
        return this;
    }
    // endregion

    // region [LIMIT]
    public LQuery4<T1, T2, T3, T4> limit(long rows)
    {
        limit0(rows);
        return this;
    }

    public LQuery4<T1, T2, T3, T4> limit(long offset, long rows)
    {
        limit0(offset, rows);
        return this;
    }
    // endregion

    // region [GROUP BY]
    public <Key> GroupedQuery4<Key, T1, T2, T3, T4> groupBy(@Expr Func4<T1, T2, T3, T4, Key> expr)
    {
        throw new NotCompiledException();
    }

    public <Key> GroupedQuery4<Key, T1, T2, T3, T4> groupBy(ExprTree<Func4<T1, T2, T3, T4, Key>> expr)
    {
        groupBy(expr.getTree());
        return new GroupedQuery4<>(getSqlBuilder());
    }
    // endregion

    // region [SELECT]
    public <R> EndQuery<R> select(@Recode Class<R> r)
    {
        return super.select(r);
    }

    public <R> LQuery<? extends R> select(@Expr Func4<T1, T2, T3, T4, ? extends R> expr)
    {
        throw new NotCompiledException();
    }

    public <R> LQuery<? extends R> select(ExprTree<Func4<T1, T2, T3, T4, ? extends R>> expr)
    {
        boolean single = select(expr.getTree());
        singleCheck(single);
        return new LQuery<>(boxedQuerySqlBuilder());
    }

    public <R> EndQuery<R> endSelect(@Expr(Expr.BodyType.Expr) Func4<T1, T2, T3, T4, R> expr)
    {
        throw new NotCompiledException();
    }

    public <R> EndQuery<R> endSelect(ExprTree<Func4<T1, T2, T3, T4, R>> expr)
    {
        select(expr.getTree());
        return new EndQuery<>(getSqlBuilder());
    }
    // endregion
}
