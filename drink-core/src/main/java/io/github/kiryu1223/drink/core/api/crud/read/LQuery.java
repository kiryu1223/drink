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

import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.metaData.NavigateData;
import io.github.kiryu1223.drink.core.api.ITable;
import io.github.kiryu1223.drink.core.api.Result;
import io.github.kiryu1223.drink.core.api.crud.delete.LDelete;
import io.github.kiryu1223.drink.core.api.crud.read.group.GroupedQuery;
import io.github.kiryu1223.drink.core.api.crud.read.group.Grouper;
import io.github.kiryu1223.drink.core.api.crud.read.pivot.Pivoted;
import io.github.kiryu1223.drink.core.api.crud.read.pivot.TransPair;
import io.github.kiryu1223.drink.core.api.crud.read.pivot.UnPivoted;
import io.github.kiryu1223.drink.core.api.crud.update.LUpdate;
import io.github.kiryu1223.drink.core.exception.NotCompiledException;
import io.github.kiryu1223.drink.core.exception.SqLinkException;
import io.github.kiryu1223.drink.base.page.PagedResult;
import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;
import io.github.kiryu1223.drink.core.sqlBuilder.IncludeBuilder;
import io.github.kiryu1223.drink.core.visitor.QuerySqlVisitor;
import io.github.kiryu1223.expressionTree.delegate.Action1;
import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.delegate.Func2;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;
import io.github.kiryu1223.expressionTree.expressions.LambdaExpression;
import io.github.kiryu1223.expressionTree.expressions.annos.Expr;
import io.github.kiryu1223.expressionTree.expressions.annos.Recode;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.kiryu1223.drink.core.util.ExpressionUtil.*;


/**
 * 查询过程对象
 *
 * @author kiryu1223
 * @since 3.0
 */
public class LQuery<T> extends QueryBase<LQuery<T>, T> {
    // region [INIT]

    public LQuery(QuerySqlBuilder sqlBuilder) {
        super(sqlBuilder);
    }

    // endregion

    //region [JOIN]

    protected <Tn> LQuery2<T, Tn> joinNewQuery() {
        return new LQuery2<>(getSqlBuilder());
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
    public <Tn> LQuery2<T, Tn> innerJoin(Class<Tn> target, @Expr(Expr.BodyType.Expr) Func2<T, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery2<T, Tn> innerJoin(Class<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr) {
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
    public <Tn> LQuery2<T, Tn> innerJoin(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func2<T, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery2<T, Tn> innerJoin(LQuery<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr) {
        join(JoinType.INNER, target, expr.getTree());
        return joinNewQuery();
    }

//    public <Tn> LQuery2<T, Tn> innerJoinWith(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func2<T, Tn, Boolean> func) {
//        throw new NotCompiledException();
//    }
//
//    public <Tn> LQuery2<T, Tn> innerJoinWith(LQuery<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr) {
//        joinWith(JoinType.INNER, target, expr.getTree());
//        return joinNewQuery();
//    }

    /**
     * join表操作<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param target 数据表类或查询过程
     * @param func   返回bool的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @param <Tn>   join过来的表的类型
     * @return 泛型数量+1的查询过程对象
     */
    public <Tn> LQuery2<T, Tn> leftJoin(Class<Tn> target, @Expr(Expr.BodyType.Expr) Func2<T, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery2<T, Tn> leftJoin(Class<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr) {
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
    public <Tn> LQuery2<T, Tn> leftJoin(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func2<T, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery2<T, Tn> leftJoin(LQuery<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr) {
        join(JoinType.LEFT, target, expr.getTree());
        return joinNewQuery();
    }

//    public <Tn> LQuery2<T, Tn> leftJoinWith(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func2<T, Tn, Boolean> func) {
//        throw new NotCompiledException();
//    }
//
//    public <Tn> LQuery2<T, Tn> leftJoinWith(LQuery<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr) {
//        joinWith(JoinType.LEFT, target, expr.getTree());
//        return joinNewQuery();
//    }

    /**
     * join表操作<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param target 数据表类或查询过程
     * @param func   返回bool的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @param <Tn>   join过来的表的类型
     * @return 泛型数量+1的查询过程对象
     */
    public <Tn> LQuery2<T, Tn> leftJoin(EndQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func2<T, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery2<T, Tn> leftJoin(EndQuery<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr) {
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
    public <Tn> LQuery2<T, Tn> rightJoin(Class<Tn> target, @Expr(Expr.BodyType.Expr) Func2<T, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery2<T, Tn> rightJoin(Class<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr) {
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
    public <Tn> LQuery2<T, Tn> rightJoin(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func2<T, Tn, Boolean> func) {
        throw new NotCompiledException();
    }

    public <Tn> LQuery2<T, Tn> rightJoin(LQuery<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr) {
        join(JoinType.RIGHT, target, expr.getTree());
        return joinNewQuery();
    }

//    public <Tn> LQuery2<T, Tn> rightJoinWith(LQuery<Tn> target, @Expr(Expr.BodyType.Expr) Func2<T, Tn, Boolean> func) {
//        throw new NotCompiledException();
//    }
//
//    public <Tn> LQuery2<T, Tn> rightJoinWith(LQuery<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr) {
//        joinWith(JoinType.RIGHT, target, expr.getTree());
//        return joinNewQuery();
//    }

    //endregion

    // region [WHERE]

    /**
     * 设置where条件<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param func 返回bool的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @return this
     */
    public LQuery<T> where(@Expr(Expr.BodyType.Expr) Func1<T, Boolean> func) {
        throw new NotCompiledException();
    }

    public LQuery<T> where(ExprTree<Func1<T, Boolean>> expr) {
        where(expr.getTree());
        return this;
    }

    public LQuery<T> whereIf(boolean condition, @Expr(Expr.BodyType.Expr) Func1<T, Boolean> func) {
        throw new NotCompiledException();
    }

    public LQuery<T> whereIf(boolean condition, ExprTree<Func1<T, Boolean>> expr) {
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
    public LQuery<T> orWhere(@Expr(Expr.BodyType.Expr) Func1<T, Boolean> func) {
        throw new NotCompiledException();
    }

    public LQuery<T> orWhere(ExprTree<Func1<T, Boolean>> expr) {
        orWhere(expr.getTree());
        return this;
    }

    public LQuery<T> orWhereIf(boolean condition, @Expr(Expr.BodyType.Expr) Func1<T, Boolean> func) {
        throw new NotCompiledException();
    }

    public LQuery<T> orWhereIf(boolean condition, ExprTree<Func1<T, Boolean>> expr) {
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
    public <R extends Comparable<R>> LQuery<T> orderBy(@Expr(Expr.BodyType.Expr) Func1<T, R> expr, boolean asc) {
        throw new NotCompiledException();
    }

    public <R extends Comparable<R>> LQuery<T> orderBy(ExprTree<Func1<T, R>> expr, boolean asc) {
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
    public <R extends Comparable<R>> LQuery<T> orderBy(@Expr(Expr.BodyType.Expr) Func1<T, R> expr) {
        throw new NotCompiledException();
    }

    public <R extends Comparable<R>> LQuery<T> orderBy(ExprTree<Func1<T, R>> expr) {
        orderBy(expr, true);
        return this;
    }

    public <R extends Comparable<R>> LQuery<T> orderByDesc(@Expr(Expr.BodyType.Expr) Func1<T, R> expr) {
        throw new NotCompiledException();
    }

    public <R extends Comparable<R>> LQuery<T> orderByDesc(ExprTree<Func1<T, R>> expr) {
        orderBy(expr, false);
        return this;
    }

    public <R extends Comparable<R>> LQuery<T> orderByIf(boolean condition, @Expr(Expr.BodyType.Expr) Func1<T, R> expr, boolean asc) {
        throw new NotCompiledException();
    }

    public <R extends Comparable<R>> LQuery<T> orderByIf(boolean condition, ExprTree<Func1<T, R>> expr, boolean asc) {
        if (condition) orderBy(expr.getTree(), asc);
        return this;
    }

    public <R extends Comparable<R>> LQuery<T> orderByIf(boolean condition, @Expr(Expr.BodyType.Expr) Func1<T, R> expr) {
        throw new NotCompiledException();
    }

    public <R extends Comparable<R>> LQuery<T> orderByIf(boolean condition, ExprTree<Func1<T, R>> expr) {
        if (condition) orderBy(expr, true);
        return this;
    }

    public <R extends Comparable<R>> LQuery<T> orderByDescIf(boolean condition, @Expr(Expr.BodyType.Expr) Func1<T, R> expr) {
        throw new NotCompiledException();
    }

    public <R extends Comparable<R>> LQuery<T> orderByDescIf(boolean condition, ExprTree<Func1<T, R>> expr) {
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
    public <Key extends Grouper> GroupedQuery<? extends Key, T> groupBy(@Expr Func1<T, Key> expr) {
        throw new NotCompiledException();
    }

    public <Key extends Grouper> GroupedQuery<? extends Key, T> groupBy(ExprTree<Func1<T, Key>> expr) {
        groupBy(expr.getTree());
        return new GroupedQuery<>(getSqlBuilder());
    }

    // endregion

    // region [SELECT]

    /**
     * 设置select，根据指定的类型的字段匹配去生成选择的sql字段
     *
     * @param r   指定的返回类型
     * @param <R> 指定的返回类型
     * @return 终结查询过程
     */
    public <R> EndQuery<R> select(@Recode Class<R> r) {
        return super.select(r);
    }

    /**
     * 设置select<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param expr 返回一个继承于Result的匿名对象的lambda表达式((a) -> new Result(){...})，初始化段{...}内编写需要select的字段(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     * @param <R>  Result
     * @return 基于Result类型的新查询过程对象
     */
    public <R> EndQuery<? extends R> select(@Expr(Expr.BodyType.Expr) Func1<T, R> expr) {
        throw new NotCompiledException();
    }

    public <R> EndQuery<? extends R> select(ExprTree<Func1<T, R>> expr) {
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
//    public <R> EndQuery<R> endSelect(@Expr(Expr.BodyType.Expr) Func1<T, R> expr) {
//        throw new NotCompiledException();
//    }
//
//    public <R> EndQuery<R> endSelect(ExprTree<Func1<T, R>> expr) {
//        select(expr.getTree());
//        return new EndQuery<>(getSqlBuilder());
//    }
//
    public <R extends ITable> LQuery<R> selectMany(@Expr(Expr.BodyType.Expr) Func1<T, Collection<R>> expr) {
        throw new NotCompiledException();
    }

    public <R extends ITable> LQuery<R> selectMany(ExprTree<Func1<T, Collection<R>>> expr) {
        return new LQuery<>(toMany(expr.getTree()));
    }


    public <R> EndQuery<? extends R> selectAggregate(@Expr(Expr.BodyType.Expr) Func1<Aggregate<T>, R> expr) {
        throw new NotCompiledException();
    }

    public <R> EndQuery<? extends R> selectAggregate(ExprTree<Func1<Aggregate<T>, R>> expr) {
        select(expr.getTree());
        return new EndQuery<>(getSqlBuilder());
    }

    // endregion

    // region [INCLUDE]

    /**
     * 对象抓取器，会根据导航属性自动为选择的字段填充属性<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param expr 返回需要抓取的字段的lambda表达式，这个字段需要被Navigate修饰
     * @return 抓取过程对象
     */
    public <R extends ITable> LQuery<T> include(@Expr(Expr.BodyType.Expr) Func1<T, R> expr) {
        throw new NotCompiledException();
    }

    public <R extends ITable> LQuery<T> include(ExprTree<Func1<T, R>> expr) {
        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), getSqlBuilder().getQueryable());
        FieldMetaData include = sqlVisitor.toField(expr.getTree());
        if (!include.hasNavigate()) throw new DrinkException("include指定的字段需要被@Navigate修饰");
        include(include, getConfig().getSqlExpressionFactory().tableRef(include.getNavigateData().getNavigateTargetType()), null);
        return this;
    }

    /**
     * 对象抓取器，会根据导航属性自动为选择的字段填充属性,并且设置简单的过滤条件<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param expr 返回需要抓取的字段的lambda表达式，这个字段需要被Navigate修饰
     * @param then 简单的过滤条件
     * @return 抓取过程对象
     */
    public <R extends ITable> LQuery<T> include(@Expr(Expr.BodyType.Expr) Func1<T, R> expr, Func1<LQuery<R>, LQuery<R>> then) {
        throw new NotCompiledException();
    }

    public <R extends ITable> LQuery<T> include(ExprTree<Func1<T, R>> expr, Func1<LQuery<R>, LQuery<R>> then) {
        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), getSqlBuilder().getQueryable());
        FieldMetaData include = sqlVisitor.toField(expr.getTree());
        if (!include.hasNavigate()) throw new DrinkException("include指定的字段需要被@Navigate修饰");
        LambdaExpression<Func1<T, R>> tree = expr.getTree();
        Class<?> targetType = tree.getReturnType();
        LQuery<R> lQuery = new LQuery<>(new QuerySqlBuilder(getConfig(), getConfig().getSqlExpressionFactory().queryable(targetType)));
        lQuery = then.invoke(lQuery);
        QuerySqlBuilder sqlBuilder = lQuery.getSqlBuilder();
        ISqlQueryableExpression queryable = sqlBuilder.getQueryable();
        include(include, queryable.getFrom().getTableRefExpression(), queryable);
        if (!sqlBuilder.getIncludes().isEmpty()) {
            List<IncludeBuilder> subQueryList = getSqlBuilder().getIncludes();
            subQueryList.get(subQueryList.size() - 1).getIncludes().addAll(sqlBuilder.getIncludes());
        }
        return this;
    }

    /**
     * include的集合版本
     */
    public <R extends ITable> LQuery<T> includeMany(@Expr(Expr.BodyType.Expr) Func1<T, Collection<R>> expr) {
        throw new NotCompiledException();
    }

    public <R extends ITable> LQuery<T> includeMany(ExprTree<Func1<T, Collection<R>>> expr) {
        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), getSqlBuilder().getQueryable());
        FieldMetaData include = sqlVisitor.toField(expr.getTree());
        if (!include.hasNavigate()) throw new DrinkException("include指定的字段需要被@Navigate修饰");
        include(include, getConfig().getSqlExpressionFactory().tableRef(include.getNavigateData().getNavigateTargetType()), null);
        return this;
    }

    /**
     * include的集合版本
     */
    public <R extends ITable> LQuery<T> includeMany(@Expr(Expr.BodyType.Expr) Func1<T, Collection<R>> expr, Func1<LQuery<R>, LQuery<R>> then) {
        throw new NotCompiledException();
    }

    public <R extends ITable> LQuery<T> includeMany(ExprTree<Func1<T, Collection<R>>> expr, Func1<LQuery<R>, LQuery<R>> then) {
        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), getSqlBuilder().getQueryable());
        FieldMetaData include = sqlVisitor.toField(expr.getTree());
        if (!include.hasNavigate()) throw new DrinkException("include指定的字段需要被@Navigate修饰");
        FieldMetaData fieldMetaData = sqlVisitor.toField(expr.getTree());
        Class<?> targetType = fieldMetaData.getNavigateData().getNavigateTargetType();
        LQuery<R> lQuery = new LQuery<>(new QuerySqlBuilder(getConfig(), getConfig().getSqlExpressionFactory().queryable(targetType)));
        then.invoke(lQuery);
        QuerySqlBuilder sqlBuilder = lQuery.getSqlBuilder();
        ISqlQueryableExpression queryable = sqlBuilder.getQueryable();
        include(include, queryable.getFrom().getTableRefExpression(), queryable);
        if (!sqlBuilder.getIncludes().isEmpty()) {
            List<IncludeBuilder> subQueryList = getSqlBuilder().getIncludes();
            subQueryList.get(subQueryList.size() - 1).getIncludes().addAll(sqlBuilder.getIncludes());
        }
        return this;
    }

    // endregion

    // region [toAny]

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
     * list集合形式返回数据，无数据则返回空list
     *
     * @return List
     */
    public List<T> toList() {
        return super.toList();
    }

//    /**
//     * list集合形式返回数据，并且执行你想要对已写入内存中的数据进行的操作，执行后再返回list
//     *
//     * @param func 执行操作的lambda
//     * @return List
//     */
//    public <R> List<R> toList(Func1<T, R> func)
//    {
//        List<T> list = toList();
//        List<R> rList = new ArrayList<>(list.size());
//        for (T t : list)
//        {
//            rList.add(func.invoke(t));
//        }
//        return rList;
//    }

    /**
     * Map形式返回数据，无数据则返回空Map
     *
     * @param func 指定一个key
     * @return Map
     */
    public <K> Map<K, T> toMap(Func1<T, K> func) {
        return toMap(func, new HashMap<>());
    }

    /**
     * Map形式返回数据，无数据则返回空Map
     *
     * @param func 指定一个key
     * @param map  指定你想要的Map类型
     * @return Map
     */
    public <K> Map<K, T> toMap(Func1<T, K> func, Map<K, T> map) {
        for (T t : toList()) {
            map.put(func.invoke(t), t);
        }
        return map;
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
     * 返回树形数据(内存排序)
     */
    public List<T> toTreeList(@Expr(Expr.BodyType.Expr) Func1<T, Collection<T>> expr) {
        throw new NotCompiledException();
    }

    public List<T> toTreeList(ExprTree<Func1<T, Collection<T>>> expr) {
        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), getSqlBuilder().getQueryable());
        FieldMetaData fieldMetaData = sqlVisitor.toField(expr.getTree());
        if (!fieldMetaData.hasNavigate()) {
            throw new SqLinkException("toTreeList指定的字段需要被@Navigate修饰");
        }
        NavigateData navigateData = fieldMetaData.getNavigateData();
        MetaData metaData = getConfig().getMetaData(fieldMetaData.getParentType());
        FieldMetaData parent = metaData.getFieldMetaDataByFieldName(navigateData.getTargetFieldName());
        FieldMetaData child = metaData.getFieldMetaDataByFieldName(navigateData.getSelfFieldName());
        return buildTree(toList(), child, parent, fieldMetaData, expr.getDelegate());
    }
    // endregion

    // region [FAST RETURN]

    public boolean any(@Expr(Expr.BodyType.Expr) Func1<T, Boolean> func) {
        throw new NotCompiledException();
    }

    public boolean any(ExprTree<Func1<T, Boolean>> expr) {
        return any(expr.getTree());
    }

    /**
     * 聚合函数COUNT
     */
    public long count() {
        return count0(null);
    }

    /**
     * 聚合函数COUNT<p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param func 返回需要统计的字段的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
     */
    public <R> long count(@Expr(Expr.BodyType.Expr) Func1<T, R> func) {
        throw new NotCompiledException();
    }

    public <R> long count(ExprTree<Func1<T, R>> expr) {
        return count0(expr.getTree());
    }

    /**
     * 聚合函数SUM
     * <p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param func 返回需要统计的字段的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red)
     */
    public <R extends Number> R sum(@Expr(Expr.BodyType.Expr) Func1<T, R> func) {
        throw new NotCompiledException();
    }

    public <R extends Number> R sum(ExprTree<Func1<T, R>> expr) {
        return sum0(expr.getTree());
    }

    /**
     * 聚合函数AVG
     * <p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param func 返回需要统计的字段的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red))
     */
    public <R extends Number> BigDecimal avg(@Expr(Expr.BodyType.Expr) Func1<T, R> func) {
        throw new NotCompiledException();
    }

    public <R extends Number> BigDecimal avg(ExprTree<Func1<T, R>> expr) {
        return avg0(expr.getTree());
    }

    /**
     * 聚合函数MAX
     * <p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param func 返回需要统计的字段的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red)))
     */
    public <R extends Number> R max(@Expr(Expr.BodyType.Expr) Func1<T, R> func) {
        throw new NotCompiledException();
    }

    public <R extends Number> R max(ExprTree<Func1<T, R>> expr) {
        return max0(expr.getTree());
    }

    /**
     * 聚合函数MIN
     * <p>
     * <b>注意：此函数的ExprTree[func类型]版本为真正被调用的函数
     *
     * @param func 返回需要统计的字段的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red)))
     */
    public <R extends Number> R min(@Expr(Expr.BodyType.Expr) Func1<T, R> func) {
        throw new NotCompiledException();
    }

    public <R extends Number> R min(ExprTree<Func1<T, R>> expr) {
        return min0(expr.getTree());
    }
    // endregion

    // region [WITH]

    public LQuery<T> withTemp() {
        withTempQuery();
        return this;
    }

    public <R extends Result> LQuery<? extends R> withTemp(@Expr(Expr.BodyType.Expr) Func1<T, R> expr) {
        throw new NotCompiledException();
    }

    public <R extends Result> LQuery<? extends R> withTemp(ExprTree<Func1<T, R>> expr) {
        select(expr.getTree());
        withTempQuery();
        return new LQuery<>(getSqlBuilder());
    }

    // endregion

    // region [UNION]

    protected UnionQuery<T> union(ISqlQueryableExpression query, boolean all) {
        return new UnionQuery<>(getConfig(), this.getSqlBuilder().getQueryable(), query, all);
    }

    public UnionQuery<T> union(LQuery<T> query, boolean all) {
        return union(query.getSqlBuilder().getQueryable(), all);
    }

    public UnionQuery<T> union(LQuery<T> query) {
        return union(query.getSqlBuilder().getQueryable(), false);
    }

    public UnionQuery<T> unionAll(LQuery<T> query) {
        return union(query.getSqlBuilder().getQueryable(), true);
    }

    public UnionQuery<T> union(EndQuery<T> query, boolean all) {
        return union(query.getSqlBuilder().getQueryable(), all);
    }

    public UnionQuery<T> union(EndQuery<T> query) {
        return union(query.getSqlBuilder().getQueryable(), false);
    }

    public UnionQuery<T> unionAll(EndQuery<T> query) {
        return union(query.getSqlBuilder().getQueryable(), true);
    }

    // endregion

    // region [CTE]

    public LQuery<T> asTreeCTE(@Expr(Expr.BodyType.Expr) Func1<T, Collection<T>> expr, int level) {
        throw new NotCompiledException();
    }

    public LQuery<T> asTreeCTE(ExprTree<Func1<T, Collection<T>>> expr, int level) {
        QuerySqlBuilder sqlBuilder = getSqlBuilder();
        ISqlQueryableExpression queryable = sqlBuilder.getQueryable();
        QuerySqlVisitor sqlVisitor = new QuerySqlVisitor(getConfig(), queryable);
        FieldMetaData fieldMetaData = sqlVisitor.toField(expr.getTree());
        if (!fieldMetaData.hasNavigate()) {
            throw new SqLinkException("asTreeCTE指定的字段需要被@Navigate修饰");
        }
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        NavigateData navigateData = fieldMetaData.getNavigateData();
        MetaData metaData = getConfig().getMetaData(fieldMetaData.getParentType());
        FieldMetaData parent = metaData.getFieldMetaDataByFieldName(navigateData.getTargetFieldName());
        FieldMetaData child = metaData.getFieldMetaDataByFieldName(navigateData.getSelfFieldName());
        ISqlSelectExpression select = queryable.getSelect().copy(getConfig());
        ISqlTableRefExpression tableRef = queryable.getFrom().getTableRefExpression();
        ISqlRecursionExpression recursion = factory.recursion(queryable, parent, child, level);
        ISqlQueryableExpression newQuery = factory.queryable(select, factory.from(recursion, tableRef));
        sqlBuilder.setQueryable(newQuery);
        return this;
    }

    public LQuery<T> asTreeCTE(@Expr(Expr.BodyType.Expr) Func1<T, Collection<T>> expr) {
        throw new NotCompiledException();
    }

    public LQuery<T> asTreeCTE(ExprTree<Func1<T, Collection<T>>> expr) {
        return asTreeCTE(expr, 0);
    }

    // endregion

    // region [ANOTHER]

    public LDelete<T> toDelete() {
        QuerySqlBuilder sqlBuilder = getSqlBuilder();
        return new LDelete<>(sqlBuilder.toDeleteSqlBuilder());
    }

    public LUpdate<T> toUpdate() {
        QuerySqlBuilder sqlBuilder = getSqlBuilder();
        return new LUpdate<>(sqlBuilder.toUpdateSqlBuilder());
    }

    // endregion

    // region [Pivot(行转列)]

    public <AggColumn,TransColumn,P extends Pivoted<TransColumn,AggColumn>> LQuery<? extends P> pivot(
            // 聚合列
            @Expr(Expr.BodyType.Expr) Func1<Aggregate<T>, AggColumn> aggColumn,
            // 需要转换的列
            @Expr(Expr.BodyType.Expr) Func1<T,TransColumn> transColumn,
            // 需要转换的列的值
            Collection<TransColumn> transColumnValues,
            // 转换后的表对象
            @Expr(Expr.BodyType.Expr) Func1<T,P> result
    ) {
        throw new NotCompiledException();
    }

    public <AggColumn,TransColumn,P extends Pivoted<TransColumn,AggColumn>> LQuery<? extends P> pivot(
            ExprTree<Func1<Aggregate<T>, AggColumn>> aggColumn,
            ExprTree<Func1<T,TransColumn>> transColumn,
            Collection<TransColumn> transColumnValues,
            ExprTree<Func1<T,P>> result
    ) {
        pivot(aggColumn.getTree(), transColumn.getTree(),transColumnValues, result.getTree());
        return new LQuery<>(getSqlBuilder());
    }

    // endregion

    // region [UnPivot(列转行)]

    public <Value,P extends UnPivoted<Value>> LQuery<? extends P> unPivot(
            // 转换后的表对象
            @Expr(Expr.BodyType.Expr) Func1<T,P> result,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn1,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn2
    ) {
        throw new NotCompiledException();
    }

    public <Value,P extends UnPivoted<Value>> LQuery<? extends P> unPivot(
            // 转换后的表对象
            ExprTree<Func1<T,P>> result,
            ExprTree<Func1<T,Value>> transColumn1,
            ExprTree<Func1<T,Value>> transColumn2
    ) {

        unPivot(result.getTree(),Arrays.asList(transColumn1.getTree(),transColumn2.getTree()));
        return new LQuery<>(getSqlBuilder());
    }

    public <Value,P extends UnPivoted<Value>> LQuery<? extends P> unPivot(
            // 转换后的表对象
            @Expr(Expr.BodyType.Expr) Func1<T,P> result,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn1,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn2,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn3
    ) {
        throw new NotCompiledException();
    }

    public <Value,P extends UnPivoted<Value>> LQuery<? extends P> unPivot(
            // 转换后的表对象
            ExprTree<Func1<T,P>> result,
            ExprTree<Func1<T,Value>> transColumn1,
            ExprTree<Func1<T,Value>> transColumn2,
            ExprTree<Func1<T,Value>> transColumn3
    ) {

        unPivot(result.getTree(),Arrays.asList(transColumn1.getTree(),transColumn2.getTree(),transColumn3.getTree()));
        return new LQuery<>(getSqlBuilder());
    }

    public <Value,P extends UnPivoted<Value>> LQuery<? extends P> unPivot(
            // 转换后的表对象
            @Expr(Expr.BodyType.Expr) Func1<T,P> result,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn1,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn2,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn3,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn4
    ) {
        throw new NotCompiledException();
    }

    public <Value,P extends UnPivoted<Value>> LQuery<? extends P> unPivot(
            // 转换后的表对象
            ExprTree<Func1<T,P>> result,
            ExprTree<Func1<T,Value>> transColumn1,
            ExprTree<Func1<T,Value>> transColumn2,
            ExprTree<Func1<T,Value>> transColumn3,
            ExprTree<Func1<T,Value>> transColumn4
    ) {

        unPivot(result.getTree(),Arrays.asList(
                transColumn1.getTree(),
                transColumn2.getTree(),
                transColumn3.getTree(),
                transColumn4.getTree()
        ));
        return new LQuery<>(getSqlBuilder());
    }

    public <Value,P extends UnPivoted<Value>> LQuery<? extends P> unPivot(
            // 转换后的表对象
            @Expr(Expr.BodyType.Expr) Func1<T,P> result,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn1,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn2,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn3,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn4,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn5
    ) {
        throw new NotCompiledException();
    }

    public <Value,P extends UnPivoted<Value>> LQuery<? extends P> unPivot(
            // 转换后的表对象
            ExprTree<Func1<T,P>> result,
            ExprTree<Func1<T,Value>> transColumn1,
            ExprTree<Func1<T,Value>> transColumn2,
            ExprTree<Func1<T,Value>> transColumn3,
            ExprTree<Func1<T,Value>> transColumn4,
            ExprTree<Func1<T,Value>> transColumn5
    ) {

        unPivot(result.getTree(),Arrays.asList(
                transColumn1.getTree(),
                transColumn2.getTree(),
                transColumn3.getTree(),
                transColumn4.getTree(),
                transColumn5.getTree()
        ));
        return new LQuery<>(getSqlBuilder());
    }

    public <Value,P extends UnPivoted<Value>> LQuery<? extends P> unPivot(
            // 转换后的表对象
            @Expr(Expr.BodyType.Expr) Func1<T,P> result,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn1,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn2,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn3,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn4,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn5,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn6
    ) {
        throw new NotCompiledException();
    }

    public <Value,P extends UnPivoted<Value>> LQuery<? extends P> unPivot(
            // 转换后的表对象
            ExprTree<Func1<T,P>> result,
            ExprTree<Func1<T,Value>> transColumn1,
            ExprTree<Func1<T,Value>> transColumn2,
            ExprTree<Func1<T,Value>> transColumn3,
            ExprTree<Func1<T,Value>> transColumn4,
            ExprTree<Func1<T,Value>> transColumn5,
            ExprTree<Func1<T,Value>> transColumn6
    ) {

        unPivot(result.getTree(),Arrays.asList(
                transColumn1.getTree(),
                transColumn2.getTree(),
                transColumn3.getTree(),
                transColumn4.getTree(),
                transColumn5.getTree(),
                transColumn6.getTree()
        ));
        return new LQuery<>(getSqlBuilder());
    }

    public <Value,P extends UnPivoted<Value>> LQuery<? extends P> unPivot(
            // 转换后的表对象
            @Expr(Expr.BodyType.Expr) Func1<T,P> result,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn1,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn2,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn3,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn4,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn5,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn6,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn7
    ) {
        throw new NotCompiledException();
    }

    public <Value,P extends UnPivoted<Value>> LQuery<? extends P> unPivot(
            // 转换后的表对象
            ExprTree<Func1<T,P>> result,
            ExprTree<Func1<T,Value>> transColumn1,
            ExprTree<Func1<T,Value>> transColumn2,
            ExprTree<Func1<T,Value>> transColumn3,
            ExprTree<Func1<T,Value>> transColumn4,
            ExprTree<Func1<T,Value>> transColumn5,
            ExprTree<Func1<T,Value>> transColumn6,
            ExprTree<Func1<T,Value>> transColumn7
    ) {

        unPivot(result.getTree(),Arrays.asList(
                transColumn1.getTree(),
                transColumn2.getTree(),
                transColumn3.getTree(),
                transColumn4.getTree(),
                transColumn5.getTree(),
                transColumn6.getTree(),
                transColumn7.getTree()
        ));
        return new LQuery<>(getSqlBuilder());
    }

    public <Value,P extends UnPivoted<Value>> LQuery<? extends P> unPivot(
            // 转换后的表对象
            @Expr(Expr.BodyType.Expr) Func1<T,P> result,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn1,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn2,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn3,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn4,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn5,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn6,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn7,
            @Expr(Expr.BodyType.Expr) Func1<T,Value> transColumn8
    ) {
        throw new NotCompiledException();
    }

    public <Value,P extends UnPivoted<Value>> LQuery<? extends P> unPivot(
            // 转换后的表对象
            ExprTree<Func1<T,P>> result,
            ExprTree<Func1<T,Value>> transColumn1,
            ExprTree<Func1<T,Value>> transColumn2,
            ExprTree<Func1<T,Value>> transColumn3,
            ExprTree<Func1<T,Value>> transColumn4,
            ExprTree<Func1<T,Value>> transColumn5,
            ExprTree<Func1<T,Value>> transColumn6,
            ExprTree<Func1<T,Value>> transColumn7,
            ExprTree<Func1<T,Value>> transColumn8
    ) {

        unPivot(result.getTree(),Arrays.asList(
                transColumn1.getTree(),
                transColumn2.getTree(),
                transColumn3.getTree(),
                transColumn4.getTree(),
                transColumn5.getTree(),
                transColumn6.getTree(),
                transColumn7.getTree(),
                transColumn8.getTree()
        ));
        return new LQuery<>(getSqlBuilder());
    }

    // endregion
}
