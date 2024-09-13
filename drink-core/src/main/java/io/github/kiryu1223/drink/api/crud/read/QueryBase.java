package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.annotation.RelationType;
import io.github.kiryu1223.drink.api.crud.CRUD;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.builder.IncludeBuilder;
import io.github.kiryu1223.drink.core.builder.IncludeSet;
import io.github.kiryu1223.drink.core.builder.ObjectBuilder;
import io.github.kiryu1223.drink.core.expression.*;
import io.github.kiryu1223.drink.core.metaData.NavigateData;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;
import io.github.kiryu1223.drink.core.session.SqlSession;
import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;
import io.github.kiryu1223.drink.core.visitor.GroupByVisitor;
import io.github.kiryu1223.drink.core.visitor.HavingVisitor;
import io.github.kiryu1223.drink.core.visitor.NormalVisitor;
import io.github.kiryu1223.drink.core.visitor.SelectVisitor;
import io.github.kiryu1223.drink.ext.IMappingTable;
import io.github.kiryu1223.expressionTree.delegate.Action1;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;
import io.github.kiryu1223.expressionTree.expressions.LambdaExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;


public abstract class QueryBase extends CRUD
{
    public final static Logger log = LoggerFactory.getLogger(QueryBase.class);

    private final QuerySqlBuilder sqlBuilder;

    public QueryBase(Config config, Class<?> c)
    {
        this.sqlBuilder = new QuerySqlBuilder(config, c);
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

    // 脑子瓦塔了，toMap不用这么麻烦
//    protected <K,T> Map<K,T> toMap(LambdaExpression<?> lambda)
//    {
//        // getMapKey
//        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
//        SqlContext context = normalVisitor.visit(lambda);
//        if (!(context instanceof SqlPropertyContext)) throw new RuntimeException("toMap需要一个对象拥有的字段为key");
//        SqlPropertyContext propertyContext = (SqlPropertyContext) context;
//        String column=propertyContext.getProperty();
//
//        Config config = getConfig();
//        AtomicBoolean atomicBoolean=new AtomicBoolean();
//        List<PropertyMetaData> mappingData = sqlBuilder.getMappingData(atomicBoolean);
//        List<Object> values = new ArrayList<>();
//        String sql = sqlBuilder.getSqlAndValue(values);
//        tryPrintUseDs(log, config.getDataSourceManager().getDsKey());
//        tryPrintSql(log, sql);
//        Class<T> targetClass = (Class<T>) sqlBuilder.getTargetClass();
//        SqlSession session = config.getSqlSessionFactory().getSession();
//        Map<K, T> map = session.executeQuery(
//                r -> ObjectBuilder.start(r, targetClass, mappingData, atomicBoolean.get()).createMap(column),
//                sql,
//                values
//        );
//        if (!includes.isEmpty())
//        {
//            try
//            {
//                IncludeBuilder<T> navigateBuilder = new IncludeBuilder<>(getConfig(),targetClass, map.values(), includes);
//                navigateBuilder.include();
//            } catch (InvocationTargetException | IllegalAccessException e)
//            {
//                throw new RuntimeException(e);
//            }
//        }
//        return map;
//    }


    protected <T> List<T> toList()
    {
        Config config = getConfig();
        boolean single = sqlBuilder.isSingle();
        List<PropertyMetaData> mappingData = single ? Collections.emptyList() : sqlBuilder.getMappingData();
        List<Object> values = new ArrayList<>();
        String sql = sqlBuilder.getSqlAndValue(values);
        tryPrintUseDs(log, config.getDataSourceManager().getDsKey());
        tryPrintSql(log, sql);
        Class<T> targetClass = (Class<T>) sqlBuilder.getTargetClass();
        SqlSession session = config.getSqlSessionFactory().getSession();
        List<T> ts = session.executeQuery(
                r -> ObjectBuilder.start(r, targetClass, mappingData, single).createList(),
                sql,
                values
        );
        if (!sqlBuilder.getIncludeSets().isEmpty())
        {
            try
            {
                IncludeBuilder<T> includeBuilder = new IncludeBuilder<>(getConfig(), session, targetClass, ts, sqlBuilder.getIncludeSets(), sqlBuilder.getQueryable());
                includeBuilder.include();
            }
            catch (InvocationTargetException | IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        }
        return ts;
    }

    protected <T> T first()
    {
        SqlQueryableExpression queryableCopy = getSqlBuilder().getQueryable().copy(getConfig());
        QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(getConfig(), queryableCopy);
        LQuery<T> lQuery = new LQuery<>(querySqlBuilder);
        lQuery.limit(1);
        List<T> list = lQuery.toList();
        return list.isEmpty() ? null : list.get(0);
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
        SelectVisitor selectVisitor = new SelectVisitor(getConfig(), sqlBuilder.getQueryable());
        SqlExpression expression = selectVisitor.visit(lambda);
        SqlSelectExpression selectExpression;
        if (expression instanceof SqlSelectExpression)
        {
            selectExpression = (SqlSelectExpression) expression;
        }
        else
        {
            SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
            selectExpression = factory.select(Collections.singletonList(expression), lambda.getReturnType(), true);
        }
        sqlBuilder.setSelect(selectExpression);
        return sqlBuilder.isSingle();
    }

    protected void select0(Class<?> c)
    {
        sqlBuilder.setSelect(c);
    }

    protected <Tn> QueryBase joinNewQuery()
    {
        return null;
    }

    protected void join(JoinType joinType, Class<?> target, ExprTree<?> expr)
    {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        SqlExpression on = normalVisitor.visit(expr.getTree());
        sqlBuilder.addJoin(joinType, factory.table(target), on);
    }

    protected void join(JoinType joinType, QueryBase target, ExprTree<?> expr)
    {
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        SqlExpression on = normalVisitor.visit(expr.getTree());
        sqlBuilder.addJoin(joinType, target.getSqlBuilder().getQueryable(), on);
    }

    protected void where(LambdaExpression<?> lambda)
    {
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        SqlExpression where = normalVisitor.visit(lambda);
        sqlBuilder.addWhere(where);
    }

    protected void orWhere(LambdaExpression<?> lambda)
    {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        SqlExpression where = normalVisitor.visit(lambda);
        sqlBuilder.addOrWhere(where);
    }

    protected void exists(Class<?> table, LambdaExpression<?> lambda, boolean not)
    {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        SqlExpression where = normalVisitor.visit(lambda);
        QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(getConfig(), table, sqlBuilder.getQueryable().getOrderedCount());
        querySqlBuilder.setSelect(factory.select(Collections.singletonList(factory.constString("1")), table, true));
        querySqlBuilder.addWhere(where);
        SqlUnaryExpression exists = factory.unary(SqlOperator.EXISTS, querySqlBuilder.getQueryable());
        if (not)
        {
            sqlBuilder.addWhere(factory.unary(SqlOperator.NOT, exists));
        }
        else
        {
            sqlBuilder.addWhere(exists);
        }
    }

    protected void exists(QueryBase queryBase, LambdaExpression<?> lambda, boolean not)
    {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        SqlExpression where = normalVisitor.visit(lambda);
        SqlQueryableExpression queryable = queryBase.getSqlBuilder().getQueryable();
        QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(getConfig(), queryable, sqlBuilder.getQueryable().getOrderedCount());
        querySqlBuilder.setSelect(factory.select(Collections.singletonList(factory.constString("1")), queryable.getTableClass(), true));
        querySqlBuilder.addWhere(where);
        SqlUnaryExpression exists = factory.unary(SqlOperator.EXISTS, querySqlBuilder.getQueryable());
        if (not)
        {
            sqlBuilder.addWhere(factory.unary(SqlOperator.NOT, exists));
        }
        else
        {
            sqlBuilder.addWhere(exists);
        }
    }

    protected void groupBy(LambdaExpression<?> lambda)
    {
        GroupByVisitor groupByVisitor = new GroupByVisitor(getConfig());
        SqlExpression expression = groupByVisitor.visit(lambda);
        SqlGroupByExpression group;
        if (expression instanceof SqlGroupByExpression)
        {
            group = (SqlGroupByExpression) expression;
        }
        else
        {
            SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
            LinkedHashMap<String, SqlExpression> map = new LinkedHashMap<>();
            map.put("key", expression);
            group = factory.groupBy(map);
        }
        sqlBuilder.setGroup(group);
    }

    protected void having(LambdaExpression<?> lambda)
    {
        HavingVisitor havingVisitor = new HavingVisitor(getConfig(), sqlBuilder.getQueryable());
        SqlExpression expression = havingVisitor.visit(lambda);
        sqlBuilder.addHaving(expression);
    }

    protected void orderBy(LambdaExpression<?> lambda, boolean asc)
    {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        HavingVisitor havingVisitor = new HavingVisitor(getConfig(), sqlBuilder.getQueryable());
        SqlExpression expression = havingVisitor.visit(lambda);
        sqlBuilder.addOrder(factory.order(expression, asc));
    }

    protected void limit0(long rows)
    {
        limit0(0, rows);
    }

    protected void limit0(long offset, long rows)
    {
        sqlBuilder.setLimit(offset, rows);
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
        return new QuerySqlBuilder(getConfig(), sqlBuilder.getQueryable());
    }

    protected void include(LambdaExpression<?> lambda, LambdaExpression<?> cond, List<IncludeSet> includeSets)
    {
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        SqlExpression expression = normalVisitor.visit(lambda);
        if (expression instanceof SqlColumnExpression)
        {
            SqlColumnExpression columnExpression = (SqlColumnExpression) expression;
            if (!columnExpression.getPropertyMetaData().hasNavigate())
            {
                throw new RuntimeException("include指定的字段需要被@Navigate修饰");
            }
            relationTypeCheck(columnExpression.getPropertyMetaData().getNavigateData());
            IncludeSet includeSet;
            if (cond != null)
            {
                NormalVisitor normalVisitor2 = new NormalVisitor(getConfig());
                SqlExpression condition = normalVisitor2.visit(cond);
                includeSet = new IncludeSet(columnExpression, condition);
            }
            else
            {
                includeSet = new IncludeSet(columnExpression);
            }
            includeSets.add(includeSet);
        }
        else
        {
            throw new RuntimeException("include需要指定一个字段");
        }
    }

    protected void include(LambdaExpression<?> lambda, LambdaExpression<?> cond)
    {
        include(lambda, cond, sqlBuilder.getIncludeSets());
    }

    protected void include(LambdaExpression<?> lambda)
    {
        include(lambda, null, sqlBuilder.getIncludeSets());
    }

    protected <R> void includeByCond(LambdaExpression<?> lambda, Action1<IncludeCond<R>> action, List<IncludeSet> includeSets)
    {
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        SqlExpression expression = normalVisitor.visit(lambda);
        if (expression instanceof SqlColumnExpression)
        {
            SqlColumnExpression columnExpression = (SqlColumnExpression) expression;
            PropertyMetaData propertyMetaData = columnExpression.getPropertyMetaData();
            if (!columnExpression.getPropertyMetaData().hasNavigate())
            {
                throw new RuntimeException("include指定的字段需要被@Navigate修饰");
            }
            relationTypeCheck(columnExpression.getPropertyMetaData().getNavigateData());
            Class<R> navigateTargetType = (Class<R>) propertyMetaData.getNavigateData().getNavigateTargetType();
            IncludeCond<R> temp = new IncludeCond<>(getConfig(), navigateTargetType);
            action.invoke(temp);
            IncludeSet includeSet = new IncludeSet(columnExpression, temp.getSqlBuilder().getQueryable());
            includeSets.add(includeSet);
        }
        else
        {
            throw new RuntimeException("include需要指定一个字段");
        }
    }

    protected <R> void includeByCond(LambdaExpression<?> lambda, Action1<IncludeCond<R>> action)
    {
        includeByCond(lambda, action, sqlBuilder.getIncludeSets());
    }

    protected void relationTypeCheck(NavigateData navigateData)
    {
        RelationType relationType = navigateData.getRelationType();
        switch (relationType)
        {
            case OneToOne:
            case ManyToOne:
                if (navigateData.isCollectionWrapper())
                {
                    throw new RuntimeException(relationType + "不支持集合");
                }
                break;
            case OneToMany:
                if (!navigateData.isCollectionWrapper())
                {
                    throw new RuntimeException(relationType + "只支持集合");
                }
                break;
            case ManyToMany:
                if (!navigateData.isCollectionWrapper())
                {
                    throw new RuntimeException(relationType + "只支持集合");
                }
                if (navigateData.getMappingTableType() == IMappingTable.class
                        || navigateData.getSelfMappingPropertyName().isEmpty()
                        || navigateData.getTargetMappingPropertyName().isEmpty())
                {
                    throw new RuntimeException(relationType + "下@Navigate注解的midTable和SelfMapping和TargetMapping字段都不能为空");
                }
                break;
        }
    }
}
