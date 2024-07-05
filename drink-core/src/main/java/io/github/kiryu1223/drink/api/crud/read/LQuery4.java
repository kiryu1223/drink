package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.api.crud.read.group.GroupedQuery4;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.JoinType;
import io.github.kiryu1223.expressionTree.delegate.Func3;
import io.github.kiryu1223.expressionTree.delegate.Func4;
import io.github.kiryu1223.expressionTree.delegate.Func5;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

public class LQuery4<T1, T2, T3, T4> extends QueryBase
{
    // region [INIT]

    public LQuery4(Config config)
    {
        super(config);
    }

    public LQuery4(Config config, Class<?> c1, Class<?> c2, Class<?> c3, Class<?> c4)
    {
        super(config);
        getSqlBuilder().addFrom(c1, c2, c3, c4);
    }

    public LQuery4(QueryBase q1, QueryBase q2, QueryBase q3, QueryBase q4)
    {
        super(q1.getConfig());
        getSqlBuilder().addFrom(q1.getSqlBuilder(), q2.getSqlBuilder(), q3.getSqlBuilder(), q4.getSqlBuilder());
    }

    // endregion

    //region [JOIN]

    @Override
    protected <Tn> LQuery5<T1, T2, T3, T4, Tn> joinNewQuery()
    {
        LQuery5<T1, T2, T3, T4, Tn> query = new LQuery5<>(getConfig());
        query.getSqlBuilder().joinBy(getSqlBuilder());
        return query;
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> innerJoin(Class<Tn> target, @Expr Func5<T1, T2, T3, T4, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> innerJoin(Class<Tn> target, ExprTree<Func5<T1, T2, T3, T4, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> innerJoin(LQuery<Tn> target, @Expr Func5<T1, T2, T3, T4, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> innerJoin(LQuery<Tn> target, ExprTree<Func5<T1, T2, T3, T4, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> leftJoin(Class<Tn> target, @Expr Func5<T1, T2, T3, T4, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> leftJoin(Class<Tn> target, ExprTree<Func5<T1, T2, T3, T4, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> leftJoin(LQuery<Tn> target, @Expr Func5<T1, T2, T3, T4, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> leftJoin(LQuery<Tn> target, ExprTree<Func5<T1, T2, T3, T4, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> rightJoin(Class<Tn> target, @Expr Func5<T1, T2, T3, T4, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> rightJoin(Class<Tn> target, ExprTree<Func5<T1, T2, T3, T4, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> rightJoin(LQuery<Tn> target, @Expr Func5<T1, T2, T3, T4, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery5<T1, T2, T3, T4, Tn> rightJoin(LQuery<Tn> target, ExprTree<Func5<T1, T2, T3, T4, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return joinNewQuery();
    }

    // endregion

    // region [WHERE]
    public LQuery4<T1, T2, T3, T4> where(@Expr Func4<T1, T2, T3, T4, Boolean> func)
    {
        throw new RuntimeException();
    }

    public LQuery4<T1, T2, T3, T4> where(ExprTree<Func4<T1, T2, T3, T4, Boolean>> expr)
    {
        where(expr.getTree());
        return this;
    }
    // endregion

    // region [ORDER BY]
    public <R> LQuery4<T1, T2, T3, T4> orderBy(@Expr Func4<T1, T2, T3, T4, R> expr, boolean asc)
    {
        throw new RuntimeException();
    }

    public <R> LQuery4<T1, T2, T3, T4> orderBy(ExprTree<Func4<T1, T2, T3, T4, R>> expr, boolean asc)
    {
        orderBy(expr.getTree(), asc);
        return this;
    }

    public <R> LQuery4<T1, T2, T3, T4> orderBy(@Expr Func4<T1, T2, T3, T4, R> expr)
    {
        throw new RuntimeException();
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
        throw new RuntimeException();
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
    public <R> LQuery<R> select(@Expr Func4<T1, T2, T3, T4, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> LQuery<R> select(ExprTree<Func4<T1, T2, T3, T4, R>> expr)
    {
        boolean single = select(expr.getTree());
        singleCheck(single);
        return new LQuery<>(this);
    }
    public <R> EndQuery<R> selectSingle(@Expr Func4<T1, T2, T3, T4, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> EndQuery<R> selectSingle(ExprTree<Func4<T1, T2, T3, T4, R>> expr)
    {
        select(expr.getTree());
        return new EndQuery<>(this);
    }
    // endregion

//    // region [INCLUDE]
//    public <R> LQuery4<T1, T2, T3, T4> include(@Expr Func4<T1, T2, T3, T4, R> expr, int groupSize)
//    {
//        throw new RuntimeException();
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
//        throw new RuntimeException();
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
        getSqlBuilder().setDistinct(true);
        return this;
    }

    public LQuery4<T1, T2, T3, T4> distinct(boolean condition)
    {
        getSqlBuilder().setDistinct(condition);
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
