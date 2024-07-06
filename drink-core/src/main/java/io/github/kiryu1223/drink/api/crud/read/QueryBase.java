package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.api.crud.base.CRUD;
import io.github.kiryu1223.drink.api.crud.builder.QuerySqlBuilder;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.builder.*;
import io.github.kiryu1223.drink.core.context.*;
import io.github.kiryu1223.drink.core.visitor.GroupByVisitor;
import io.github.kiryu1223.drink.core.visitor.HavingVisitor;
import io.github.kiryu1223.drink.core.visitor.SelectVisitor;
import io.github.kiryu1223.drink.core.visitor.WhereVisitor;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;
import io.github.kiryu1223.expressionTree.expressions.LambdaExpression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public abstract class QueryBase extends CRUD
{
    private final QuerySqlBuilder sqlBuilder;
    private List<PropertyMetaData> propertyMetaData;
    private boolean isSingle;

    public QueryBase(Config config)
    {
        sqlBuilder = new QuerySqlBuilder(config);
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
        SqlSession session = SqlSessionBuilder.getSession();
        List<Object> values = new ArrayList<>();
        String sql = sqlBuilder.getSqlAndValue(values);
        return session.executeQuery(f -> f.next(), "SELECT 1 FROM (" + sql + ") LIMIT 1", values);
    }

    protected <R> EndQuery<R> select(Class<R> r)
    {
        select0(r);
        return new EndQuery<>(this);
    }

    protected <T> List<T> toList()
    {
        setDefaultMetaData();
        List<Object> values = new ArrayList<>();
        String sql = sqlBuilder.getSqlAndValue(values);
        SqlSession session = SqlSessionBuilder.getSession();
        return session.executeQuery(
                r -> ObjectBuilder.start(r, getConfig(), (Class<T>) sqlBuilder.getTargetClass(), propertyMetaData, isSingle).createList(),
                sql,
                values
        );
    }

    // region [123]
    protected boolean select(LambdaExpression<?> lambda)
    {
        SelectVisitor selectVisitor = new SelectVisitor(getSqlBuilder().getGroupBy(), getConfig());
        SqlContext context = selectVisitor.visit(lambda);
        getSqlBuilder().setSelect(context);
        getSqlBuilder().setTargetClass(lambda.getReturnType());
        propertyMetaData = selectVisitor.getPropertyMetaData();
        return !(context instanceof SqlSelectorContext);
    }

    protected void select0()
    {
        setDefaultSelectAndMetaData();
    }

    protected void select0(Class<?> c)
    {
        getSqlBuilder().setTargetClass(c);
        setDefaultSelectAndMetaData();
    }

    private void setDefaultSelectAndMetaData()
    {
        propertyMetaData = new ArrayList<>();
        List<SqlContext> sqlContextList = new ArrayList<>();
        Class<?> targetClass = getSqlBuilder().getTargetClass();
        if (getSqlBuilder().getGroupBy() != null)
        {

            SqlContext groupBy = getSqlBuilder().getGroupBy();
            MetaData metaData = MetaDataCache.getMetaData(getSqlBuilder().getTargetClass());
            if (groupBy instanceof SqlGroupContext)
            {
                SqlGroupContext group = (SqlGroupContext) groupBy;
                for (Map.Entry<String, SqlContext> entry : group.getContextMap().entrySet())
                {
                    sqlContextList.add(new SqlAsNameContext(entry.getKey(), entry.getValue()));
                    propertyMetaData.add(metaData.getPropertyMetaData(entry.getKey()));
                }
            }
            else
            {
                sqlContextList.add(groupBy);
                setSingle(true);
            }
        }
        else if (getSqlBuilder().getOrderedClass().contains(targetClass))
        {
            int index = getSqlBuilder().getOrderedClass().indexOf(targetClass);
            MetaData metaData = MetaDataCache.getMetaData(targetClass);
            for (Map.Entry<String, PropertyMetaData> entry : metaData.getColumns().entrySet())
            {
                sqlContextList.add(new SqlPropertyContext(entry.getKey(), index));
                propertyMetaData.add(entry.getValue());
            }
        }
        else
        {
            List<MetaData> metaDataList = MetaDataCache.getMetaData(getSqlBuilder().getOrderedClass());
            MetaData metaData = MetaDataCache.getMetaData(targetClass);
            for (Map.Entry<String, PropertyMetaData> column : metaData.getColumns().entrySet())
            {
                label:
                for (int i = 0; i < metaDataList.size(); i++)
                {
                    for (Map.Entry<String, PropertyMetaData> temp : metaDataList.get(i).getColumns().entrySet())
                    {
                        if (temp.getKey().equals(column.getKey()) && temp.getValue().equals(column.getValue()))
                        {
                            sqlContextList.add(new SqlPropertyContext(column.getKey(), i));
                            propertyMetaData.add(column.getValue());
                            break label;
                        }
                    }
                }
            }
        }
        getSqlBuilder().setSelect(new SqlSelectorContext(sqlContextList));
    }

    private void setDefaultMetaData()
    {
        if (propertyMetaData != null && !propertyMetaData.isEmpty()) return;
        propertyMetaData = new ArrayList<>();
        Class<?> targetClass = getSqlBuilder().getTargetClass();
        if (getSqlBuilder().getGroupBy() != null)
        {
            SqlContext groupBy = getSqlBuilder().getGroupBy();
            MetaData metaData = MetaDataCache.getMetaData(getSqlBuilder().getTargetClass());
            if (groupBy instanceof SqlGroupContext)
            {
                SqlGroupContext group = (SqlGroupContext) groupBy;
                for (Map.Entry<String, SqlContext> entry : group.getContextMap().entrySet())
                {
                    propertyMetaData.add(metaData.getPropertyMetaData(entry.getKey()));
                }
            }
            else
            {
                setSingle(true);
            }
        }
        else if (getSqlBuilder().getOrderedClass().contains(targetClass))
        {
            MetaData metaData = MetaDataCache.getMetaData(targetClass);
            propertyMetaData.addAll(metaData.getColumns().values());
        }
        else
        {
            List<MetaData> metaDataList = MetaDataCache.getMetaData(getSqlBuilder().getOrderedClass());
            MetaData metaData = MetaDataCache.getMetaData(targetClass);
            for (Map.Entry<String, PropertyMetaData> column : metaData.getColumns().entrySet())
            {
                label:
                for (MetaData data : metaDataList)
                {
                    for (Map.Entry<String, PropertyMetaData> temp : data.getColumns().entrySet())
                    {
                        if (temp.getKey().equals(column.getKey()) && temp.getValue().equals(column.getValue()))
                        {
                            propertyMetaData.add(column.getValue());
                            break label;
                        }
                    }
                }
            }
        }
    }

//    private void setDefaultSelect()
//    {
//        if (getSqlBuilder().getSelect() != null) return;
//        List<SqlContext> sqlContextList = new ArrayList<>();
//        Class<?> targetClass = getSqlBuilder().getTargetClass();
//        if (getSqlBuilder().getGroupBy() != null)
//        {
//            SqlContext groupBy = getSqlBuilder().getGroupBy();
//            if (groupBy instanceof SqlGroupContext)
//            {
//                SqlGroupContext group = (SqlGroupContext) groupBy;
//                for (Map.Entry<String, SqlContext> entry : group.getContextMap().entrySet())
//                {
//                    sqlContextList.add(new SqlAsNameContext(entry.getKey(), entry.getValue()));
//                }
//            }
//            else
//            {
//                sqlContextList.add(groupBy);
//                setSingle(true);
//            }
//        }
//        else if (getSqlBuilder().getOrderedClass().contains(targetClass))
//        {
//            int index = getSqlBuilder().getOrderedClass().indexOf(targetClass);
//            MetaData metaData = MetaDataCache.getMetaData(targetClass);
//            for (Map.Entry<String, PropertyMetaData> entry : metaData.getColumns().entrySet())
//            {
//                sqlContextList.add(new SqlPropertyContext(entry.getKey(), index));
//            }
//        }
//        else
//        {
//            List<MetaData> metaDataList = MetaDataCache.getMetaData(getSqlBuilder().getOrderedClass());
//            MetaData metaData = MetaDataCache.getMetaData(targetClass);
//            for (Map.Entry<String, PropertyMetaData> column : metaData.getColumns().entrySet())
//            {
//                label:
//                for (int i = 0; i < metaDataList.size(); i++)
//                {
//                    for (Map.Entry<String, PropertyMetaData> temp : metaDataList.get(i).getColumns().entrySet())
//                    {
//                        if (temp.getKey().equals(column.getKey()) && temp.getValue().equals(column.getValue()))
//                        {
//                            sqlContextList.add(new SqlPropertyContext(column.getKey(), i));
//                            break label;
//                        }
//                    }
//                }
//            }
//        }
//        getSqlBuilder().setSelect(new SqlSelectorContext(sqlContextList));
//    }

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

    protected void groupBy(LambdaExpression<?> lambda)
    {
        GroupByVisitor groupByVisitor = new GroupByVisitor(getConfig());
        SqlContext context = groupByVisitor.visit(lambda);
        getSqlBuilder().setGroupBy(context);
        getSqlBuilder().setTargetClass(lambda.getReturnType());
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

    protected void setSingle(boolean single)
    {
        isSingle = single;
    }

    // endregion

    protected void singleCheck(boolean single)
    {
        if (single)
        {
            throw new RuntimeException("query.select(Func<T1,T2..., R> expr) 不允许传入单个元素, 单元素请使用selectSingle");
        }
    }

    public String toSql()
    {
        //setDefaultSelect();
        return sqlBuilder.getSql();
    }
}
