package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.api.crud.read.group.GroupedQuery2;
import io.github.kiryu1223.drink.core.expression.JoinType;
import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;
import io.github.kiryu1223.drink.exception.NotCompiledException;
import io.github.kiryu1223.expressionTree.delegate.Func2;
import io.github.kiryu1223.expressionTree.delegate.Func3;
import io.github.kiryu1223.expressionTree.expressions.annos.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;
import io.github.kiryu1223.expressionTree.expressions.annos.Recode;


public class LQuery2<T1, T2> extends QueryBase
{
    // region [INIT]

    public LQuery2(QuerySqlBuilder sqlBuilder)
    {
        super(sqlBuilder);
    }

    // endregion

    //region [JOIN]

    protected <Tn> LQuery3<T1, T2, Tn> joinNewQuery()
    {
        return new LQuery3<>(getSqlBuilder());
    }

    public <Tn> LQuery3<T1, T2, Tn> innerJoin(Class<Tn> target, @Expr(Expr.BodyType.Expr) Func3<T1, T2, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery3<T1, T2, Tn> innerJoin(Class<Tn> target, ExprTree<Func3<T1, T2, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery3<T1, T2, Tn> innerJoin(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func3<T1, T2, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery3<T1, T2, Tn> innerJoin(LQuery<Tn> target, ExprTree<Func3<T1, T2, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery3<T1, T2, Tn> leftJoin(Class<Tn> target, @Expr(Expr.BodyType.Expr) Func3<T1, T2, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery3<T1, T2, Tn> leftJoin(Class<Tn> target, ExprTree<Func3<T1, T2, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery3<T1, T2, Tn> leftJoin(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func3<T1, T2, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery3<T1, T2, Tn> leftJoin(LQuery<Tn> target, ExprTree<Func3<T1, T2, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery3<T1, T2, Tn> rightJoin(Class<Tn> target, @Expr(Expr.BodyType.Expr) Func3<T1, T2, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery3<T1, T2, Tn> rightJoin(Class<Tn> target, ExprTree<Func3<T1, T2, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery3<T1, T2, Tn> rightJoin(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func3<T1, T2, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery3<T1, T2, Tn> rightJoin(LQuery<Tn> target, ExprTree<Func3<T1, T2, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return joinNewQuery();
    }
    // endregion

    // region [WHERE]
    public LQuery2<T1, T2> where(@Expr(Expr.BodyType.Expr) Func2<T1, T2, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public LQuery2<T1, T2> where(ExprTree<Func2<T1, T2, Boolean>> expr)
    {
        where(expr.getTree());
        return this;
    }

    public LQuery2<T1, T2> orWhere(@Expr(Expr.BodyType.Expr) Func2<T1, T2, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public LQuery2<T1, T2> orWhere(ExprTree<Func2<T1, T2, Boolean>> expr)
    {
        orWhere(expr.getTree());
        return this;
    }

    public <E> LQuery2<T1, T2> exists(Class<E> table, @Expr(Expr.BodyType.Expr) Func3<T1, T2, E, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <E> LQuery2<T1, T2> exists(Class<E> table, ExprTree<Func3<T1, T2, E, Boolean>> expr)
    {
        exists(table, expr.getTree(), false);
        return this;
    }

    public <E> LQuery2<T1, T2> exists(LQuery<E> query, @Expr(Expr.BodyType.Expr) Func3<T1, T2, E, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <E> LQuery2<T1, T2> exists(LQuery<E> query, ExprTree<Func3<T1, T2, E, Boolean>> expr)
    {
        exists(query, expr.getTree(), false);
        return this;
    }

    public <E> LQuery2<T1, T2> notExists(Class<E> table, @Expr(Expr.BodyType.Expr) Func3<T1, T2, E, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <E> LQuery2<T1, T2> notExists(Class<E> table, ExprTree<Func3<T1, T2, E, Boolean>> expr)
    {
        exists(table, expr.getTree(), true);
        return this;
    }

    public <E> LQuery2<T1, T2> notExists(LQuery<E> query, @Expr(Expr.BodyType.Expr) Func3<T1, T2, E, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <E> LQuery2<T1, T2> notExists(LQuery<E> query, ExprTree<Func3<T1, T2, E, Boolean>> expr)
    {
        exists(query, expr.getTree(), true);
        return this;
    }
    // endregion

    // region [ORDER BY]
    public <R> LQuery2<T1, T2> orderBy(@Expr(Expr.BodyType.Expr) Func2<T1, T2, R> expr, boolean asc)
    {
        throw new NotCompiledException();
    }

    public <R> LQuery2<T1, T2> orderBy(ExprTree<Func2<T1, T2, R>> expr, boolean asc)
    {
        orderBy(expr.getTree(), asc);
        return this;
    }

    public <R> LQuery2<T1, T2> orderBy(@Expr(Expr.BodyType.Expr) Func2<T1, T2, R> expr)
    {
        throw new NotCompiledException();
    }

    public <R> LQuery2<T1, T2> orderBy(ExprTree<Func2<T1, T2, R>> expr)
    {
        orderBy(expr.getTree(), true);
        return this;
    }
    // endregion

    // region [LIMIT]
    public LQuery2<T1, T2> limit(long rows)
    {
        limit0(rows);
        return this;
    }

    public LQuery2<T1, T2> limit(long offset, long rows)
    {
        limit0(offset, rows);
        return this;
    }
    // endregion

    // region [GROUP BY]
    public <Key> GroupedQuery2<Key, T1, T2> groupBy(@Expr Func2<T1, T2, Key> expr)
    {
        throw new NotCompiledException();
    }

    public <Key> GroupedQuery2<Key, T1, T2> groupBy(ExprTree<Func2<T1, T2, Key>> expr)
    {
        groupBy(expr.getTree());
        return new GroupedQuery2<>(getSqlBuilder());
    }
    // endregion

    // region [SELECT]

    public <R> EndQuery<R> select(@Recode Class<R> r)
    {
        return super.select(r);
    }

    public <R> LQuery<? extends R> select(@Expr Func2<T1, T2, ? extends R> expr)
    {
        throw new NotCompiledException();
    }

    public <R> LQuery<? extends R> select(ExprTree<Func2<T1, T2, ? extends R>> expr)
    {
        boolean single = select(expr.getTree());
        singleCheck(single);
        return new LQuery<>(boxedQuerySqlBuilder());
    }

    public <R> EndQuery<R> endSelect(@Expr(Expr.BodyType.Expr) Func2<T1, T2, R> expr)
    {
        throw new NotCompiledException();
    }

    public <R> EndQuery<R> endSelect(ExprTree<Func2<T1, T2, R>> expr)
    {
        select(expr.getTree());
        return new EndQuery<>(getSqlBuilder());
    }
    // endregion

    //region [OTHER]

    public LQuery2<T1, T2> distinct()
    {
        distinct0(true);
        return this;
    }

    public LQuery2<T1, T2> distinct(boolean condition)
    {
        distinct0(condition);
        return this;
    }

    //endregion

    // region [toAny]


//    public String toSQL()
//    {
//        return clientQueryable.toSQL();
//    }
//
//    public ToSQLResult toSQLResult()
//    {
//        return clientQueryable.toSQLResult();
//    }
//
//    public List<T1> toList()
//    {
//        return clientQueryable.toList();
//    }
//
//    public <R> List<R> toList(Func1<T1, R> func)
//    {
//        List<R> rList = new ArrayList<>();
//        for (T1 t : toList())
//        {
//            rList.add(func.invoke(t));
//        }
//        return rList;
//    }
//
//    public Map<String, Object> toMap()
//    {
//        return clientQueryable.toMap();
//    }
//
//    public List<Map<String, Object>> toMaps()
//    {
//        return clientQueryable.toMaps();
//    }

//    public EasyPageResult<T1> toPageResult(long pageIndex, long pageSize)
//    {
//        return clientQueryable.toPageResult(pageIndex, pageSize);
//    }
//
//    public EasyPageResult<T1> toPageResult(long pageIndex, long pageSize, long pageTotal)
//    {
//        return clientQueryable.toPageResult(pageIndex, pageSize, pageTotal);
//    }
//
//    public <TPageResult> TPageResult toPageResult(Pager<T1, TPageResult> pager)
//    {
//        return clientQueryable.toPageResult(pager);
//    }

    // endregion
}
