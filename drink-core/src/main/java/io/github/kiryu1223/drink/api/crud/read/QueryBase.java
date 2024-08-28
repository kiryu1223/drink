package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.annotation.RelationType;
import io.github.kiryu1223.drink.api.crud.CRUD;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.builder.IncludeBuilder;
import io.github.kiryu1223.drink.core.builder.IncludeSet;
import io.github.kiryu1223.drink.core.builder.ObjectBuilder;
import io.github.kiryu1223.drink.core.context.*;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
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
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        List<PropertyMetaData> mappingData = sqlBuilder.getMappingData(atomicBoolean);
        List<Object> values = new ArrayList<>();
        String sql = sqlBuilder.getSqlAndValue(values);
        tryPrintUseDs(log, config.getDataSourceManager().getDsKey());
        tryPrintSql(log, sql);
        Class<T> targetClass = (Class<T>) sqlBuilder.getTargetClass();
        SqlSession session = config.getSqlSessionFactory().getSession();
        List<T> ts = session.executeQuery(
                r -> ObjectBuilder.start(r, targetClass, mappingData, atomicBoolean.get()).createList(),
                sql,
                values
        );
        if (!sqlBuilder.getIncludes().isEmpty())
        {
            try
            {
                IncludeBuilder<T> includeBuilder = new IncludeBuilder<>(getConfig(), targetClass, ts, sqlBuilder.getIncludes(), sqlBuilder);
                includeBuilder.include();
            }
            catch (InvocationTargetException | IllegalAccessException e)
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
        boolean isSingle = !(context instanceof SqlSelectorContext);
        if (isSingle)
        {
            getSqlBuilder().setSelect(context);
        }
        else
        {
            getSqlBuilder().setSelect(context, lambda.getReturnType());
        }
        return isSingle;
    }

    protected void select0(Class<?> c)
    {
        MetaData metaData = MetaDataCache.getMetaData(c);
        List<SqlContext> propertyContexts = new ArrayList<>();
        for (PropertyMetaData sel : metaData.getNotIgnorePropertys())
        {
            int index = 0;
            lable:
            for (MetaData data : MetaDataCache.getMetaData(sqlBuilder.getOrderedClass()))
            {
                for (PropertyMetaData noi : data.getNotIgnorePropertys())
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
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        SqlContext onContext = normalVisitor.visit(expr.getTree());
        SqlTableContext tableContext = new SqlRealTableContext(target);
        getSqlBuilder().addJoin(joinType, tableContext, onContext);
    }

    protected void join(JoinType joinType, QueryBase target, ExprTree<?> expr)
    {
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        SqlContext onContext = normalVisitor.visit(expr.getTree());
        SqlTableContext tableContext = new SqlVirtualTableContext(target.getSqlBuilder());
        getSqlBuilder().addJoin(joinType, tableContext, onContext);
    }

    protected void where(LambdaExpression<?> lambda)
    {
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        SqlContext where = normalVisitor.visit(lambda);
        getSqlBuilder().addWhere(where);
    }

    protected void orWhere(LambdaExpression<?> lambda)
    {
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        SqlContext right = normalVisitor.visit(lambda);
        getSqlBuilder().addOrWhere(right);
    }

    protected void exists(Class<?> table, LambdaExpression<?> lambda, boolean not)
    {
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        SqlContext where = normalVisitor.visit(lambda);
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
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        SqlContext where = normalVisitor.visit(lambda);
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

    protected void include(LambdaExpression<?> lambda, LambdaExpression<?> cond, List<IncludeSet> includeSets)
    {
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        SqlContext context = normalVisitor.visit(lambda);
        if (context instanceof SqlPropertyContext)
        {
            SqlPropertyContext propertyContext = (SqlPropertyContext) context;
            if (!propertyContext.getPropertyMetaData().hasNavigate())
            {
                throw new RuntimeException("include指定的字段需要被@Navigate修饰");
            }
            relationTypeCheck(propertyContext.getPropertyMetaData().getNavigateData());
            IncludeSet includeSet;
            if (cond != null)
            {
                NormalVisitor normalVisitor2 = new NormalVisitor(getConfig());
                SqlContext condition = normalVisitor2.visit(cond);
                includeSet = new IncludeSet(propertyContext, condition);
            }
            else
            {
                includeSet = new IncludeSet(propertyContext);
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
        include(lambda, cond, sqlBuilder.getIncludes());
    }

    protected void include(LambdaExpression<?> lambda)
    {
        include(lambda, null, sqlBuilder.getIncludes());
    }

    protected <R> void includeByCond(LambdaExpression<?> lambda, Action1<IncludeCond<R>> action, List<IncludeSet> includeSets)
    {
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        SqlContext context = normalVisitor.visit(lambda);
        if (context instanceof SqlPropertyContext)
        {
            SqlPropertyContext propertyContext = (SqlPropertyContext) context;
            PropertyMetaData propertyMetaData = propertyContext.getPropertyMetaData();
            if (!propertyContext.getPropertyMetaData().hasNavigate())
            {
                throw new RuntimeException("include指定的字段需要被@Navigate修饰");
            }
            relationTypeCheck(propertyContext.getPropertyMetaData().getNavigateData());
            Class<R> navigateTargetType = (Class<R>) propertyMetaData.getNavigateData().getNavigateTargetType();
            IncludeCond<R> temp = new IncludeCond<>(getConfig(), navigateTargetType);
            action.invoke(temp);
            IncludeSet includeSet = new IncludeSet(propertyContext, new SqlVirtualTableContext(temp.getSqlBuilder()));
            includeSets.add(includeSet);
        }
        else
        {
            throw new RuntimeException("include需要指定一个字段");
        }
    }

    protected <R> void includeByCond(LambdaExpression<?> lambda, Action1<IncludeCond<R>> action)
    {
        includeByCond(lambda, action, sqlBuilder.getIncludes());
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
