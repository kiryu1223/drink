/*
 * Copyright 2017-2024 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.kiryu1223.drink.core.api.crud.read.group;

import io.github.kiryu1223.drink.base.page.PagedResult;
import io.github.kiryu1223.drink.core.api.crud.read.EndQuery;
import io.github.kiryu1223.drink.core.api.crud.read.QueryBase;
import io.github.kiryu1223.drink.core.exception.NotCompiledException;
import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;
import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.delegate.Func5;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;
import io.github.kiryu1223.expressionTree.expressions.annos.Expr;

import java.math.BigDecimal;
import java.util.List;

/**
 * 分组查询过程对象
 *
 * @author kiryu1223
 * @since 3.0
 */
public class GroupedQuery5<Key, T1, T2, T3, T4, T5> extends QueryBase<GroupedQuery5<Key, T1, T2, T3, T4, T5>, Key> {
    public GroupedQuery5(QuerySqlBuilder sqlBuilder) {
        super(sqlBuilder);
    }

    // region [HAVING]

    /**
     * 设置having<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param func 返回bool的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @return this
     */
    public GroupedQuery5<Key, T1, T2, T3, T4, T5> having(@Expr(Expr.BodyType.Expr) Func1<Group5<Key, T1, T2, T3, T4, T5>, Boolean> func) {
        throw new NotCompiledException();
    }

    public GroupedQuery5<Key, T1, T2, T3, T4, T5> having(ExprTree<Func1<Group5<Key, T1, T2, T3, T4, T5>, Boolean>> expr) {
        having(expr.getTree());
        return this;
    }

    public GroupedQuery5<Key, T1, T2, T3, T4, T5> havingIf(boolean condition, @Expr(Expr.BodyType.Expr) Func1<Group5<Key, T1, T2, T3, T4, T5>, Boolean> func) {
        throw new NotCompiledException();
    }

    public GroupedQuery5<Key, T1, T2, T3, T4, T5> havingIf(boolean condition, ExprTree<Func1<Group5<Key, T1, T2, T3, T4, T5>, Boolean>> expr) {
        if (condition) having(expr.getTree());
        return this;
    }

    // endregion

    // region [ORDER BY]

    /**
     * 设置orderBy的字段以及升降序，多次调用可以指定多个orderBy字段<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param expr 返回需要的字段的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @param asc  是否为升序
     * @return this
     */
    public <R> GroupedQuery5<Key, T1, T2, T3, T4, T5> orderBy(@Expr(Expr.BodyType.Expr) Func1<Group5<Key, T1, T2, T3, T4, T5>, R> expr, boolean asc) {
        throw new NotCompiledException();
    }

    public <R> GroupedQuery5<Key, T1, T2, T3, T4, T5> orderBy(ExprTree<Func1<Group5<Key, T1, T2, T3, T4, T5>, R>> expr, boolean asc) {
        orderBy(expr.getTree(), asc);
        return this;
    }

    /**
     * 设置orderBy的字段并且为升序，多次调用可以指定多个orderBy字段<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param expr 返回需要的字段的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @return this
     */
    public <R> GroupedQuery5<Key, T1, T2, T3, T4, T5> orderBy(@Expr(Expr.BodyType.Expr) Func1<Group5<Key, T1, T2, T3, T4, T5>, R> expr) {
        throw new NotCompiledException();
    }

    public <R> GroupedQuery5<Key, T1, T2, T3, T4, T5> orderBy(ExprTree<Func1<Group5<Key, T1, T2, T3, T4, T5>, R>> expr) {
        orderBy(expr, true);
        return this;
    }

    public <R> GroupedQuery5<Key, T1, T2, T3, T4, T5> orderByDesc(@Expr(Expr.BodyType.Expr) Func1<Group5<Key, T1, T2, T3, T4, T5>, R> expr) {
        throw new NotCompiledException();
    }

    public <R> GroupedQuery5<Key, T1, T2, T3, T4, T5> orderByDesc(ExprTree<Func1<Group5<Key, T1, T2, T3, T4, T5>, R>> expr) {
        orderBy(expr, false);
        return this;
    }

    public <R> GroupedQuery5<Key, T1, T2, T3, T4, T5> orderByIf(boolean condition, @Expr(Expr.BodyType.Expr) Func1<Group5<Key, T1, T2, T3, T4, T5>, R> expr, boolean asc) {
        throw new NotCompiledException();
    }

    public <R> GroupedQuery5<Key, T1, T2, T3, T4, T5> orderByIf(boolean condition, ExprTree<Func1<Group5<Key, T1, T2, T3, T4, T5>, R>> expr, boolean asc) {
        if (condition) orderBy(expr.getTree(), asc);
        return this;
    }

    public <R> GroupedQuery5<Key, T1, T2, T3, T4, T5> orderByIf(boolean condition, @Expr(Expr.BodyType.Expr) Func1<Group5<Key, T1, T2, T3, T4, T5>, R> expr) {
        throw new NotCompiledException();
    }

    public <R> GroupedQuery5<Key, T1, T2, T3, T4, T5> orderByIf(boolean condition, ExprTree<Func1<Group5<Key, T1, T2, T3, T4, T5>, R>> expr) {
        if (condition) orderBy(expr, true);
        return this;
    }

    public <R> GroupedQuery5<Key, T1, T2, T3, T4, T5> orderByDescIf(boolean condition, @Expr(Expr.BodyType.Expr) Func1<Group5<Key, T1, T2, T3, T4, T5>, R> expr) {
        throw new NotCompiledException();
    }

    public <R> GroupedQuery5<Key, T1, T2, T3, T4, T5> orderByDescIf(boolean condition, ExprTree<Func1<Group5<Key, T1, T2, T3, T4, T5>, R>> expr) {
        if (condition) orderBy(expr, false);
        return this;
    }

    // endregion

    // region [SELECT]

    /**
     * 设置select<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param expr 返回一个继承于Result的匿名对象的lambda表达式((a) -> new Result(){...})，初始化段{...}内编写需要select的字段(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @param <R>  Result
     * @return 基于Result类型的新查询过程对象
     */
    public <R> EndQuery<R> select(@Expr(Expr.BodyType.Expr) Func1<Group5<Key, T1, T2, T3, T4, T5>, R> expr) {
        throw new NotCompiledException();
    }

    public <R> EndQuery<R> select(ExprTree<Func1<Group5<Key, T1, T2, T3, T4, T5>, R>> expr) {
        select(expr.getTree());
        return new EndQuery<>(getSqlBuilder());
    }

//    /**
//     * 此重载用于当想要返回某个字段的情况((r) -> r.getId),因为select泛型限制为必须是Result的子类<p>
//     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
//     *
//     * @param expr 返回一个值的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
//     * @return 终结查询过程
//     */
//    public <R> EndQuery<R> endSelect(@Expr(Expr.BodyType.Expr) Func1<Group5<Key, T1, T2, T3, T4, T5>, R> expr) {
//        throw new NotCompiledException();
//    }
//
//    public <R> EndQuery<R> endSelect(ExprTree<Func1<Group5<Key, T1, T2, T3, T4, T5>, R>> expr) {
//        select(expr.getTree());
//        return new EndQuery<>(boxedQuerySqlBuilder());
//    }
    // endregion

    // region [toAny]

    /**
     * list集合形式返回数据，无数据则返回空list
     *
     * @return List
     */
    @Override
    public List<? extends Key> toList() {
        return super.toList();
    }

    /**
     * 分页返回数据，无数据则返回空List
     *
     * @param pageIndex 页编号 默认1开始
     * @param pageSize  页长度 默认大于等于1
     * @return 分页数据
     */
    public PagedResult<? extends Key> toPagedResult(long pageIndex, long pageSize) {
        return toPagedResult0(pageIndex, pageSize);
    }

    /**
     * 分页返回数据，无数据则返回空List
     *
     * @param pageIndex 页编号 默认1开始
     * @param pageSize  页长度 默认大于等于1
     * @return 分页数据
     */
    public PagedResult<? extends Key> toPagedResult(int pageIndex, int pageSize) {
        return toPagedResult((long) pageIndex, (long) pageSize);
    }

    // endregion

    // region [AGGREGATE]

    /**
     * 检查表中是否存在至少一条数据<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     */
    public boolean any(@Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, Boolean> func) {
        throw new NotCompiledException();
    }

    public boolean any(ExprTree<Func5<T1, T2, T3, T4, T5, Boolean>> expr) {
        return any(expr.getTree());
    }

    /**
     * 聚合函数COUNT
     */
    public List<Long> count() {
        return groupByCount0(null);
    }

    /**
     * 聚合函数COUNT<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param func 返回需要统计的字段的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     */
    public <R> List<Long> count(@Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, R> func) {
        throw new NotCompiledException();
    }

    public <R> List<Long> count(ExprTree<Func5<T1, T2, T3, T4, T5, R>> expr) {
        return groupByCount0(expr.getTree());
    }


    /**
     * 聚合函数SUM
     * <p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param func 返回需要统计的字段的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red)
     */
    public <R extends Number> List<R> sum(@Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, R> func) {
        throw new NotCompiledException();
    }

    public <R extends Number> List<R> sum(ExprTree<Func5<T1, T2, T3, T4, T5, R>> expr) {
        return groupBySum0(expr.getTree());
    }


    /**
     * 聚合函数AVG
     * <p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param func 返回需要统计的字段的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red))
     */
    public <R extends Number> List<BigDecimal> avg(@Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, R> func) {
        throw new NotCompiledException();
    }

    public <R extends Number> List<BigDecimal> avg(ExprTree<Func5<T1, T2, T3, T4, T5, R>> expr) {
        return groupByAvg0(expr.getTree());
    }

    /**
     * 聚合函数MAX
     * <p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param func 返回需要统计的字段的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red)))
     */
    public <R extends Number> List<R> max(@Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, R> func) {
        throw new NotCompiledException();
    }

    public <R extends Number> List<R> max(ExprTree<Func5<T1, T2, T3, T4, T5, R>> expr) {
        return groupByMax0(expr.getTree());
    }

    /**
     * 聚合函数MIN
     * <p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param func 返回需要统计的字段的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red)))
     */
    public <R extends Number> List<R> min(@Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, R> func) {
        throw new NotCompiledException();
    }

    public <R extends Number> List<R> min(ExprTree<Func5<T1, T2, T3, T4, T5, R>> expr) {
        return groupByMin0(expr.getTree());
    }
    // endregion
}
