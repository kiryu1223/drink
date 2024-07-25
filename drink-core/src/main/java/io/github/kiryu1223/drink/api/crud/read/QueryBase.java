package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.annotation.Navigate;
import io.github.kiryu1223.drink.api.crud.CRUD;
import io.github.kiryu1223.drink.core.builder.NavigateBuilder;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.builder.ObjectBuilder;
import io.github.kiryu1223.drink.core.context.*;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;
import io.github.kiryu1223.drink.core.session.SqlSession;
import io.github.kiryu1223.drink.core.visitor.*;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;
import io.github.kiryu1223.expressionTree.expressions.LambdaExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public abstract class QueryBase extends CRUD
{
    public final static Logger log = LoggerFactory.getLogger(QueryBase.class);

    private final QuerySqlBuilder sqlBuilder;

    public QueryBase(Config config, Class<?> c)
    {
        sqlBuilder = new QuerySqlBuilder(config, new SqlRealTableContext(c));
    }

    public QueryBase(QuerySqlBuilder sqlBuilder)
    {
        this.sqlBuilder = sqlBuilder;
    }

    protected QuerySqlBuilder getSqlBuilder()
    {
        return sqlBuilder;
    }

    protected Config getConfig()
    {
        return sqlBuilder.getConfig();
    }

    protected boolean any()
    {
        SqlSession session = getConfig().getSqlSessionFactory().getSession();
        List<Object> values = new ArrayList<>();
        String sql = sqlBuilder.getSqlAndValue(values);
        return session.executeQuery(f -> f.next(), "SELECT 1 FROM (" + sql + ") LIMIT 1", values);
    }

    protected <T> List<T> toList()
    {
        Config config = getConfig();
        List<PropertyMetaData> mappingData = sqlBuilder.getMappingData();
        List<Object> values = new ArrayList<>();
        String sql = sqlBuilder.getSqlAndValue(values);
        tryPrintUseDs(log, config.getDataSourceManager().getDsKey());
        tryPrintSql(log, sql);
        Class<T> targetClass = (Class<T>) sqlBuilder.getTargetClass();
        SqlSession session = config.getSqlSessionFactory().getSession();
        List<T> ts = session.executeQuery(
                r -> ObjectBuilder.start(r, targetClass, mappingData, false).createList(),
                sql,
                values
        );
        if (!includes.isEmpty())
        {
            try
            {
                NavigateBuilder<T> navigateBuilder = new NavigateBuilder<>(getConfig(),targetClass, ts, includes);
                navigateBuilder.build();
            } catch (InvocationTargetException | IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        }
        return ts;
    }

    protected void distinct0(boolean condition)
    {
        sqlBuilder.setDistinct(condition);
    }

    protected <R> EndQuery<R> select(Class<R> r)
    {
        select0(r);
        return new EndQuery<>(boxedQuerySqlBuilder());
    }

    protected boolean select(LambdaExpression<?> lambda)
    {
        SelectVisitor selectVisitor = new SelectVisitor(getSqlBuilder().getGroupBy(), getConfig());
        SqlContext context = selectVisitor.visit(lambda);
        getSqlBuilder().setSelect(context, lambda.getReturnType());
        return !(context instanceof SqlSelectorContext);
    }

    protected void select0(Class<?> c)
    {
        MetaData metaData = MetaDataCache.getMetaData(c);
        List<SqlContext> propertyContexts = new ArrayList<>();
        for (PropertyMetaData sel : metaData.getNotIgnoreColumns())
        {
            int index = 0;
            lable:
            for (MetaData data : MetaDataCache.getMetaData(sqlBuilder.getOrderedClass()))
            {
                for (PropertyMetaData noi : data.getNotIgnoreColumns())
                {
                    if (noi.getColumn().equals(sel.getColumn()))
                    {
                        propertyContexts.add(new SqlPropertyContext(sel, index));
                        break lable;
                    }
                }
                index++;
            }
        }
        getSqlBuilder().setSelect(new SqlSelectorContext(propertyContexts), c);
    }

    protected <Tn> QueryBase joinNewQuery()
    {
        return null;
    }

    protected void join(JoinType joinType, Class<?> target, ExprTree<?> expr)
    {
        WhereVisitor whereVisitor = new WhereVisitor(getConfig());
        SqlContext onContext = whereVisitor.visit(expr.getTree());
        SqlTableContext tableContext = new SqlRealTableContext(target);
        getSqlBuilder().addJoin(target, joinType, tableContext, onContext);
    }

    protected void join(JoinType joinType, QueryBase target, ExprTree<?> expr)
    {
        WhereVisitor whereVisitor = new WhereVisitor(getConfig());
        SqlContext onContext = whereVisitor.visit(expr.getTree());
        SqlTableContext tableContext = new SqlVirtualTableContext(target.getSqlBuilder());
        getSqlBuilder().addJoin(target.getSqlBuilder().getTargetClass(), joinType, tableContext, onContext);
    }

    protected void where(LambdaExpression<?> lambda)
    {
        WhereVisitor whereVisitor = new WhereVisitor(getConfig());
        SqlContext where = whereVisitor.visit(lambda);
        getSqlBuilder().addWhere(where);
    }

    protected void orWhere(LambdaExpression<?> lambda)
    {
        WhereVisitor whereVisitor = new WhereVisitor(getConfig());
        SqlContext right = whereVisitor.visit(lambda);
        getSqlBuilder().addOrWhere(right);
    }

    protected void exists(Class<?> table, LambdaExpression<?> lambda, boolean not)
    {
        WhereVisitor whereVisitor = new WhereVisitor(getConfig());
        SqlContext where = whereVisitor.visit(lambda);
        QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(getConfig(), new SqlRealTableContext(table), sqlBuilder.getOrderedClass().size());
        querySqlBuilder.setSelect(new SqlConstString("1"), table);
        querySqlBuilder.addWhere(where);
        SqlUnaryContext exists = new SqlUnaryContext(SqlOperator.EXISTS, new SqlParensContext(new SqlVirtualTableContext(querySqlBuilder)));
        if (not)
        {
            getSqlBuilder().addWhere(new SqlUnaryContext(SqlOperator.NOT, exists));
        }
        else
        {
            getSqlBuilder().addWhere(exists);
        }
    }

    protected void exists(QueryBase queryBase, LambdaExpression<?> lambda, boolean not)
    {
        WhereVisitor whereVisitor = new WhereVisitor(getConfig());
        SqlContext where = whereVisitor.visit(lambda);
        QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(getConfig(), new SqlVirtualTableContext(queryBase.getSqlBuilder()), sqlBuilder.getOrderedClass().size());
        querySqlBuilder.setSelect(new SqlConstString("1"), queryBase.getSqlBuilder().getTargetClass());
        querySqlBuilder.addWhere(where);
        SqlUnaryContext exists = new SqlUnaryContext(SqlOperator.EXISTS, new SqlParensContext(new SqlVirtualTableContext(querySqlBuilder)));
        if (not)
        {
            getSqlBuilder().addWhere(new SqlUnaryContext(SqlOperator.NOT, exists));
        }
        else
        {
            getSqlBuilder().addWhere(exists);
        }
    }

    protected void groupBy(LambdaExpression<?> lambda)
    {
        GroupByVisitor groupByVisitor = new GroupByVisitor(getConfig());
        SqlContext context = groupByVisitor.visit(lambda);
        getSqlBuilder().setGroupBy(context, lambda.getReturnType());
    }

    protected void having(LambdaExpression<?> lambda)
    {
        HavingVisitor havingVisitor = new HavingVisitor(getSqlBuilder().getGroupBy(), getConfig());
        SqlContext context = havingVisitor.visit(lambda);
        getSqlBuilder().addHaving(context);
    }

    protected void orderBy(LambdaExpression<?> lambda, boolean asc)
    {
        HavingVisitor havingVisitor = new HavingVisitor(getSqlBuilder().getGroupBy(), getConfig());
        SqlContext context = havingVisitor.visit(lambda);
        getSqlBuilder().addOrderBy(new SqlOrderContext(asc, context));
    }

    protected void limit0(long rows)
    {
        getSqlBuilder().setLimit(new SqlLimitContext(rows));
    }

    protected void limit0(long offset, long rows)
    {
        getSqlBuilder().setLimit(new SqlLimitContext(offset, rows));
    }

    protected void singleCheck(boolean single)
    {
        if (single)
        {
            throw new RuntimeException("query.select(Func<T1,T2..., R> expr) 不允许传入单个元素, 单元素请使用endSelect");
        }
    }

    public String toSql()
    {
        return sqlBuilder.getSql();
    }

    protected QuerySqlBuilder boxedQuerySqlBuilder()
    {
        return new QuerySqlBuilder(getConfig(), new SqlVirtualTableContext(sqlBuilder));
    }

    private final List<SqlPropertyContext> includes = new ArrayList<>();

    protected void include(LambdaExpression<?> lambda)
    {
        IncludeVisitor includeVisitor = new IncludeVisitor(getConfig());
        SqlContext context = includeVisitor.visit(lambda);
        if (context instanceof SqlPropertyContext)
        {
            SqlPropertyContext propertyContext = (SqlPropertyContext) context;
            if (!propertyContext.getPropertyMetaData().HasNavigate())
            {
                throw new RuntimeException("include指定的字段需要被@Navigate修饰");
            }
            includes.add(propertyContext);
            return;
        }
        throw new RuntimeException("include需要指定一个字段");
    }
}
