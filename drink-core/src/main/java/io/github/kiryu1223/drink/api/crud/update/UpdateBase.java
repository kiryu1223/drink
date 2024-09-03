package io.github.kiryu1223.drink.api.crud.update;

import io.github.kiryu1223.drink.api.crud.CRUD;
import io.github.kiryu1223.drink.core.expression.*;
import io.github.kiryu1223.drink.core.sqlBuilder.UpdateSqlBuilder;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.session.SqlSession;
import io.github.kiryu1223.drink.core.visitor.NormalVisitor;
import io.github.kiryu1223.drink.core.visitor.SetVisitor;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;
import io.github.kiryu1223.expressionTree.expressions.LambdaExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class UpdateBase extends CRUD
{
    public final static Logger log = LoggerFactory.getLogger(UpdateBase.class);

    private final UpdateSqlBuilder sqlBuilder;

    public UpdateBase(Config config,Class<?> target)
    {
        this.sqlBuilder = new UpdateSqlBuilder(config,target);
    }

    public UpdateBase(UpdateSqlBuilder sqlBuilder)
    {
        this.sqlBuilder = sqlBuilder;
    }

    protected UpdateSqlBuilder getSqlBuilder()
    {
        return sqlBuilder;
    }

    protected Config getConfig()
    {
        return sqlBuilder.getConfig();
    }

    public String toSql()
    {
        return sqlBuilder.getSql();
    }

    public long executeRows()
    {
        Config config = getConfig();
        checkHasWhere();
        List<Object> values = new ArrayList<>();
        String sql = sqlBuilder.getSqlAndValue(values);
        tryPrintUseDs(log, config.getDataSourceManager().getDsKey());
        tryPrintSql(log, sql);
        SqlSession session = config.getSqlSessionFactory().getSession();
        return session.executeUpdate(sql, values);
    }

    private void checkHasWhere()
    {
        if (getConfig().isIgnoreUpdateNoWhere()) return;
        if (!sqlBuilder.hasWhere())
        {
            throw new RuntimeException("UPDATE没有条件");
        }
    }

    protected void join(JoinType joinType, Class<?> target, ExprTree<?> expr)
    {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        SqlExpression on = normalVisitor.visit(expr.getTree());
        SqlTableExpression table = factory.table(target);
        getSqlBuilder().addJoin(target, joinType, table, on);
    }

    protected void set(LambdaExpression<?> lambda)
    {
        SetVisitor setVisitor = new SetVisitor(getConfig());
        SqlExpression expression = setVisitor.visit(lambda);
        if (expression instanceof SqlSetsExpression)
        {
            SqlSetsExpression sqlSetsExpression = (SqlSetsExpression) expression;
            sqlBuilder.addSet(sqlSetsExpression);
        }
        else if (expression instanceof SqlSetExpression)
        {
            SqlSetExpression sqlSetExpression = (SqlSetExpression) expression;
            sqlBuilder.addSet(sqlSetExpression);
        }
    }

    protected void where(LambdaExpression<?> lambda)
    {
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        SqlExpression expression = normalVisitor.visit(lambda);
        sqlBuilder.addWhere(expression);
    }
}
