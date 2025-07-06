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

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.toBean.executor.JdbcExecutor;
import io.github.kiryu1223.drink.core.api.crud.CRUD;
import io.github.kiryu1223.drink.core.exception.SqLinkException;
import io.github.kiryu1223.drink.core.sqlBuilder.UpdateSqlBuilder;
import io.github.kiryu1223.drink.core.visitor.UpdateSqlVisitor;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;
import io.github.kiryu1223.expressionTree.expressions.LambdaExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author kiryu1223
 * @since 3.0
 */
public class UpdateBase<C> extends CRUD<C> {
    public final static Logger log = LoggerFactory.getLogger(UpdateBase.class);

    private final UpdateSqlBuilder sqlBuilder;

    public UpdateBase(UpdateSqlBuilder sqlBuilder) {
        this.sqlBuilder = sqlBuilder;
    }

    protected UpdateSqlBuilder getSqlBuilder() {
        return sqlBuilder;
    }

    protected IConfig getConfig() {
        return sqlBuilder.getConfig();
    }

    public String toSql() {
        return sqlBuilder.getSql();
    }

    /**
     * 执行sql语句
     *
     * @return 执行后的结果
     */
    public long executeRows() {
        if (!sqlBuilder.hasSet()) {
            return 0;
        }
        checkHasWhere();
        IConfig config = getConfig();
        List<SqlValue> sqlValues = new ArrayList<>();
        String sql = sqlBuilder.getSqlAndValue(sqlValues);
        try
        {
            return JdbcExecutor.executeUpdate(config,sql,sqlValues);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void checkHasWhere() {
        if (getConfig().isIgnoreUpdateNoWhere()) return;
        if (!sqlBuilder.hasWhere()) {
            throw new SqLinkException("UPDATE没有条件");
        }
    }

    protected void join(JoinType joinType, Class<?> target, ExprTree<?> expr) {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        UpdateSqlVisitor sqlVisitor = new UpdateSqlVisitor(getConfig(), sqlBuilder.getUpdate());
        ISqlExpression on = sqlVisitor.visit(expr.getTree());
        ISqlTableExpression table = factory.table(target);
        sqlBuilder.addJoin(joinType, table, factory.condition(Collections.singletonList(on)));
    }

    protected void set(LambdaExpression<?> left, Object value) {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        UpdateSqlVisitor sqlVisitor = new UpdateSqlVisitor(getConfig(), sqlBuilder.getUpdate());
        ISqlColumnExpression column = sqlVisitor.toColumn(left);
        sqlBuilder.addSet(factory.set(column, factory.AnyValue(value)));
    }

    protected void set(LambdaExpression<?> left, LambdaExpression<?> right) {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        UpdateSqlVisitor sqlVisitor = new UpdateSqlVisitor(getConfig(), sqlBuilder.getUpdate());
        ISqlColumnExpression column = sqlVisitor.toColumn(left);
        ISqlExpression value = sqlVisitor.visit(right);
        sqlBuilder.addSet(factory.set(column, value));
    }

    protected void where(LambdaExpression<?> lambda) {
        UpdateSqlVisitor sqlVisitor = new UpdateSqlVisitor(getConfig(), sqlBuilder.getUpdate());
        ISqlExpression expression = sqlVisitor.visit(lambda);
        sqlBuilder.addAndOrWhere(expression,true);
    }

    protected void orWhere(LambdaExpression<?> lambda) {
        UpdateSqlVisitor sqlVisitor = new UpdateSqlVisitor(getConfig(), sqlBuilder.getUpdate());
        ISqlExpression expression = sqlVisitor.visit(lambda);
        sqlBuilder.addAndOrWhere(expression,false);
    }
}
