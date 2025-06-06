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

import io.github.kiryu1223.drink.core.api.Result;
import io.github.kiryu1223.drink.core.exception.NotCompiledException;
import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;
import io.github.kiryu1223.expressionTree.delegate.Func0;
import io.github.kiryu1223.expressionTree.delegate.Func2;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;
import io.github.kiryu1223.expressionTree.expressions.annos.Expr;

/**
 * 空表查询过程（用于数据库计算）
 *
 * @author kiryu1223
 * @since 3.0
 */
public class EmptyQuery extends QueryBase<EmptyQuery,Void> {
    public EmptyQuery(QuerySqlBuilder sqlBuilder) {
        super(sqlBuilder);
    }

    // region [SELECT]

    /**
     * 设置select<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param expr 返回一个继承于Result的匿名对象的lambda表达式((a) -> new Result(){...})，初始化段{...}内编写需要select的字段(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @param <R>  Result
     * @return 基于Result类型的新查询过程对象
     */
    public <R> EndQuery<R> select(@Expr(Expr.BodyType.Expr) Func0<R> expr) {
        throw new NotCompiledException();
    }

    public <R> EndQuery<R> select(ExprTree<Func0<R>> expr) {
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
//    public <R> EndQuery<R> endSelect(@Expr(Expr.BodyType.Expr) Func2<T1, T2, R> expr) {
//        throw new NotCompiledException();
//    }
//
//    public <R> EndQuery<R> endSelect(ExprTree<Func2<T1, T2, R>> expr) {
//        select(expr.getTree());
//        return new EndQuery<>(getSqlBuilder());
//    }

    // endregion

    // region [WITH]

    public <R extends Result> LQuery<? extends R> withTemp(@Expr(Expr.BodyType.Expr) Func0<R> expr) {
        throw new NotCompiledException();
    }

    public <R extends Result> LQuery<? extends R> withTemp(ExprTree<Func0<R>> expr) {
        select(expr.getTree());
        withTempQuery();
        return new LQuery<>(getSqlBuilder());
    }

    // endregion
}
