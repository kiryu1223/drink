package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;
import io.github.kiryu1223.drink.api.crud.read.group.GroupedQuery10;
import io.github.kiryu1223.drink.exception.NotCompiledException;
import io.github.kiryu1223.expressionTree.delegate.Func10;
import io.github.kiryu1223.expressionTree.delegate.Func11;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

public class LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> extends QueryBase
{
    // region [INIT]

    public LQuery10(QuerySqlBuilder sqlBuilder)
    {
        super(sqlBuilder);
    }

    // endregion

    // region [WHERE]
    public LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> where(@Expr(Expr.BodyType.Expr) Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> where(ExprTree<Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Boolean>> expr)
    {
        where(expr.getTree());
        return this;
    }

    public LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> orWhere(@Expr(Expr.BodyType.Expr) Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> orWhere(ExprTree<Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Boolean>> expr)
    {
        orWhere(expr.getTree());
        return this;
    }

    public <E> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> exists(Class<E> table, @Expr(Expr.BodyType.Expr) Func11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <E> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> exists(Class<E> table, ExprTree<Func11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E, Boolean>> expr)
    {
        exists(table, expr.getTree(), false);
        return this;
    }

    public <E> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> exists(LQuery<E> query, @Expr(Expr.BodyType.Expr) Func11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <E> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> exists(LQuery<E> query, ExprTree<Func11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E, Boolean>> expr)
    {
        exists(query, expr.getTree(), false);
        return this;
    }

    public <E> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> notExists(Class<E> table, @Expr(Expr.BodyType.Expr) Func11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <E> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> notExists(Class<E> table, ExprTree<Func11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E, Boolean>> expr)
    {
        exists(table, expr.getTree(), true);
        return this;
    }

    public <E> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> notExists(LQuery<E> query, @Expr(Expr.BodyType.Expr) Func11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <E> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> notExists(LQuery<E> query, ExprTree<Func11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E, Boolean>> expr)
    {
        exists(query, expr.getTree(), true);
        return this;
    }
    // endregion

    // region [ORDER BY]
    public <R> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> orderBy(@Expr(Expr.BodyType.Expr) Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> expr, boolean asc)
    {
        throw new NotCompiledException();
    }

    public <R> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> orderBy(ExprTree<Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R>> expr, boolean asc)
    {
        orderBy(expr.getTree(), asc);
        return this;
    }

    public <R> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> orderBy(@Expr(Expr.BodyType.Expr) Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> expr)
    {
        throw new NotCompiledException();
    }

    public <R> LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> orderBy(ExprTree<Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R>> expr)
    {
        orderBy(expr.getTree(), true);
        return this;
    }
    // endregion

    // region [LIMIT]
    public LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> limit(long rows)
    {
        limit0(rows);
        return this;
    }

    public LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> limit(long offset, long rows)
    {
        limit0(offset, rows);
        return this;
    }
    // endregion

    // region [GROUP BY]
    public <Key> GroupedQuery10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> groupBy(@Expr Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Key> expr)
    {
        throw new NotCompiledException();
    }

    public <Key> GroupedQuery10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> groupBy(ExprTree<Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Key>> expr)
    {
        groupBy(expr.getTree());
        return new GroupedQuery10<>(getSqlBuilder());
    }
    // endregion

    // region [SELECT]
    public <R> EndQuery<R> select(Class<R> r)
    {
        return super.select(r);
    }

    public <R> LQuery<? extends R> select(@Expr Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, ? extends R> expr)
    {
        throw new NotCompiledException();
    }

    public <R> LQuery<? extends R> select(ExprTree<Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, ? extends R>> expr)
    {
        boolean single = select(expr.getTree());
        singleCheck(single);
        return new LQuery<>(boxedQuerySqlBuilder());
    }

    public <R> EndQuery<R> endSelect(@Expr(Expr.BodyType.Expr) Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> expr)
    {
        throw new NotCompiledException();
    }

    public <R> EndQuery<R> endSelect(ExprTree<Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R>> expr)
    {
        select(expr.getTree());
        return new EndQuery<>(boxedQuerySqlBuilder());
    }

    // endregion

    //region [OTHER]

    public LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> distinct()
    {
        distinct0(true);
        return this;
    }

    public LQuery10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> distinct(boolean condition)
    {
        distinct0(condition);
        return this;
    }
    //endregion

}
