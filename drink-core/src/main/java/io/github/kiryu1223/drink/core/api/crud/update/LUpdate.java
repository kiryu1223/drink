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
import io.github.kiryu1223.expressionTree.delegate.Func2;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;
import io.github.kiryu1223.expressionTree.expressions.annos.Expr;
import io.github.kiryu1223.drink.base.expression.JoinType;
import io.github.kiryu1223.drink.core.exception.NotCompiledException;
import io.github.kiryu1223.drink.core.sqlBuilder.UpdateSqlBuilder;

/**
 * 更新过程对象
 *
 * @author kiryu1223
 * @since 3.0
 */
public class LUpdate<T> extends UpdateBase<LUpdate<T>> {
    public LUpdate(UpdateSqlBuilder updateSqlBuilder) {
        super(updateSqlBuilder);
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
    public <Tn> LUpdate2<T, Tn> innerJoin(Class<Tn> target, @Expr Func2<T, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LUpdate2<T, Tn> innerJoin(Class<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr) {
        join(JoinType.INNER, target, expr);
        return new LUpdate2<>(getSqlBuilder());
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
    public <Tn> LUpdate2<T, Tn> leftJoin(Class<Tn> target, @Expr Func2<T, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LUpdate2<T, Tn> leftJoin(Class<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr) {
        join(JoinType.LEFT, target, expr);
        return new LUpdate2<>(getSqlBuilder());
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
    public <Tn> LUpdate2<T, Tn> rightJoin(Class<Tn> target, @Expr Func2<T, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LUpdate2<T, Tn> rightJoin(Class<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr) {
        join(JoinType.RIGHT, target, expr);
        return new LUpdate2<>(getSqlBuilder());
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
    public <R> LUpdate<T> set(@Expr(Expr.BodyType.Expr) Func1<T, R> func, R value) {
        throw new NotCompiledException();
    }

    public <R> LUpdate<T> set(ExprTree<Func1<T, R>> func, R value) {
        set(func.getTree(), value);
        return this;
    }

    public <R> LUpdate<T> setColumn(@Expr(Expr.BodyType.Expr) Func1<T, R> func, Func1<T, R> value) {
        throw new NotCompiledException();
    }

    public <R> LUpdate<T> setColumn(ExprTree<Func1<T, R>> func, ExprTree<Func1<T, R>> value) {
        set(func.getTree(), value.getTree());
        return this;
    }

    public <R> LUpdate<T> setIf(boolean condition, @Expr(Expr.BodyType.Expr) Func1<T, R> func, R value) {
        throw new NotCompiledException();
    }

    public <R> LUpdate<T> setIf(boolean condition, ExprTree<Func1<T, R>> func, R value) {
        if (condition) set(func.getTree(), value);
        return this;
    }

    public <R> LUpdate<T> setInDbIf(boolean condition, @Expr(Expr.BodyType.Expr) Func1<T, R> func, Func1<T, R> value) {
        throw new NotCompiledException();
    }

    public <R> LUpdate<T> setInDbIf(boolean condition, ExprTree<Func1<T, R>> func, ExprTree<Func1<T, R>> value) {
        if (condition) set(func.getTree(), value.getTree());
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
    public LUpdate<T> where(@Expr Func1<T, Boolean> func) {
        throw new NotCompiledException();
    }

    public LUpdate<T> where(ExprTree<Func1<T, Boolean>> expr) {
        where(expr.getTree());
        return this;
    }

    public LUpdate<T> whereIf(boolean condition, @Expr Func1<T, Boolean> func) {
        throw new NotCompiledException();
    }

    public LUpdate<T> whereIf(boolean condition, ExprTree<Func1<T, Boolean>> expr) {
        if (condition) where(expr.getTree());
        return this;
    }

    //endregion
}
