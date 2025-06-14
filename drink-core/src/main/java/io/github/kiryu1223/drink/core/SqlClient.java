package io.github.kiryu1223.drink.core;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.annotation.Empty;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.expression.ISqlQueryableExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.toBean.beancreator.AbsBeanCreator;
import io.github.kiryu1223.drink.base.transaction.Transaction;
import io.github.kiryu1223.drink.core.api.IView;
import io.github.kiryu1223.drink.core.api.crud.create.ObjectInsert;
import io.github.kiryu1223.drink.core.api.crud.create.ObjectInsertOrUpdate;
import io.github.kiryu1223.drink.core.api.crud.delete.LDelete;
import io.github.kiryu1223.drink.core.api.crud.read.*;
import io.github.kiryu1223.drink.core.api.crud.update.LUpdate;
import io.github.kiryu1223.drink.core.exception.SqLinkException;
import io.github.kiryu1223.drink.core.sqlBuilder.DeleteSqlBuilder;
import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;
import io.github.kiryu1223.drink.core.sqlBuilder.UpdateSqlBuilder;
import io.github.kiryu1223.expressionTree.expressions.annos.Recode;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class SqlClient {
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
        return new LQuery<>(new QuerySqlBuilder(config, tryView(c)));
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
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return new EmptyQuery(new QuerySqlBuilder(config, factory.queryable(Empty.class)));
    }

    private void viewCheck(Class<?> c) {
        if (IView.class.isAssignableFrom(c)) {
            throw new DrinkException("视图对象无法进行修改");
        }
    }

    /**
     * 新增
     *
     * @param t   数据类对象
     * @param <T> 数据类类型
     * @return 新增过程对象
     */
    public <T> ObjectInsert<T> insert(@Recode T t) {
        Class<T> aClass = (Class<T>) t.getClass();
        viewCheck(aClass);
        ObjectInsert<T> objectInsert = new ObjectInsert<>(config,aClass);
        return objectInsert.insert(t);
    }

    /**
     * 新增或插入
     */
    public <T> ObjectInsertOrUpdate<T> insertOrUpdate(@Recode T t) {
        viewCheck(t.getClass());
        return new ObjectInsertOrUpdate<>(config, Collections.singletonList(t));
    }

    /**
     * 批量新增或插入
     */
    public <T> ObjectInsertOrUpdate<T> insertOrUpdate(List<T> ts) {
        viewCheck(getType(ts));
        return new ObjectInsertOrUpdate<>(config, ts);
    }

    /**
     * 集合新增
     *
     * @param ts  数据类对象集合
     * @param <T> 数据类类型
     * @return 新增过程对象
     */
    public <T> ObjectInsert<T> insert(@Recode Collection<T> ts) {
        Class<T> type = getType(ts);
        viewCheck(type);
        ObjectInsert<T> objectInsert = new ObjectInsert<>(config,type);
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
        viewCheck(c);
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return new LUpdate<>(new UpdateSqlBuilder(config, factory.update(c)));
    }

    /**
     * 删除
     *
     * @param c   数据类类对象
     * @param <T> 数据类类型
     * @return 删除过程对象
     */
    public <T> LDelete<T> delete(@Recode Class<T> c) {
        viewCheck(c);
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return new LDelete<>(new DeleteSqlBuilder(config, factory.delete(c)));
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

    private <T> ISqlQueryableExpression tryView(Class<T> c) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        if (IView.class.isAssignableFrom(c)) {
            AbsBeanCreator<IView<T>> creator = config.getBeanCreatorFactory().get((Class<IView<T>>)c);
            IView<T> view = creator.getBeanCreator().get();
            QueryBase<?,T> with = view.createView(this);
            ISqlQueryableExpression queryable = with.getSqlBuilder().getQueryable();
            MetaData metaData = config.getMetaData(c);
            return factory.queryable(factory.with(queryable, metaData.getTableName()));
        }
        else {
            return factory.queryable(c);
        }
    }
}
