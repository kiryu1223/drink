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

import io.github.kiryu1223.drink.base.page.DefaultPager;
import io.github.kiryu1223.drink.base.page.PagedResult;
import io.github.kiryu1223.drink.core.exception.NotCompiledException;
import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;
import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;
import io.github.kiryu1223.expressionTree.expressions.annos.Expr;

import java.util.List;

/**
 * 终结查询过程
 *
 * @author kiryu1223
 * @since 3.0
 */
public class EndQuery<T> extends QueryBase<EndQuery<T>,T> {
    public EndQuery(QuerySqlBuilder sqlBuilder) {
        super(sqlBuilder);
    }

    /**
     * list集合形式返回数据，无数据则返回空list
     *
     * @return List
     */
    @Override
    public List<T> toList() {
        return super.toList();
    }

    /**
     * 返回一条数据，会调用各种数据库limit 1的具体实现，无数据则返回null
     *
     * @return T
     */
    @Override
    public T first() {
        return super.first();
    }

    /**
     * 分页返回数据，无数据则返回空List
     *
     * @param pageIndex 页编号 默认1开始
     * @param pageSize  页长度 默认大于等于1
     * @return 分页数据
     */
    public PagedResult<T> toPagedResult(long pageIndex, long pageSize) {
        return toPagedResult0(pageIndex, pageSize);
    }

    /**
     * 分页返回数据，无数据则返回空List
     *
     * @param pageIndex 页编号 默认1开始
     * @param pageSize  页长度 默认大于等于1
     * @return 分页数据
     */
    public PagedResult<T> toPagedResult(int pageIndex, int pageSize) {
        return toPagedResult((long) pageIndex, (long) pageSize);
    }

    /**
     * 忽略选择的字段
     */
    public <R> EndQuery<T> ignoreColumn(@Expr(Expr.BodyType.Expr) Func1<T,R> expr)
    {
        throw new NotCompiledException();
    }

    public <R> EndQuery<T> ignoreColumn(ExprTree<Func1<T,R>> expr)
    {
        ignoreColumn(expr.getTree());
        return this;
    }
}
