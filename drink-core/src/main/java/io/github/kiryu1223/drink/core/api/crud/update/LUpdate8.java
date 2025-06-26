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
package io.github.kiryu1223.drink.core.api.crud.update;

import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.delegate.Func3;
import io.github.kiryu1223.expressionTree.delegate.Func8;
import io.github.kiryu1223.expressionTree.delegate.Func9;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;
import io.github.kiryu1223.expressionTree.expressions.annos.Expr;
import io.github.kiryu1223.drink.base.expression.JoinType;
import io.github.kiryu1223.drink.core.exception.NotCompiledException;
import io.github.kiryu1223.drink.core.sqlBuilder.UpdateSqlBuilder;
/**
 * @author kiryu1223
 * @since 3.0
 */
public class LUpdate8<T1, T2, T3, T4, T5, T6, T7, T8> extends UpdateBase<LUpdate8<T1, T2, T3, T4, T5, T6, T7, T8>> {
    public LUpdate8(UpdateSqlBuilder sqlBuilder) {
        super(sqlBuilder);
    }

    //region [JOIN]

    /**
     * join表操作<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param target 数据表类
     * @param func   返回bool的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @param <Tn>   join过来的表的类型
     * @return 泛型数量+1的更新过程对象
     */
    public <Tn> LUpdate9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> innerJoin(Class<Tn> target, @Expr Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LUpdate9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> innerJoin(Class<Tn> target, ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean>> expr) {
        join(JoinType.INNER, target, expr);
        return new LUpdate9<>(getSqlBuilder());
    }

    /**
     * join表操作<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param target 数据表类
     * @param func   返回bool的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @param <Tn>   join过来的表的类型
     * @return 泛型数量+1的更新过程对象
     */
    public <Tn> LUpdate9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> leftJoin(Class<Tn> target, @Expr Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LUpdate9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> leftJoin(Class<Tn> target, ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean>> expr) {
        join(JoinType.LEFT, target, expr);
        return new LUpdate9<>(getSqlBuilder());
    }

    /**
     * join表操作<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param target 数据表类
     * @param func   返回bool的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @param <Tn>   join过来的表的类型
     * @return 泛型数量+1的更新过程对象
     */
    public <Tn> LUpdate9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> rightJoin(Class<Tn> target, @Expr Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LUpdate9<T1, T2, T3, T4, T5, T6, T7, T8, Tn> rightJoin(Class<Tn> target, ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, Tn, Boolean>> expr) {
        join(JoinType.RIGHT, target, expr);
        return new LUpdate9<>(getSqlBuilder());
    }
    //endregion

    //region [SET]

    /**
     * 选择需要更新的字段和值
     * <p><b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param func 需要更新的字段(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @return this
     */
    public <R> LUpdate8<T1, T2, T3, T4, T5, T6, T7, T8> set(@Expr(Expr.BodyType.Expr) Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> func, R value) {
        throw new NotCompiledException();
    }

    public <R> LUpdate8<T1, T2, T3, T4, T5, T6, T7, T8> set(ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, R>> func, R value) {
        set(func.getTree(), value);
        return this;
    }

    public <R> LUpdate8<T1, T2, T3, T4, T5, T6, T7, T8> setColumn(@Expr(Expr.BodyType.Expr) Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> func,@Expr(Expr.BodyType.Expr) Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> value) {
        throw new NotCompiledException();
    }

    public <R> LUpdate8<T1, T2, T3, T4, T5, T6, T7, T8> setColumn(ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, R>> func, ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, R>> value) {
        set(func.getTree(), value.getTree());
        return this;
    }

    public <R> LUpdate8<T1, T2, T3, T4, T5, T6, T7, T8> setIf(boolean condition, @Expr(Expr.BodyType.Expr) Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> func, R value) {
        throw new NotCompiledException();
    }

    public <R> LUpdate8<T1, T2, T3, T4, T5, T6, T7, T8> setIf(boolean condition, ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, R>> func, R value) {
        if (condition) set(func.getTree(), value);
        return this;
    }

    public <R> LUpdate8<T1, T2, T3, T4, T5, T6, T7, T8> setColumnIf(boolean condition, @Expr(Expr.BodyType.Expr) Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> func,@Expr(Expr.BodyType.Expr) Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> value) {
        throw new NotCompiledException();
    }

    public <R> LUpdate8<T1, T2, T3, T4, T5, T6, T7, T8> setColumnIf(boolean condition, ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, R>> func, ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, R>> value) {
        if (condition) set(func.getTree(), value.getTree());
        return this;
    }

    /**
     * 当value不为null时更新字段
     */
    public <R> LUpdate8<T1, T2, T3, T4, T5, T6, T7, T8> setIfNotNull(@Expr(Expr.BodyType.Expr) Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> func, R value) {
        throw new NotCompiledException();
    }

    public <R> LUpdate8<T1, T2, T3, T4, T5, T6, T7, T8> setIfNotNull(ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, R>> func, R value) {
        setIf(value != null, func, value);
        return this;
    }

    /**
     * 当value匹配条件时更新字段
     */
    public <R> LUpdate8<T1, T2, T3, T4, T5, T6, T7, T8> setIfMatch(@Expr(Expr.BodyType.Expr) Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> func, R value, Func1<R,Boolean> predicate) {
        throw new NotCompiledException();
    }

    public <R> LUpdate8<T1, T2, T3, T4, T5, T6, T7, T8> setIfMatch(ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, R>> func, R value,Func1<R,Boolean> predicate) {
        setIf(predicate.invoke(value), func, value);
        return this;
    }

    //endregion

    //region [WHERE]

    /**
     * 设置where条件<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param func 返回bool的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @return this
     */
    public LUpdate8<T1, T2, T3, T4, T5, T6, T7, T8> where(@Expr Func8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean> func) {
        throw new NotCompiledException();
    }

    public LUpdate8<T1, T2, T3, T4, T5, T6, T7, T8> where(ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean>> expr) {
        where(expr.getTree());
        return this;
    }

    public LUpdate8<T1, T2, T3, T4, T5, T6, T7, T8> whereIf(boolean condition, @Expr Func8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean> func) {
        throw new NotCompiledException();
    }

    public LUpdate8<T1, T2, T3, T4, T5, T6, T7, T8> whereIf(boolean condition, ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean>> expr) {
        if (condition) where(expr.getTree());
        return this;
    }

    public LUpdate8<T1, T2, T3, T4, T5, T6, T7, T8> orWhere(@Expr Func8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean> func) {
        throw new NotCompiledException();
    }

    public LUpdate8<T1, T2, T3, T4, T5, T6, T7, T8> orWhere(ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean>> expr) {
        orWhere(expr.getTree());
        return this;
    }

    public LUpdate8<T1, T2, T3, T4, T5, T6, T7, T8> orWhereIf(boolean condition, @Expr Func8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean> func) {
        throw new NotCompiledException();
    }

    public LUpdate8<T1, T2, T3, T4, T5, T6, T7, T8> orWhereIf(boolean condition, ExprTree<Func8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean>> expr) {
        if (condition) orWhere(expr.getTree());
        return this;
    }

    //endregion
}
