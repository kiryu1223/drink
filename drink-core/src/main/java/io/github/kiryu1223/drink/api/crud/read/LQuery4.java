package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.api.crud.builder.QuerySqlBuilder;
import io.github.kiryu1223.drink.api.crud.read.group.GroupedQuery4;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.JoinType;
import io.github.kiryu1223.drink.exception.NotCompiledException;
import io.github.kiryu1223.expressionTree.delegate.Func3;
import io.github.kiryu1223.expressionTree.delegate.Func4;
import io.github.kiryu1223.expressionTree.delegate.Func5;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

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
    public <R> EndQuery<R> select(Class<R> r)
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

    public <R> EndQuery<R> selectSingle(@Expr(Expr.BodyType.Expr) Func4<T1, T2, T3, T4, R> expr)
    {
        throw new NotCompiledException();
    }

    public <R> EndQuery<R> selectSingle(ExprTree<Func4<T1, T2, T3, T4, R>> expr)
    {
        select(expr.getTree());
        return new EndQuery<>(boxedQuerySqlBuilder());
    }
    // endregion

//    // region [INCLUDE]
//    public <R> LQuery4<T1, T2, T3, T4> include(@Expr Func4<T1, T2, T3, T4, R> expr, int groupSize)
//    {
//        throw new NotCompiledException();
//    }
//
//    public <R> LQuery4<T1, T2, T3, T4> include(ExprTree<Func4<T1, T2, T3, T4, R>> expr, int groupSize)
//    {
//        Include include = new Include(expr.getTree(), groupSize);
//        include.analysis(clientQueryable, queryData);
//        return this;
//    }
//
//    public <R> LQuery4<T1, T2, T3, T4> include(@Expr Func4<T1, T2, T3, T4, R> expr)
//    {
//        throw new NotCompiledException();
//    }
//
//    public <R> LQuery4<T1, T2, T3, T4> include(ExprTree<Func4<T1, T2, T3, T4, R>> expr)
//    {
//        Include include = new Include(expr.getTree());
//        include.analysis(clientQueryable, queryData);
//        return this;
//    }
//    // endregion
//
//    // region [UNION]
//
//    public LQuery<T1> union(LQuery<T1> q1)
//    {
//        return new LQuery<>(clientQueryable.union(q1.getClientQueryable()), queryData.getDbType());
//    }
//
//    public LQuery<T1> union(LQuery<T1> q1, LQuery<T1> q2)
//    {
//        return new LQuery<>(clientQueryable.union(q1.getClientQueryable(), q2.getClientQueryable()), queryData.getDbType());
//    }
//
//    public LQuery<T1> union(LQuery<T1> q1, LQuery<T1> q2, LQuery<T1> q3)
//    {
//        return new LQuery<>(clientQueryable.union(q1.getClientQueryable(), q2.getClientQueryable(), q3.getClientQueryable()), queryData.getDbType());
//    }
//
//    public LQuery<T1> union(Collection<LQuery<T1>> qs)
//    {
//        List<ClientQueryable<T1>> clientQueryable = new ArrayList<>();
//        for (LQuery<T1> q : qs)
//        {
//            clientQueryable.add(q.getClientQueryable());
//        }
//        return new LQuery<>(this.clientQueryable.union(clientQueryable), queryData.getDbType());
//    }
//
//    public LQuery<T1> unionAll(LQuery<T1> q1)
//    {
//        return new LQuery<>(clientQueryable.unionAll(q1.getClientQueryable()), queryData.getDbType());
//    }
//
//    public LQuery<T1> unionAll(LQuery<T1> q1, LQuery<T1> q2)
//    {
//        return new LQuery<>(clientQueryable.unionAll(q1.getClientQueryable(), q2.getClientQueryable()), queryData.getDbType());
//    }
//
//    public LQuery<T1> unionAll(LQuery<T1> q1, LQuery<T1> q2, LQuery<T1> q3)
//    {
//        return new LQuery<>(clientQueryable.unionAll(q1.getClientQueryable(), q2.getClientQueryable(), q3.getClientQueryable()), queryData.getDbType());
//    }
//
//    public LQuery<T1> unionAll(Collection<LQuery<T1>> qs)
//    {
//        List<ClientQueryable<T1>> clientQueryable = new ArrayList<>();
//        for (LQuery<T1> q : qs)
//        {
//            clientQueryable.add(q.getClientQueryable());
//        }
//        return new LQuery<>(this.clientQueryable.unionAll(clientQueryable), queryData.getDbType());
//    }
//
//    // endregion

    //region [OTHER]

    public LQuery4<T1, T2, T3, T4> distinct()
    {
        distinct0(true);
        return this;
    }

    public LQuery4<T1, T2, T3, T4> distinct(boolean condition)
    {
        distinct0(condition);
        return this;
    }
//
//    public void required()
//    {
//        clientQueryable.required();
//    }
//
//    public void required(String msg)
//    {
//        clientQueryable.required(msg);
//    }
//
//    public void required(String msg, String code)
//    {
//        clientQueryable.required(msg, code);
//    }
//
//    public void required(Supplier<RuntimeException> throwFunc)
//    {
//        clientQueryable.required(throwFunc);
//    }
//
//    public T1 firstOrNull()
//    {
//        return clientQueryable.firstOrNull();
//    }
//
//    public <R> R firstOrNull(Class<R> r)
//    {
//        return clientQueryable.firstOrNull(r);
//    }
//
//    public T1 firstNotNull()
//    {
//        return clientQueryable.firstNotNull();
//    }
//
//    public T1 firstNotNull(String msg)
//    {
//        return clientQueryable.firstNotNull(msg);
//    }
//
//    public T1 firstNotNull(String msg, String code)
//    {
//        return clientQueryable.firstNotNull(msg, code);
//    }
//
//    public T1 firstNotNull(Supplier<RuntimeException> throwFunc)
//    {
//        return clientQueryable.firstNotNull(throwFunc);
//    }
//
//    public <R> R firstNotNull(Class<R> r)
//    {
//        return clientQueryable.firstNotNull(r);
//    }
//
//    public <R> R firstNotNull(Class<R> r, String msg)
//    {
//        return clientQueryable.firstNotNull(r, msg);
//    }
//
//    public <R> R firstNotNull(Class<R> r, String msg, String code)
//    {
//        return clientQueryable.firstNotNull(r, msg, code);
//    }
    //endregion

//    // region [toAny]
//

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
//
////    public EasyPageResult<T1> toPageResult(long pageIndex, long pageSize)
////    {
////        return clientQueryable.toPageResult(pageIndex, pageSize);
////    }
////
////    public EasyPageResult<T1> toPageResult(long pageIndex, long pageSize, long pageTotal)
////    {
////        return clientQueryable.toPageResult(pageIndex, pageSize, pageTotal);
////    }
////
////    public <TPageResult> TPageResult toPageResult(Pager<T1,TPageResult> pager)
////    {
////        return clientQueryable.toPageResult(pager);
////    }
//
//    // endregion
}
