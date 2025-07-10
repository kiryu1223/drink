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
package io.github.kiryu1223.drink.core.api.crud.read;

import io.github.kiryu1223.drink.base.expression.JoinType;
import io.github.kiryu1223.drink.core.api.Result;
import io.github.kiryu1223.drink.core.api.crud.read.group.GroupedQuery5;
import io.github.kiryu1223.drink.core.api.crud.read.group.Grouper;
import io.github.kiryu1223.drink.core.exception.NotCompiledException;
import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;
import io.github.kiryu1223.expressionTree.delegate.Func5;
import io.github.kiryu1223.expressionTree.delegate.Func6;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;
import io.github.kiryu1223.expressionTree.expressions.annos.Expr;

/**
 * 查询过程对象
 *
 * @author kiryu1223
 * @since 3.0
 */
public class LQuery5<T1, T2, T3, T4, T5> extends QueryBase<LQuery5<T1, T2, T3, T4, T5>, T1> {
    // region [INIT]

    public LQuery5(QuerySqlBuilder sqlBuilder) {
        super(sqlBuilder);
    }

    // endregion

    //region [JOIN]

    protected <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> joinNewQuery() {
        return new LQuery6<>(getSqlBuilder());
    }

    /**
     * join表操作<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param target 数据表类或查询过程
     * @param func   返回bool的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @param <Tn>   join过来的表的类型
     * @return 泛型数量+1的查询过程对象
     */
    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> innerJoin(Class<Tn> target, @Expr(Expr.BodyType.Expr) Func6<T1, T2, T3, T4, T5, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> innerJoin(Class<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr) {
        join(JoinType.INNER, target, expr.getTree());
        return joinNewQuery();
    }

    /**
     * join表操作<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param target 数据表类或查询过程
     * @param func   返回bool的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @param <Tn>   join过来的表的类型
     * @return 泛型数量+1的查询过程对象
     */
    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> innerJoin(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func6<T1, T2, T3, T4, T5, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> innerJoin(LQuery<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr) {
        join(JoinType.INNER, target, expr.getTree());
        return joinNewQuery();
    }

    /**
     * join表操作<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param target 数据表类或查询过程
     * @param func   返回bool的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @param <Tn>   join过来的表的类型
     * @return 泛型数量+1的查询过程对象
     */
    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> innerJoin(EndQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func6<T1, T2, T3, T4, T5, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> innerJoin(EndQuery<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr) {
        join(JoinType.INNER, target, expr.getTree());
        return joinNewQuery();
    }


    /**
     * join表操作<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param target 数据表类或查询过程
     * @param func   返回bool的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @param <Tn>   join过来的表的类型
     * @return 泛型数量+1的查询过程对象
     */
    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> leftJoin(Class<Tn> target, @Expr(Expr.BodyType.Expr) Func6<T1, T2, T3, T4, T5, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> leftJoin(Class<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr) {
        join(JoinType.LEFT, target, expr.getTree());
        return joinNewQuery();
    }

    /**
     * join表操作<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param target 数据表类或查询过程
     * @param func   返回bool的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @param <Tn>   join过来的表的类型
     * @return 泛型数量+1的查询过程对象
     */
    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> leftJoin(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func6<T1, T2, T3, T4, T5, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> leftJoin(LQuery<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr) {
        join(JoinType.LEFT, target, expr.getTree());
        return joinNewQuery();
    }

    /**
     * join表操作<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param target 数据表类或查询过程
     * @param func   返回bool的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @param <Tn>   join过来的表的类型
     * @return 泛型数量+1的查询过程对象
     */
    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> leftJoin(EndQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func6<T1, T2, T3, T4, T5, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> leftJoin(EndQuery<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr) {
        join(JoinType.LEFT, target, expr.getTree());
        return joinNewQuery();
    }


    /**
     * join表操作<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param target 数据表类或查询过程
     * @param func   返回bool的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @param <Tn>   join过来的表的类型
     * @return 泛型数量+1的查询过程对象
     */
    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> rightJoin(Class<Tn> target, @Expr(Expr.BodyType.Expr) Func6<T1, T2, T3, T4, T5, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> rightJoin(Class<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr) {
        join(JoinType.RIGHT, target, expr.getTree());
        return joinNewQuery();
    }

    /**
     * join表操作<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param target 数据表类或查询过程
     * @param func   返回bool的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @param <Tn>   join过来的表的类型
     * @return 泛型数量+1的查询过程对象
     */
    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> rightJoin(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func6<T1, T2, T3, T4, T5, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> rightJoin(LQuery<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr) {
        join(JoinType.RIGHT, target, expr.getTree());
        return joinNewQuery();
    }

    /**
     * join表操作<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param target 数据表类或查询过程
     * @param func   返回bool的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @param <Tn>   join过来的表的类型
     * @return 泛型数量+1的查询过程对象
     */
    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> rightJoin(EndQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func6<T1, T2, T3, T4, T5, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery6<T1, T2, T3, T4, T5, Tn> rightJoin(EndQuery<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr) {
        join(JoinType.RIGHT, target, expr.getTree());
        return joinNewQuery();
    }

    // endregion

    // region [WHERE]

    /**
     * 设置where条件<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param func 返回bool的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @return this
     */
    public LQuery5<T1, T2, T3, T4, T5> where(@Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, Boolean> func) {
        throw new NotCompiledException();
    }

    public LQuery5<T1, T2, T3, T4, T5> where(ExprTree<Func5<T1, T2, T3, T4, T5, Boolean>> expr) {
        where(expr.getTree());
        return this;
    }

    public LQuery5<T1, T2, T3, T4, T5> whereIf(boolean condition, @Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, Boolean> func) {
        throw new NotCompiledException();
    }

    public LQuery5<T1, T2, T3, T4, T5> whereIf(boolean condition, ExprTree<Func5<T1, T2, T3, T4, T5, Boolean>> expr) {
        if (condition) where(expr.getTree());
        return this;
    }

    /**
     * 设置where条件，并且以or将多个where连接<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param func 返回bool的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @return this
     */
    public LQuery5<T1, T2, T3, T4, T5> orWhere(@Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, Boolean> func) {
        throw new NotCompiledException();
    }

    public LQuery5<T1, T2, T3, T4, T5> orWhere(ExprTree<Func5<T1, T2, T3, T4, T5, Boolean>> expr) {
        orWhere(expr.getTree());
        return this;
    }

    public LQuery5<T1, T2, T3, T4, T5> orWhereIf(boolean condition, @Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, Boolean> func) {
        throw new NotCompiledException();
    }

    public LQuery5<T1, T2, T3, T4, T5> orWhereIf(boolean condition, ExprTree<Func5<T1, T2, T3, T4, T5, Boolean>> expr) {
        if (condition) orWhere(expr.getTree());
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
    public <R extends Comparable<R>> LQuery5<T1, T2, T3, T4, T5> orderBy(@Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, R> expr, boolean asc) {
        throw new NotCompiledException();
    }

    public <R extends Comparable<R>> LQuery5<T1, T2, T3, T4, T5> orderBy(ExprTree<Func5<T1, T2, T3, T4, T5, R>> expr, boolean asc) {
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
    public <R extends Comparable<R>> LQuery5<T1, T2, T3, T4, T5> orderBy(@Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, R> expr) {
        throw new NotCompiledException();
    }

    public <R extends Comparable<R>> LQuery5<T1, T2, T3, T4, T5> orderBy(ExprTree<Func5<T1, T2, T3, T4, T5, R>> expr) {
        orderBy(expr, true);
        return this;
    }

    public <R extends Comparable<R>> LQuery5<T1, T2, T3, T4, T5> orderByDesc(@Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, R> expr) {
        throw new NotCompiledException();
    }

    public <R extends Comparable<R>> LQuery5<T1, T2, T3, T4, T5> orderByDesc(ExprTree<Func5<T1, T2, T3, T4, T5, R>> expr) {
        orderBy(expr, false);
        return this;
    }

    public <R extends Comparable<R>> LQuery5<T1, T2, T3, T4, T5> orderByIf(boolean condition, @Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, R> expr, boolean asc) {
        throw new NotCompiledException();
    }

    public <R extends Comparable<R>> LQuery5<T1, T2, T3, T4, T5> orderByIf(boolean condition, ExprTree<Func5<T1, T2, T3, T4, T5, R>> expr, boolean asc) {
        if (condition) orderBy(expr.getTree(), asc);
        return this;
    }

    public <R extends Comparable<R>> LQuery5<T1, T2, T3, T4, T5> orderByIf(boolean condition, @Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, R> expr) {
        throw new NotCompiledException();
    }

    public <R extends Comparable<R>> LQuery5<T1, T2, T3, T4, T5> orderByIf(boolean condition, ExprTree<Func5<T1, T2, T3, T4, T5, R>> expr) {
        if (condition) orderBy(expr, true);
        return this;
    }

    public <R extends Comparable<R>> LQuery5<T1, T2, T3, T4, T5> orderByDescIf(boolean condition, @Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, R> expr) {
        throw new NotCompiledException();
    }

    public <R extends Comparable<R>> LQuery5<T1, T2, T3, T4, T5> orderByDescIf(boolean condition, ExprTree<Func5<T1, T2, T3, T4, T5, R>> expr) {
        if (condition) orderBy(expr, false);
        return this;
    }

    // endregion

    // region [GROUP BY]

    /**
     * 设置groupBy<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param expr 返回一个继承于Grouper的匿名对象的lambda表达式((a) -> new Grouper(){...})，初始化段{...}内编写需要加入到Group的字段(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @return 分组查询过程对象
     */
    public <Key extends Grouper> GroupedQuery5<? extends Key, T1, T2, T3, T4, T5> groupBy(@Expr Func5<T1, T2, T3, T4, T5, Key> expr) {
        throw new NotCompiledException();
    }

    public <Key extends Grouper> GroupedQuery5<? extends Key, T1, T2, T3, T4, T5> groupBy(ExprTree<Func5<T1, T2, T3, T4, T5, Key>> expr) {
        groupBy(expr.getTree());
        return new GroupedQuery5<>(getSqlBuilder());
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
    public <R> EndQuery<R> select(@Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, R> expr) {
        throw new NotCompiledException();
    }

    public <R> EndQuery<R> select(ExprTree<Func5<T1, T2, T3, T4, T5, R>> expr) {
        select(expr.getTree());
        return new EndQuery<>(getSqlBuilder());
    }

    // endregion

    // region [WITH]

    public <R extends Result> LQuery<R> withTemp(@Expr(Expr.BodyType.Expr) Func5<T1, T2, T3, T4, T5, R> expr) {
        throw new NotCompiledException();
    }

    public <R extends Result> LQuery<R> withTemp(ExprTree<Func5<T1, T2, T3, T4, T5, R>> expr) {
        select(expr.getTree());
        withTempQuery();
        return new LQuery<>(getSqlBuilder());
    }

    // endregion
}
