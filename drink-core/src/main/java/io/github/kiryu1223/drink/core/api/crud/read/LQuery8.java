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


import io.github.kiryu1223.expressionTree.delegate.Func8;
import io.github.kiryu1223.expressionTree.delegate.Func9;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;
import io.github.kiryu1223.expressionTree.expressions.annos.Expr;
import io.github.kiryu1223.expressionTree.expressions.annos.Recode;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.core.api.Result;
import io.github.kiryu1223.drink.core.api.crud.read.group.Grouper;
import io.github.kiryu1223.drink.core.exception.NotCompiledException;
import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;
import io.github.kiryu1223.drink.core.api.crud.read.group.*;

/**
 * 查询过程对象
 *
 * @author kiryu1223
 * @since 3.0
 */
public class LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> extends QueryBase<LQuery8<T1, T2, T3, T4, T5, T6, T7, T8>> {
    // region [INIT]

    public LQuery8(QuerySqlBuilder sqlBuilder) {
        super(sqlBuilder);
    }

    // endregion

    //region [JOIN]

    protected <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> joinNewQuery() {
        return new LQuery9<>(getSqlBuilder());
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
    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> innerJoin(Class<Tn> target, @Expr(Expr.BodyType.Expr) Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> innerJoin(Class<Tn> target, ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean>> expr) {
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
    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> innerJoin(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> innerJoin(LQuery<Tn> target, ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean>> expr) {
        join(JoinType.INNER, target, expr.getTree());
        return joinNewQuery();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> innerJoinWith(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> innerJoinWith(LQuery<Tn> target, ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean>> expr) {
        joinWith(JoinType.INNER, target, expr.getTree());
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
    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> leftJoin(Class<Tn> target, @Expr(Expr.BodyType.Expr) Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> leftJoin(Class<Tn> target, ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean>> expr) {
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
    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> leftJoin(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> leftJoin(LQuery<Tn> target, ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean>> expr) {
        join(JoinType.LEFT, target, expr.getTree());
        return joinNewQuery();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> leftJoinWith(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> leftJoinWith(LQuery<Tn> target, ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean>> expr) {
        joinWith(JoinType.LEFT, target, expr.getTree());
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
    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> rightJoin(Class<Tn> target, @Expr(Expr.BodyType.Expr) Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> rightJoin(Class<Tn> target, ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean>> expr) {
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
    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> rightJoin(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> rightJoin(LQuery<Tn> target, ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean>> expr) {
        join(JoinType.RIGHT, target, expr.getTree());
        return joinNewQuery();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> rightJoinWith(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> rightJoinWith(LQuery<Tn> target, ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean>> expr) {
        joinWith(JoinType.RIGHT, target, expr.getTree());
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
    public LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> where(@Expr(Expr.BodyType.Expr) Func8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean> func) {
        throw new NotCompiledException();
    }

    public LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> where(ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean>> expr) {
        where(expr.getTree());
        return this;
    }

    public LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> whereIf(boolean condition, @Expr(Expr.BodyType.Expr) Func8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean> func) {
        throw new NotCompiledException();
    }

    public LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> whereIf(boolean condition, ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean>> expr) {
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
    public LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> orWhere(@Expr(Expr.BodyType.Expr) Func8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean> func) {
        throw new NotCompiledException();
    }

    public LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> orWhere(ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean>> expr) {
        orWhere(expr.getTree());
        return this;
    }

    public LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> orWhereIf(boolean condition, @Expr(Expr.BodyType.Expr) Func8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean> func) {
        throw new NotCompiledException();
    }

    public LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> orWhereIf(boolean condition, ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean>> expr) {
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
    public  <R extends Comparable<R>> LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> orderBy(@Expr(Expr.BodyType.Expr) Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> expr, boolean asc) {
        throw new NotCompiledException();
    }

    public  <R extends Comparable<R>> LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> orderBy(ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, R>> expr, boolean asc) {
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
    public  <R extends Comparable<R>> LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> orderBy(@Expr(Expr.BodyType.Expr) Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> expr) {
        throw new NotCompiledException();
    }

    public  <R extends Comparable<R>> LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> orderBy(ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, R>> expr) {
        orderBy(expr.getTree(), true);
        return this;
    }

    public  <R extends Comparable<R>> LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> orderByDesc(@Expr(Expr.BodyType.Expr) Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> expr) {
        throw new NotCompiledException();
    }

    public  <R extends Comparable<R>> LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> orderByDesc(ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, R>> expr) {
        orderBy(expr, false);
        return this;
    }

    public  <R extends Comparable<R>> LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> orderByIf(boolean condition, @Expr(Expr.BodyType.Expr) Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> expr, boolean asc) {
        throw new NotCompiledException();
    }

    public  <R extends Comparable<R>> LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> orderByIf(boolean condition, ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, R>> expr, boolean asc) {
        if (condition) orderBy(expr.getTree(), asc);
        return this;
    }

    public  <R extends Comparable<R>> LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> orderByIf(boolean condition, @Expr(Expr.BodyType.Expr) Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> expr) {
        throw new NotCompiledException();
    }

    public  <R extends Comparable<R>> LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> orderByIf(boolean condition, ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, R>> expr) {
        if (condition) orderBy(expr, true);
        return this;
    }

    public  <R extends Comparable<R>> LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> orderByDescIf(boolean condition, @Expr(Expr.BodyType.Expr) Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> expr) {
        throw new NotCompiledException();
    }

    public  <R extends Comparable<R>> LQuery8<T1, T2, T3, T4, T5, T6, T7, T8> orderByDescIf(boolean condition, ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, R>> expr) {
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
    public <Key extends Grouper> GroupedQuery8<? extends Key, T1, T2, T3, T4, T5, T6, T7, T8> groupBy(@Expr Func8<T1, T2, T3, T4, T5, T6, T7, T8, Key> expr) {
        throw new NotCompiledException();
    }

    public <Key extends Grouper> GroupedQuery8<? extends Key, T1, T2, T3, T4, T5, T6, T7, T8> groupBy(ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, Key>> expr) {
        groupBy(expr.getTree());
        return new GroupedQuery8<>(getSqlBuilder());
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
    public <R> EndQuery<R> select(@Expr(Expr.BodyType.Expr) Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> expr) {
        throw new NotCompiledException();
    }

    public <R> EndQuery<R> select(ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, R>> expr) {
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
//    public <R> EndQuery<R> endSelect(@Expr(Expr.BodyType.Expr) Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> expr) {
//        throw new NotCompiledException();
//    }
//
//    public <R> EndQuery<R> endSelect(ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, R>> expr) {
//        select(expr.getTree());
//        return new EndQuery<>(getSqlBuilder());
//    }

    // endregion
}
