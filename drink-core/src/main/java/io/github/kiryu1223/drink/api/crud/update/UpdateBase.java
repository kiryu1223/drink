package io.github.kiryu1223.drink.api.crud.update;

import io.github.kiryu1223.drink.api.crud.base.CRUD;
import io.github.kiryu1223.drink.api.crud.builder.UpdateSqlBuilder;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.session.SqlSession;
import io.github.kiryu1223.drink.core.context.JoinType;
import io.github.kiryu1223.drink.core.context.SqlContext;
import io.github.kiryu1223.drink.core.context.SqlRealTableContext;
import io.github.kiryu1223.drink.core.context.SqlTableContext;
import io.github.kiryu1223.drink.core.visitor.WhereVisitor;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

import java.util.ArrayList;
import java.util.List;

public class UpdateBase extends CRUD
{
    private final UpdateSqlBuilder sqlBuilder;

    public UpdateBase(Config config)
    {
        this.sqlBuilder = new UpdateSqlBuilder(config);
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
        System.out.println("===> " + sql);
        String key = config.getDataSourceManager().getDsKey();
        System.out.println("使用数据源: " + (key == null ? "默认" : key));
        SqlSession session = config.getSqlSessionFactory().getSession();
        return session.executeUpdate(sql, values);
    }

    private void checkHasWhere()
    {
        if (!sqlBuilder.hasWhere())
        {
            throw new RuntimeException("UPDATE没有条件");
        }
    }

    protected void join(JoinType joinType, Class<?> target, ExprTree<?> expr)
    {
        WhereVisitor whereVisitor = new WhereVisitor(getConfig());
        SqlContext onContext = whereVisitor.visit(expr.getTree());
        SqlTableContext tableContext = new SqlRealTableContext(target);
        getSqlBuilder().addJoin(target, joinType, tableContext, onContext);
    }
}
