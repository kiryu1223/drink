package io.github.kiryu1223.drink.core;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.AsName;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.transaction.Transaction;
import io.github.kiryu1223.drink.core.api.crud.create.ObjectInsert;
import io.github.kiryu1223.drink.core.api.crud.delete.LDelete;
import io.github.kiryu1223.drink.base.annotation.Empty;
import io.github.kiryu1223.drink.core.api.crud.read.EmptyQuery;
import io.github.kiryu1223.drink.core.api.crud.read.EndQuery;
import io.github.kiryu1223.drink.core.api.crud.read.LQuery;
import io.github.kiryu1223.drink.core.api.crud.read.UnionQuery;
import io.github.kiryu1223.drink.core.api.crud.read.group.Grouper;
import io.github.kiryu1223.drink.core.api.crud.update.LUpdate;
import io.github.kiryu1223.drink.core.exception.SqLinkException;
import io.github.kiryu1223.drink.core.sqlBuilder.DeleteSqlBuilder;
import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;
import io.github.kiryu1223.drink.core.sqlBuilder.UpdateSqlBuilder;
import io.github.kiryu1223.drink.core.visitor.ExpressionUtil;
import io.github.kiryu1223.expressionTree.expressions.annos.Recode;

import java.util.Collection;
import java.util.List;

public class SqlClient {
    private final IConfig config;

    SqlClient(IConfig config) {
        this.config = config;
    }

    /**
     * 手动开始事务
     *
     * @param isolationLevel 事务级别
     * @return 事务对象
     */
    public Transaction beginTransaction(Integer isolationLevel) {
        return config.getTransactionManager().get(isolationLevel);
    }

    /**
     * 手动开始事务
     *
     * @return 事务对象
     */
    public Transaction beginTransaction() {
        return beginTransaction(null);
    }

    /**
     * 查询
     *
     * @param c   数据类类对象
     * @param <T> 数据类类型
     * @return 查询过程对象
     */
    public <T> LQuery<T> query(@Recode Class<T> c) {
        String first = ExpressionUtil.getFirst(c);
        return new LQuery<>(new QuerySqlBuilder(config, config.getSqlExpressionFactory().queryable(c, new AsName(first))));
    }

    public <T> UnionQuery<T> union(LQuery<T> q1, LQuery<T> q2) {
        return new UnionQuery<>(config, q1, q2, false);
    }

    public <T> UnionQuery<T> union(EndQuery<T> q1, EndQuery<T> q2) {
        return new UnionQuery<>(config, q1, q2, false);
    }

    public <T> UnionQuery<T> unionAll(LQuery<T> q1, LQuery<T> q2) {
        return new UnionQuery<>(config, q1, q2, true);
    }

    public <T> UnionQuery<T> unionAll(EndQuery<T> q1, EndQuery<T> q2) {
        return new UnionQuery<>(config, q1, q2, false);
    }

    /**
     * 进行不包含表的查询
     *
     * @return 查询过程对象
     */
    public EmptyQuery queryEmptyTable() {
        return new EmptyQuery(new QuerySqlBuilder(config, config.getSqlExpressionFactory().queryable(Empty.class, new AsName())));
    }

    /**
     * 新增
     *
     * @param t   数据类对象
     * @param <T> 数据类类型
     * @return 新增过程对象
     */
    public <T> ObjectInsert<T> insert(@Recode T t) {
        ObjectInsert<T> objectInsert = new ObjectInsert<>(config, (Class<T>) t.getClass());
        return objectInsert.insert(t);
    }

    /**
     * 集合新增
     *
     * @param ts  数据类对象集合
     * @param <T> 数据类类型
     * @return 新增过程对象
     */
    public <T> ObjectInsert<T> insert(@Recode Collection<T> ts) {
        ObjectInsert<T> objectInsert = new ObjectInsert<>(config, getType(ts));
        return objectInsert.insert(ts);
    }

    /**
     * 更新
     *
     * @param c   数据类类对象
     * @param <T> 数据类类型
     * @return 更新过程对象
     */
    public <T> LUpdate<T> update(@Recode Class<T> c) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        String first = ExpressionUtil.getFirst(c);
        return new LUpdate<>(new UpdateSqlBuilder(config, factory.update(c, new AsName(first))));
    }

    /**
     * 删除
     *
     * @param c   数据类类对象
     * @param <T> 数据类类型
     * @return 删除过程对象
     */
    public <T> LDelete<T> delete(@Recode Class<T> c) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        String first = ExpressionUtil.getFirst(c);
        return new LDelete<>(new DeleteSqlBuilder(config, factory.delete(c, new AsName(first))));
    }

    public IConfig getConfig() {
        return config;
    }

    private <T> Class<T> getType(Collection<T> ts) {
        for (T t : ts) {
            return (Class<T>) t.getClass();
        }
        throw new SqLinkException("insert内容为空");
    }

    {
//        List<Empty> list = query(Empty.class)
//                .DisableFilterAll()
//                .leftJoin(Empty.class, (e1, e2) -> true)
//                .limit(5)
//                .select(Empty.class)
//                .toList();
    }
}
