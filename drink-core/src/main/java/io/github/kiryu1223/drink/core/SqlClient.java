package io.github.kiryu1223.drink.core;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.annotation.Empty;
import io.github.kiryu1223.drink.base.annotation.Navigate;
import io.github.kiryu1223.drink.base.annotation.RelationType;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.transaction.Transaction;
import io.github.kiryu1223.drink.core.api.ITable;
import io.github.kiryu1223.drink.core.api.Result;
import io.github.kiryu1223.drink.core.api.crud.create.ObjectInsert;
import io.github.kiryu1223.drink.core.api.crud.delete.LDelete;
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
import io.github.kiryu1223.expressionTree.expressions.annos.Recode;

import java.math.BigDecimal;
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
        return new LQuery<>(new QuerySqlBuilder(config, config.getSqlExpressionFactory().queryable(c)));
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

//    {
//
//        class User implements ITable {
//            int id;
//            int age;
//            int areaId;
//            String name;
//            BigDecimal amount;
//        }
//
//        class Area implements ITable {
//            int id;
//            String name;
//            @Navigate(
//                    value = RelationType.OneToMany,
//                    self = "id",
//                    target = "areaId"
//            )
//            List<User> users;
//
//            public int getId() {
//                return id;
//            }
//
//            public void setId(int id) {
//                this.id = id;
//            }
//
//            public String getName() {
//                return name;
//            }
//
//            public void setName(String name) {
//                this.name = name;
//            }
//
//            public List<User> getUsers() {
//                return users;
//            }
//
//            public void setUsers(List<User> users) {
//                this.users = users;
//            }
//        }
//
//        BigDecimal sumMoney = query(Area.class)
//                .where(a -> a.name.contains("华东"))
//                .selectMany(a -> a.users)
//                .where(a -> a.age >= 20 && a.age <= 30)
//                .sum(u -> u.amount);
//
//        List<? extends Result> list = query(Area.class)
//                .groupBy(a->new Grouper()
//                {
//                    int id=a.getId();
//                    String name=a.getName();
//                })
//                .select(g -> new Result() {
//                    Group<? extends Grouper, Area> gg=g;
//                    String name = g.key.name;
//                    long count1 = g.count(g.value1.id);
//                    long count2 = g.count(gg -> gg.id);
//                    BigDecimal avg1 = g.avg(gg -> gg.id);
//                    BigDecimal avg2 = g.avg(g.key.id);
//                    int sum1 = g.sum(a -> a.id);
//                    int sum2 = g.sum(g.key.id);
//                    String max1 = g.max(g.key.name);
//                    String max2 = g.max(gg -> gg.name);
//                    String min1 = g.min(g.key.name);
//                    String min2 = g.min(gg -> gg.name);
//                }).toList();
//
//        List<Long> list1 = query(Area.class)
//                .selectAggregate(g -> g.count())
//                .toList();
//
//        query(Area.class)
//                .where(a -> a.query(a.users)
//                        .where(u -> u.age > 20)
//                        .groupBy(u -> new Grouper() {
//                            int id = u.id;
//                        })
//                        .select(g -> g.sum(g.value1.age))
//                        .toList()
//                        .contains(a.getId())
//                )
//                .toList();
//
//
//        Area first = query(Area.class)
//                .where(a -> a.query(a.users).where(u -> u.age > 20).any())
//                .first();
//    }
}
