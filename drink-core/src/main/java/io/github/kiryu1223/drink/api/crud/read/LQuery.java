package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.api.crud.read.group.GroupedQuery;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.JoinType;
import io.github.kiryu1223.drink.core.context.SqlContext;
import io.github.kiryu1223.drink.core.context.SqlPropertyContext;
import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;
import io.github.kiryu1223.drink.core.visitor.NormalVisitor;
import io.github.kiryu1223.drink.exception.NotCompiledException;
import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.delegate.Func2;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LQuery<T> extends QueryBase
{
    // region [INIT]

    public LQuery(Config config, Class<T> c)
    {
        super(config, c);
    }

    public LQuery(QuerySqlBuilder sqlBuilder)
    {
        super(sqlBuilder);
    }

    // endregion

    //region [JOIN]

    protected <Tn> LQuery2<T, Tn> joinNewQuery()
    {
        return new LQuery2<>(getSqlBuilder());
    }

    public <Tn> LQuery2<T, Tn> innerJoin(Class<Tn> target, @Expr(Expr.BodyType.Expr) Func2<T, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery2<T, Tn> innerJoin(Class<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery2<T, Tn> innerJoin(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func2<T, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery2<T, Tn> innerJoin(LQuery<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery2<T, Tn> leftJoin(Class<Tn> target, @Expr(Expr.BodyType.Expr) Func2<T, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery2<T, Tn> leftJoin(Class<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery2<T, Tn> leftJoin(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func2<T, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery2<T, Tn> leftJoin(LQuery<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery2<T, Tn> leftJoin(EndQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func2<T, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery2<T, Tn> leftJoin(EndQuery<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery2<T, Tn> rightJoin(Class<Tn> target, @Expr(Expr.BodyType.Expr) Func2<T, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery2<T, Tn> rightJoin(Class<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return joinNewQuery();
    }

    public <Tn> LQuery2<T, Tn> rightJoin(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func2<T, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LQuery2<T, Tn> rightJoin(LQuery<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return joinNewQuery();
    }

    //endregion

    // region [WHERE]

    public LQuery<T> where(@Expr(Expr.BodyType.Expr) Func1<T, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public LQuery<T> where(ExprTree<Func1<T, Boolean>> expr)
    {
        where(expr.getTree());
        return this;
    }

    public LQuery<T> orWhere(@Expr(Expr.BodyType.Expr) Func1<T, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public LQuery<T> orWhere(ExprTree<Func1<T, Boolean>> expr)
    {
        orWhere(expr.getTree());
        return this;
    }

    public <E> LQuery<T> exists(Class<E> table, @Expr(Expr.BodyType.Expr) Func2<T, E, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <E> LQuery<T> exists(Class<E> table, ExprTree<Func2<T, E, Boolean>> expr)
    {
        exists(table, expr.getTree(), false);
        return this;
    }

    public <E> LQuery<T> exists(LQuery<E> query, @Expr(Expr.BodyType.Expr) Func2<T, E, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <E> LQuery<T> exists(LQuery<E> query, ExprTree<Func2<T, E, Boolean>> expr)
    {
        exists(query, expr.getTree(), false);
        return this;
    }

    public <E> LQuery<T> notExists(Class<E> table, @Expr(Expr.BodyType.Expr) Func2<T, E, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <E> LQuery<T> notExists(Class<E> table, ExprTree<Func2<T, E, Boolean>> expr)
    {
        exists(table, expr.getTree(), true);
        return this;
    }

    public <E> LQuery<T> notExists(LQuery<E> query, @Expr(Expr.BodyType.Expr) Func2<T, E, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <E> LQuery<T> notExists(LQuery<E> query, ExprTree<Func2<T, E, Boolean>> expr)
    {
        exists(query, expr.getTree(), true);
        return this;
    }

    // endregion

    // region [ORDER BY]

    public <R> LQuery<T> orderBy(@Expr(Expr.BodyType.Expr) Func1<T, R> expr, boolean asc)
    {
        throw new NotCompiledException();
    }

    public <R> LQuery<T> orderBy(ExprTree<Func1<T, R>> expr, boolean asc)
    {
        orderBy(expr.getTree(), asc);
        return this;
    }

    public <R> LQuery<T> orderBy(@Expr(Expr.BodyType.Expr) Func1<T, R> expr)
    {
        throw new NotCompiledException();
    }

    public <R> LQuery<T> orderBy(ExprTree<Func1<T, R>> expr)
    {
        orderBy(expr, true);
        return this;
    }

    // endregion

    // region [LIMIT]

    public LQuery<T> limit(long rows)
    {
        limit0(rows);
        return this;
    }

    public LQuery<T> limit(long offset, long rows)
    {
        limit0(offset, rows);
        return this;
    }

    // endregion

    // region [GROUP BY]

    public <Key> GroupedQuery<Key, T> groupBy(@Expr Func1<T, Key> expr)
    {
        throw new NotCompiledException();
    }

    public <Key> GroupedQuery<Key, T> groupBy(ExprTree<Func1<T, Key>> expr)
    {
        groupBy(expr.getTree());
        return new GroupedQuery<>(getSqlBuilder());
    }

    // endregion

    // region [SELECT]

    public <R> EndQuery<R> select(Class<R> r)
    {
        return super.select(r);
    }

    public <R> LQuery<? extends R> select(@Expr Func1<T, ? extends R> expr)
    {
        throw new NotCompiledException();
    }

    public LQuery<T> select()
    {
        return new LQuery<>(boxedQuerySqlBuilder());
    }

    public <R> LQuery<? extends R> select(ExprTree<Func1<T, ? extends R>> expr)
    {
        boolean single = select(expr.getTree());
        singleCheck(single);
        return new LQuery<>(boxedQuerySqlBuilder());
    }

    public <R> EndQuery<R> endSelect(@Expr(Expr.BodyType.Expr) Func1<T, R> expr)
    {
        throw new NotCompiledException();
    }

    public <R> EndQuery<R> endSelect(ExprTree<Func1<T, R>> expr)
    {
        select(expr.getTree());
        return new EndQuery<>(getSqlBuilder());
    }

//    public <R> LQuery<R> selectMany(@Expr Func1<T, Collection<R>> expr)
//    {
//        throw new NotCompiledException();
//    }
//
//    public <R> LQuery<R> selectMany(ExprTree<Func1<T, Collection<R>>> expr)
//    {
//        return new LQuery<>(getSqlBuilder());
//    }


    // endregion

    // region [INCLUDE]

    public <R> LQuery<T> include(@Expr(Expr.BodyType.Expr) Func1<T, R> expr)
    {
        throw new NotCompiledException();
    }

    public <R> LQuery<T> include(ExprTree<Func1<T, R>> expr)
    {
        include(expr.getTree());
        return this;
    }

    // endregion

//    // region [INCLUDE]
//    public <R> LQuery<T> include(@Expr Func1<T, R> expr, int groupSize)
//    {
//        throw new NotCompiledException();
//    }
//
//    public <R> LQuery<T> include(ExprTree<Func1<T, R>> expr, int groupSize)
//    {
//        Include include = new Include(expr.getTree(), groupSize);
//        include.analysis(clientQueryable, queryData);
//        return this;
//    }
//
//    public <R> LQuery<T> include(@Expr Func1<T, R> expr)
//    {
//        throw new NotCompiledException();
//    }
//
//    public <R> LQuery<T> include(ExprTree<Func1<T, R>> expr)
//    {
//        Include include = new Include(expr.getTree());
//        include.analysis(clientQueryable, queryData);
//        return this;
//    }
//    // endregion
//
//    // region [UNION]
//
//    public LQuery<T> union(LQuery<T> q1)
//    {
//        clientQueryable.union(q1.getClientQueryable());
//        return this;
//    }
//
//    public LQuery<T> union(LQuery<T> q1, LQuery<T> q2)
//    {
//        clientQueryable.union(q1.getClientQueryable(), q2.getClientQueryable());
//        return this;
//    }
//
//    public LQuery<T> union(LQuery<T> q1, LQuery<T> q2, LQuery<T> q3)
//    {
//        clientQueryable.union(q1.getClientQueryable(), q2.getClientQueryable(), q3.getClientQueryable());
//        return this;
//    }
//
//    public LQuery<T> union(Collection<LQuery<T>> qs)
//    {
//        List<ClientQueryable<T>> clientQueryable = new ArrayList<>();
//        for (LQuery<T> q : qs)
//        {
//            clientQueryable.add(q.getClientQueryable());
//        }
//        this.clientQueryable.union(clientQueryable);
//        return this;
//    }
//
//    public LQuery<T> unionAll(LQuery<T> q1)
//    {
//        clientQueryable.unionAll(q1.getClientQueryable());
//        return this;
//    }
//
//    public LQuery<T> unionAll(LQuery<T> q1, LQuery<T> q2)
//    {
//        clientQueryable.unionAll(q1.getClientQueryable(), q2.getClientQueryable());
//        return this;
//    }
//
//    public LQuery<T> unionAll(LQuery<T> q1, LQuery<T> q2, LQuery<T> q3)
//    {
//        clientQueryable.unionAll(q1.getClientQueryable(), q2.getClientQueryable(), q3.getClientQueryable());
//        return this;
//    }
//
//    public LQuery<T> unionAll(Collection<LQuery<T>> qs)
//    {
//        List<ClientQueryable<T>> clientQueryable = new ArrayList<>();
//        for (LQuery<T> q : qs)
//        {
//            clientQueryable.add(q.getClientQueryable());
//        }
//        this.clientQueryable.unionAll(clientQueryable);
//        return this;
//    }
//
//    // endregion

    //region [OTHER]

    public LQuery<T> distinct()
    {
        distinct0(true);
        return this;
    }

    public LQuery<T> distinct(boolean condition)
    {
        distinct0(condition);
        return this;
    }

//    public T firstOrNull()
//    {
//        return clientQueryable.firstOrNull();
//    }
//
//    //endregion
//
    // region [toAny]

    @Override
    public boolean any()
    {
        return super.any();
    }

    public List<T> toList()
    {
        return super.toList();
    }

    public <R> List<R> toList(Func1<T, R> func)
    {
        List<R> rList = new ArrayList<>();
        for (T t : toList())
        {
            rList.add(func.invoke(t));
        }
        return rList;
    }

    public <K> Map<K, T> toMap(Func1<T, K> func)
    {
        return toMap(func, new HashMap<>());
    }

    public <K> Map<K, T> toMap(Func1<T, K> func, Map<K, T> map)
    {
        for (T t : toList())
        {
            map.put(func.invoke(t), t);
        }
        return map;
    }

//    public <K> Map<K, T> toMap(@Expr(Expr.BodyType.Expr) Func1<T, K> func)
//    {
//        throw new NotCompiledException();
//    }
//
//    public <K> Map<K, T> toMap(ExprTree<Func1<T, K>> expr)
//    {
//        return super.toMap(expr.getTree());
//    }

//    public <R> List<R> toList(Func1<T, R> func)
//    {
//        List<R> rList = new ArrayList<>();
//        for (T t : toList())
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

//    public EasyPageResult<T> toPageResult(long pageIndex, long pageSize)
//    {
//        return clientQueryable.toPageResult(pageIndex, pageSize);
//    }
//
//    public EasyPageResult<T> toPageResult(long pageIndex, long pageSize, long pageTotal)
//    {
//        return clientQueryable.toPageResult(pageIndex, pageSize, pageTotal);
//    }
//
//    public <TPageResult> TPageResult toPageResult(Pager<T,TPageResult> pager)
//    {
//        return clientQueryable.toPageResult(pager);
//    }

    // endregion
}
