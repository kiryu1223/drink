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
package io.github.kiryu1223.drink.core.api.crud.delete;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.JoinType;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.session.SqlSession;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.core.api.crud.CRUD;
import io.github.kiryu1223.drink.core.sqlBuilder.DeleteSqlBuilder;
import io.github.kiryu1223.drink.core.visitor.SqlVisitor;
import io.github.kiryu1223.expressionTree.expressions.LambdaExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kiryu1223
 * @since 3.0
 */
public abstract class DeleteBase extends CRUD {
    public final static Logger log = LoggerFactory.getLogger(DeleteBase.class);

    private final DeleteSqlBuilder sqlBuilder;

    public DeleteBase(DeleteSqlBuilder sqlBuilder) {
        this.sqlBuilder = sqlBuilder;
    }

    protected DeleteSqlBuilder getSqlBuilder() {
        return sqlBuilder;
    }

    public IConfig getConfig() {
        return sqlBuilder.getConfig();
    }

    /**
     * 执行sql语句
     *
     * @return 执行后的结果
     */
    public long executeRows() {
        checkHasWhere();
        IConfig config = getConfig();
        List<SqlValue> sqlValues = new ArrayList<>();
        String sql = sqlBuilder.getSqlAndValue(sqlValues);
        //tryPrintUseDs(log,config.getDataSourceManager().getDsKey());
        tryPrintSql(log, sql);
        SqlSession session = config.getSqlSessionFactory().getSession(config);
        return session.executeDelete(sql, sqlValues);
    }

    public String toSql() {
        return sqlBuilder.getSql();
    }

    private void checkHasWhere() {
        if (getConfig().isIgnoreDeleteNoWhere()) return;
        if (!sqlBuilder.hasWhere()) {
            throw new RuntimeException("DELETE没有条件");
        }
    }

    protected void join(JoinType joinType, Class<?> target, LambdaExpression<?> lambda) {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), sqlBuilder.getFrom(), sqlBuilder.getJoins());
        ISqlExpression on = sqlVisitor.visit(lambda);
        getSqlBuilder().addJoin(joinType, factory.table(target), on);
    }

    protected void selectDeleteTable(Class<?> c) {
        //getSqlBuilder().addExclude(c);
    }

    protected void where(LambdaExpression<?> lambda) {
        SqlVisitor sqlVisitor = new SqlVisitor(getConfig(), sqlBuilder.getFrom(), sqlBuilder.getJoins());
        ISqlExpression expression = sqlVisitor.visit(lambda);
        sqlBuilder.addWhere(expression);
    }
}
