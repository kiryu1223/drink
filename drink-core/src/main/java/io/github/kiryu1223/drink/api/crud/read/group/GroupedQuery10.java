package io.github.kiryu1223.drink.api.crud.read.group;

import io.github.kiryu1223.drink.api.crud.builder.QuerySqlBuilder;
import io.github.kiryu1223.drink.api.crud.read.LQuery;
import io.github.kiryu1223.drink.api.crud.read.QueryBase;
import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

public class GroupedQuery10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> extends QueryBase
{
    public GroupedQuery10(QuerySqlBuilder sqlBuilder)
    {
        super(sqlBuilder);
    }

    // region [HAVING]
    public GroupedQuery10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> having(@Expr Func1<Group10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, Boolean> func)
    {
        throw new RuntimeException();
    }

    public GroupedQuery10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> having(ExprTree<Func1<Group10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, Boolean>> expr)
    {
        having(expr.getTree());
        return this;
    }

    // endregion

    // region [ORDER BY]
    public <R> GroupedQuery10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> orderBy(@Expr Func1<Group10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, R> expr, boolean asc)
    {
        throw new RuntimeException();
    }

    public <R> GroupedQuery10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> orderBy(ExprTree<Func1<Group10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, R>> expr, boolean asc)
    {
        orderBy(expr.getTree(), asc);
        return this;
    }

    public <R> GroupedQuery10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> orderBy(@Expr Func1<Group10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> GroupedQuery10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> orderBy(ExprTree<Func1<Group10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, R>> expr)
    {
        orderBy(expr, true);
        return this;
    }
    // endregion

    // region [SELECT]
    public <R> LQuery<R> select(@Expr Func1<Group10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> LQuery<R> select(ExprTree<Func1<Group10<Key, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, R>> expr)
    {
        singleCheck(select(expr.getTree()));
        return new LQuery<>(this);
    }
    // endregion
}
