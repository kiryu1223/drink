package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.api.crud.read.group.GroupedQuery3;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.JoinType;
import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.delegate.Func3;
import io.github.kiryu1223.expressionTree.delegate.Func4;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class LQuery3<T1, T2, T3> extends QueryBase
{
    // region [INIT]

    public LQuery3(Config config)
    {
        super(config);
    }

    public LQuery3(Config config, Class<?> c1, Class<?> c2, Class<?> c3)
    {
        super(config);
        getSqlBuilder().addFrom(c1, c2, c3);
    }

    public LQuery3(QueryBase q1, QueryBase q2, QueryBase q3)
    {
        super(q1.getConfig());
        getSqlBuilder().addFrom(q1.getSqlBuilder(), q2.getSqlBuilder(), q3.getSqlBuilder());
    }

    // endregion

    //region [JOIN]

    @Override
    protected <Tn> LQuery4<T1, T2,T3, Tn> joinNewQuery()
    {
        LQuery4<T1, T2,T3, Tn> query = new LQuery4<>(getConfig());
        query.getSqlBuilder().joinBy(getSqlBuilder());
        return query;
    }

    public <Tn> LQuery4<T1, T2, T3, Tn> innerJoin(Class<Tn> target, @Expr Func4<T1, T2, T3, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery4<T1, T2, T3, Tn> innerJoin(Class<Tn> target, ExprTree<Func4<T1, T2, T3, Tn, Boolean>> expr)
    {
        join(JoinType.INNER,target,expr);
        return joinNewQuery();
    }

    public <Tn> LQuery4<T1, T2, T3, Tn> innerJoin(LQuery<Tn> target, @Expr Func4<T1, T2, T3, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery4<T1, T2, T3, Tn> innerJoin(LQuery<Tn> target, ExprTree<Func4<T1, T2, T3, Tn, Boolean>> expr)
    {
        join(JoinType.INNER,target,expr);
        return joinNewQuery();
    }

    public <Tn> LQuery4<T1, T2, T3, Tn> leftJoin(Class<Tn> target, @Expr Func4<T1, T2, T3, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery4<T1, T2, T3, Tn> leftJoin(Class<Tn> target, ExprTree<Func4<T1, T2, T3, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT,target,expr);
        return joinNewQuery();
    }

    public <Tn> LQuery4<T1, T2, T3, Tn> leftJoin(LQuery<Tn> target, @Expr Func4<T1, T2, T3, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery4<T1, T2, T3, Tn> leftJoin(LQuery<Tn> target, ExprTree<Func4<T1, T2, T3, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT,target,expr);
        return joinNewQuery();
    }

    public <Tn> LQuery4<T1, T2, T3, Tn> rightJoin(Class<Tn> target, @Expr Func4<T1, T2, T3, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery4<T1, T2, T3, Tn> rightJoin(Class<Tn> target, ExprTree<Func4<T1, T2, T3, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT,target,expr);
        return joinNewQuery();
    }

    public <Tn> LQuery4<T1, T2, T3, Tn> rightJoin(LQuery<Tn> target, @Expr Func4<T1, T2, T3, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery4<T1, T2, T3, Tn> rightJoin(LQuery<Tn> target, ExprTree<Func4<T1, T2, T3, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT,target,expr);
        return joinNewQuery();
    }

    // endregion

    // region [WHERE]
    public LQuery3<T1, T2, T3> where(@Expr Func3<T1, T2, T3, Boolean> func)
    {
        throw new RuntimeException();
    }

    public LQuery3<T1, T2, T3> where(ExprTree<Func3<T1, T2, T3, Boolean>> expr)
    {
        where(expr.getTree());
        return this;
    }
    // endregion

    // region [ORDER BY]
    public <R> LQuery3<T1, T2, T3> orderBy(@Expr Func3<T1, T2, T3, R> expr, boolean asc)
    {
        throw new RuntimeException();
    }

    public <R> LQuery3<T1, T2, T3> orderBy(ExprTree<Func3<T1, T2, T3, R>> expr, boolean asc)
    {
        orderBy(expr.getTree(),asc);
        return this;
    }

    public <R> LQuery3<T1, T2, T3> orderBy(@Expr Func3<T1, T2, T3, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> LQuery3<T1, T2, T3> orderBy(ExprTree<Func3<T1, T2, T3, R>> expr)
    {
        orderBy(expr,true);
        return this;
    }
    // endregion

    // region [LIMIT]
    public LQuery3<T1, T2, T3> limit(long rows)
    {
        limit0(rows);
        return this;
    }

    public LQuery3<T1, T2, T3> limit(long offset, long rows)
    {
        limit0(offset,rows);
        return this;
    }
    // endregion

    // region [GROUP BY]
    public <Key> GroupedQuery3<Key, T1, T2, T3> groupBy(@Expr Func3<T1, T2, T3, Key> expr)
    {
        throw new RuntimeException();
    }

    public <Key> GroupedQuery3<Key, T1, T2, T3> groupBy(ExprTree<Func3<T1, T2, T3, Key>> expr)
    {
        groupBy(expr.getTree());
        return new GroupedQuery3<>(getSqlBuilder());
    }
    // endregion

    // region [SELECT]
    public LQuery<T1> select()
    {
        return new LQuery<>(this);
    }

    public <R> LQuery<R> select(Class<R> r)
    {
        getSqlBuilder().setTargetClass(r);
        return new LQuery<>(this);
    }

    public <R> LQuery<R> select(@Expr Func3<T1, T2, T3, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> LQuery<R> select(ExprTree<Func3<T1, T2, T3, R>> expr)
    {
        boolean single = select(expr.getTree());
        singleCheck(single);
        return new LQuery<>(this);
    }

    // endregion

//    // region [INCLUDE]
//    public <R> LQuery3<T1, T2, T3> include(@Expr Func3<T1, T2, T3, R> expr, int groupSize)
//    {
//        throw new RuntimeException();
//    }
//
//    public <R> LQuery3<T1, T2, T3> include(ExprTree<Func3<T1, T2, T3, R>> expr, int groupSize)
//    {
//        Include include = new Include(expr.getTree(), groupSize);
//        include.analysis(clientQueryable, queryData);
//        return this;
//    }
//
//    public <R> LQuery3<T1, T2, T3> include(@Expr Func3<T1, T2, T3, R> expr)
//    {
//        throw new RuntimeException();
//    }
//
//    public <R> LQuery3<T1, T2, T3> include(ExprTree<Func3<T1, T2, T3, R>> expr)
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

    public LQuery3<T1, T2, T3> distinct()
    {
        getSqlBuilder().setDistinct(true);
        return this;
    }

    public LQuery3<T1, T2, T3> distinct(boolean condition)
    {
        getSqlBuilder().setDistinct(condition);
        return this;
    }

//    public boolean any()
//    {
//        return clientQueryable.any();
//    }
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
