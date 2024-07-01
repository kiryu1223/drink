package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.api.crud.read.group.GroupedQuery;
import io.github.kiryu1223.drink.core.context.SqlContext;
import io.github.kiryu1223.drink.core.context.SqlOrderContext;
import io.github.kiryu1223.drink.core.visitor.GroupByVisitor;
import io.github.kiryu1223.drink.core.visitor.HavingVisitor;
import io.github.kiryu1223.drink.core.visitor.SelectVisitor;
import io.github.kiryu1223.drink.core.visitor.WhereVisitor;
import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.delegate.Func2;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;


public class LQuery<T> extends QueryBase
{
    protected LQuery(Class<?> c)
    {
        super(c);
    }

    //region [JOIN]

    public <Tn> LQuery2<T, Tn> innerJoin(Class<Tn> target, @Expr Func2<T, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery2<T, Tn> innerJoin(Class<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr)
    {
        Join join = new Join(expr.getTree());
        ClientQueryable2<T, Tn> joinQuery = join.innerJoin(target, clientQueryable, queryData);
        return new LQuery2<>(joinQuery, queryData.getDbType());
    }

    public <Tn> LQuery2<T, Tn> innerJoin(LQuery<Tn> target, @Expr Func2<T, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery2<T, Tn> innerJoin(LQuery<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr)
    {
        Join join = new Join(expr.getTree());
        ClientQueryable2<T, Tn> joinQuery = join.innerJoin(target.getClientQueryable(), clientQueryable, queryData);
        return new LQuery2<>(joinQuery, queryData.getDbType());
    }

    public <Tn> LQuery2<T, Tn> leftJoin(Class<Tn> target, @Expr Func2<T, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery2<T, Tn> leftJoin(Class<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr)
    {
        Join join = new Join(expr.getTree());
        ClientQueryable2<T, Tn> joinQuery = join.leftJoin(target, clientQueryable, queryData);
        return new LQuery2<>(joinQuery, queryData.getDbType());
    }

    public <Tn> LQuery2<T, Tn> leftJoin(LQuery<Tn> target, @Expr Func2<T, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery2<T, Tn> leftJoin(LQuery<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr)
    {
        Join join = new Join(expr.getTree());
        ClientQueryable2<T, Tn> joinQuery = join.leftJoin(target.getClientQueryable(), clientQueryable, queryData);
        return new LQuery2<>(joinQuery, queryData.getDbType());
    }

    public <Tn> LQuery2<T, Tn> rightJoin(Class<Tn> target, @Expr Func2<T, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery2<T, Tn> rightJoin(Class<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr)
    {
        Join join = new Join(expr.getTree());
        ClientQueryable2<T, Tn> joinQuery = join.rightJoin(target, clientQueryable, queryData);
        return new LQuery2<>(joinQuery, queryData.getDbType());
    }

    public <Tn> LQuery2<T, Tn> rightJoin(LQuery<Tn> target, @Expr Func2<T, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LQuery2<T, Tn> rightJoin(LQuery<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr)
    {
        Join join = new Join(expr.getTree());
        ClientQueryable2<T, Tn> joinQuery = join.rightJoin(target.getClientQueryable(), clientQueryable, queryData);
        return new LQuery2<>(joinQuery, queryData.getDbType());
    }

    //endregion

    // region [WHERE]

    public LQuery<T> where(@Expr Func1<T, Boolean> func)
    {
        throw new RuntimeException();
    }

    public LQuery<T> where(ExprTree<Func1<T, Boolean>> expr)
    {
        WhereVisitor whereVisitor = new WhereVisitor();
        SqlContext where = whereVisitor.visit(expr.getTree());
        getSqlBuilder().getWhere().add(where);
        return this;
    }

    // endregion

    // region [ORDER BY]

    public <R> LQuery<T> orderBy(@Expr Func1<T, R> expr, boolean asc)
    {
        throw new RuntimeException();
    }

    public <R> LQuery<T> orderBy(ExprTree<Func1<T, R>> expr, boolean asc)
    {
        HavingVisitor havingVisitor = new HavingVisitor(getSqlBuilder().getGroupBy());
        SqlContext context = havingVisitor.visit(expr.getTree());
        getSqlBuilder().getOrderBys().add(new SqlOrderContext(asc, context));
        return this;
    }

    public <R> LQuery<T> orderBy(@Expr Func1<T, R> expr)
    {
        throw new RuntimeException();
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
        getSqlBuilder().setRows(rows);
        return this;
    }

    public LQuery<T> limit(long offset, long rows)
    {
        getSqlBuilder().setOffset(offset);
        getSqlBuilder().setRows(rows);
        return this;
    }

    // endregion

    // region [GROUP BY]

    public <Key> GroupedQuery<Key, T> groupBy(@Expr Func1<T, Key> expr)
    {
        throw new RuntimeException();
    }

    public <Key> GroupedQuery<Key, T> groupBy(ExprTree<Func1<T, Key>> expr)
    {
        GroupByVisitor groupByVisitor = new GroupByVisitor();
        SqlContext context = groupByVisitor.visit(expr.getTree());
        return new GroupedQuery<>();
    }

    // endregion

    // region [SELECT]
    public LQuery<T> select()
    {
        return new LQuery<>();
    }

    public <R> LQuery<R> select(Class<R> r)
    {
        return new LQuery<>();
    }

    public <R> LQuery<R> select(@Expr Func1<T, R> expr)
    {
        throw new RuntimeException();
    }

    public <R> LQuery<R> select(ExprTree<Func1<T, R>> expr)
    {
        SelectVisitor selectVisitor=new SelectVisitor(getSqlBuilder().getGroupBy());
        SqlContext context = selectVisitor.visit(expr.getTree());
        getSqlBuilder().setSelects(context);
        return new LQuery<>();
    }

    // endregion

//    // region [INCLUDE]
//    public <R> LQuery<T> include(@Expr Func1<T, R> expr, int groupSize)
//    {
//        throw new RuntimeException();
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
//        throw new RuntimeException();
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
        clientQueryable.distinct();
        return this;
    }

    public LQuery<T> distinct(boolean condition)
    {
        clientQueryable.distinct(condition);
        return this;
    }

    public boolean any()
    {
        return clientQueryable.any();
    }

    public T firstOrNull()
    {
        return clientQueryable.firstOrNull();
    }

    //endregion

    // region [toAny]

    public ToSQLResult toSQLResult()
    {
        return clientQueryable.toSQLResult();
    }

    public List<T> toList()
    {
        return clientQueryable.toList();
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

    public Map<String, Object> toMap()
    {
        return clientQueryable.toMap();
    }

    public List<Map<String, Object>> toMaps()
    {
        return clientQueryable.toMaps();
    }

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
