package io.github.kiryu1223.drink.api.crud.read.group;

import io.github.kiryu1223.drink.api.crud.builder.QuerySqlBuilder;
import io.github.kiryu1223.drink.api.crud.read.EndQuery;
import io.github.kiryu1223.drink.api.crud.read.LQuery;
import io.github.kiryu1223.drink.api.crud.read.QueryBase;
import io.github.kiryu1223.drink.exception.NotCompiledException;
import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

import java.util.List;

public class GroupedQuery10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> extends QueryBase
{
    public GroupedQuery10(QuerySqlBuilder sqlBuilder)
    {
        super(sqlBuilder);
    }

    // region [HAVING]
    public GroupedQuery10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> having(@Expr(Expr.BodyType.Expr) Func1<Group10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public GroupedQuery10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> having(ExprTree<Func1<Group10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, Boolean>> expr)
    {
        having(expr.getTree());
        return this;
    }

    // endregion

    // region [ORDER BY]
    public <R> GroupedQuery10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> orderBy(@Expr(Expr.BodyType.Expr) Func1<Group10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, R> expr, boolean asc)
    {
        throw new NotCompiledException();
    }

    public <R> GroupedQuery10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> orderBy(ExprTree<Func1<Group10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, R>> expr, boolean asc)
    {
        orderBy(expr.getTree(), asc);
        return this;
    }

    public <R> GroupedQuery10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> orderBy(@Expr(Expr.BodyType.Expr) Func1<Group10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, R> expr)
    {
        throw new NotCompiledException();
    }

    public <R> GroupedQuery10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> orderBy(ExprTree<Func1<Group10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, R>> expr)
    {
        orderBy(expr, true);
        return this;
    }
    // endregion

    // region [LIMIT]
    public GroupedQuery10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> limit(long rows)
    {
        limit0(rows);
        return this;
    }

    public GroupedQuery10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> limit(long offset, long rows)
    {
        limit0(offset, rows);
        return this;
    }
    // endregion

    // region [SELECT]
    public <R> LQuery<? extends R> select(@Expr Func1<Group10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, ? extends R> expr)
    {
        throw new NotCompiledException();
    }

    public <R> LQuery<? extends R> select(ExprTree<Func1<Group10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, ? extends R>> expr)
    {
        singleCheck(select(expr.getTree()));
        return new LQuery<>(boxedQuerySqlBuilder());
    }

    public <R> EndQuery<R> selectSingle(@Expr(Expr.BodyType.Expr) Func1<Group10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, R> expr)
    {
        throw new NotCompiledException();
    }

    public <R> EndQuery<R> selectSingle(ExprTree<Func1<Group10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, R>> expr)
    {
        select(expr.getTree());
        return new EndQuery<>(boxedQuerySqlBuilder());
    }
    // endregion

    @Override
    public boolean any()
    {
        return super.any();
    }

    @Override
    public List<Key> toList()
    {
        return super.toList();
    }

    public GroupedQuery10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> distinct()
    {
        distinct0(true);
        return this;
    }

    public GroupedQuery10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> distinct(boolean condition)
    {
        distinct0(condition);
        return this;
    }
}
