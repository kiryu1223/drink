package io.github.kiryu1223.drink.core.api.crud.read;

import io.github.kiryu1223.drink.core.api.crud.CRUD;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.annotation.RelationType;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.metaData.IMappingTable;
import io.github.kiryu1223.drink.base.metaData.NavigateData;
import io.github.kiryu1223.drink.base.metaData.PropertyMetaData;
import io.github.kiryu1223.drink.base.toBean.Include.IncludeFactory;
import io.github.kiryu1223.drink.base.toBean.Include.IncludeSet;
import io.github.kiryu1223.drink.base.toBean.build.ObjectBuilder;
import io.github.kiryu1223.drink.base.session.SqlSession;
import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;
import io.github.kiryu1223.drink.core.visitor.GroupByVisitor;
import io.github.kiryu1223.drink.core.visitor.HavingVisitor;
import io.github.kiryu1223.drink.core.visitor.NormalVisitor;
import io.github.kiryu1223.drink.core.visitor.SelectVisitor;
import io.github.kiryu1223.drink.core.visitor.methods.LogicExpression;
import io.github.kiryu1223.drink.core.exception.DrinkException;
import io.github.kiryu1223.expressionTree.delegate.Action1;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;
import io.github.kiryu1223.expressionTree.expressions.LambdaExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.isBool;


public abstract class QueryBase extends CRUD
{
    public final static Logger log = LoggerFactory.getLogger(QueryBase.class);

    private final QuerySqlBuilder sqlBuilder;

    public QueryBase(IConfig config, Class<?> c)
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

    protected IConfig getConfig()
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
        IConfig config = getConfig();
        boolean single = sqlBuilder.isSingle();
        List<PropertyMetaData> mappingData = single ? Collections.emptyList() : sqlBuilder.getMappingData();
        List<Object> values = new ArrayList<>();

        //long start = System.nanoTime();
        String sql = sqlBuilder.getSqlAndValue(values);
        //System.out.println("本次toSql耗时" + (System.nanoTime() - start));

        tryPrintUseDs(log, config.getDataSourceManager().getDsKey());
        tryPrintSql(log, sql);
        Class<T> targetClass = (Class<T>) sqlBuilder.getTargetClass();
        SqlSession session = config.getSqlSessionFactory().getSession();

        //long start2 = System.nanoTime();
        List<T> ts = session.executeQuery(
                r -> ObjectBuilder.start(r, targetClass, mappingData, single, config).createList(),
                sql,
                values
        );
        //System.out.println("本次toBean耗时" + (System.nanoTime() - start2));

        if (!sqlBuilder.getIncludeSets().isEmpty())
        {
            try
            {
                IncludeFactory includeFactory = config.getIncludeFactory();
                includeFactory.getBuilder(getConfig(), session, targetClass, ts, sqlBuilder.getIncludeSets(), sqlBuilder.getQueryable()).include();
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
        ISqlQueryableExpression queryableCopy = getSqlBuilder().getQueryable().copy(getConfig());
        QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(getConfig(), queryableCopy);
        querySqlBuilder.getIncludeSets().addAll(getSqlBuilder().getIncludeSets());
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
        ISqlExpression expression = selectVisitor.visit(lambda);
        ISqlSelectExpression selectExpression;
        if (expression instanceof ISqlSelectExpression)
        {
            selectExpression = (ISqlSelectExpression) expression;
        }
        else
        {
            SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
            // 用于包装某些数据库不支持直接返回bool
            if (isBool(lambda.getReturnType()))
            {
                IConfig config = getConfig();
                switch (config.getDbType())
                {
                    case SQLServer:
                    case Oracle:
                        expression = LogicExpression.IfExpression(config, expression, factory.constString("1"), factory.constString("0"));
                }
            }
            selectExpression = factory.select(Collections.singletonList(expression), lambda.getReturnType(), true,false);
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
        ISqlExpression on = normalVisitor.visit(expr.getTree());
        sqlBuilder.addJoin(joinType, factory.table(target), on);
    }

    protected void join(JoinType joinType, QueryBase target, ExprTree<?> expr)
    {
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        ISqlExpression on = normalVisitor.visit(expr.getTree());
        sqlBuilder.addJoin(joinType, target.getSqlBuilder().getQueryable(), on);
    }

    protected void where(LambdaExpression<?> lambda)
    {
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        ISqlExpression where = normalVisitor.visit(lambda);
        sqlBuilder.addWhere(where);
    }

    protected void orWhere(LambdaExpression<?> lambda)
    {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        ISqlExpression where = normalVisitor.visit(lambda);
        sqlBuilder.addOrWhere(where);
    }

    protected void exists(Class<?> table, LambdaExpression<?> lambda, boolean not)
    {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        ISqlExpression where = normalVisitor.visit(lambda);
        QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(getConfig(), table, sqlBuilder.getQueryable().getOrderedCount());
        querySqlBuilder.setSelect(factory.select(Collections.singletonList(factory.constString("1")), table, true,false));
        querySqlBuilder.addWhere(where);
        ISqlUnaryExpression exists = factory.unary(SqlOperator.EXISTS, querySqlBuilder.getQueryable());
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
        ISqlExpression where = normalVisitor.visit(lambda);
        ISqlQueryableExpression queryable = queryBase.getSqlBuilder().getQueryable();
        QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(getConfig(), queryable, sqlBuilder.getQueryable().getOrderedCount());
        querySqlBuilder.setSelect(factory.select(Collections.singletonList(factory.constString("1")), queryable.getTableClass(), true,false));
        querySqlBuilder.addWhere(where);
        ISqlUnaryExpression exists = factory.unary(SqlOperator.EXISTS, querySqlBuilder.getQueryable());
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
        ISqlExpression expression = groupByVisitor.visit(lambda);
        ISqlGroupByExpression group;
        if (expression instanceof ISqlGroupByExpression)
        {
            group = (ISqlGroupByExpression) expression;
        }
        else
        {
            SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
            LinkedHashMap<String, ISqlExpression> map = new LinkedHashMap<>();
            map.put("key", expression);
            group = factory.groupBy(map);
        }
        sqlBuilder.setGroup(group);
    }

    protected void having(LambdaExpression<?> lambda)
    {
        HavingVisitor havingVisitor = new HavingVisitor(getConfig(), sqlBuilder.getQueryable());
        ISqlExpression expression = havingVisitor.visit(lambda);
        sqlBuilder.addHaving(expression);
    }

    protected void orderBy(LambdaExpression<?> lambda, boolean asc)
    {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        HavingVisitor havingVisitor = new HavingVisitor(getConfig(), sqlBuilder.getQueryable());
        ISqlExpression expression = havingVisitor.visit(lambda);
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
        ISqlExpression expression = normalVisitor.visit(lambda);
        if (expression instanceof ISqlColumnExpression)
        {
            ISqlColumnExpression columnExpression = (ISqlColumnExpression) expression;
            if (!columnExpression.getPropertyMetaData().hasNavigate())
            {
                throw new RuntimeException("include指定的字段需要被@Navigate修饰");
            }
            relationTypeCheck(columnExpression.getPropertyMetaData().getNavigateData());
            IncludeSet includeSet;
            if (cond != null)
            {
                NormalVisitor normalVisitor2 = new NormalVisitor(getConfig());
                ISqlExpression condition = normalVisitor2.visit(cond);
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
        ISqlExpression expression = normalVisitor.visit(lambda);
        if (expression instanceof ISqlColumnExpression)
        {
            ISqlColumnExpression columnExpression = (ISqlColumnExpression) expression;
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
                    throw new DrinkException(relationType + "不支持集合");
                }
                break;
            case OneToMany:
                if (!navigateData.isCollectionWrapper())
                {
                    if (!(List.class.isAssignableFrom(navigateData.getCollectionWrapperType()) || Set.class.isAssignableFrom(navigateData.getCollectionWrapperType())))
                    {
                        throw new DrinkException(relationType + "只支持List和Set");
                    }
                    throw new DrinkException(relationType + "只支持集合");
                }
                break;
            case ManyToMany:
                if (navigateData.getMappingTableType() == IMappingTable.class
                        || navigateData.getSelfMappingPropertyName().isEmpty()
                        || navigateData.getTargetMappingPropertyName().isEmpty())
                {
                    throw new DrinkException(relationType + "下@Navigate注解的midTable和SelfMapping和TargetMapping字段都不能为空");
                }
                if (!navigateData.isCollectionWrapper())
                {
                    if (!(List.class.isAssignableFrom(navigateData.getCollectionWrapperType()) || Set.class.isAssignableFrom(navigateData.getCollectionWrapperType())))
                    {
                        throw new DrinkException(relationType + "只支持List和Set");
                    }
                    throw new RuntimeException(relationType + "只支持集合");
                }
                break;
        }
    }
}
