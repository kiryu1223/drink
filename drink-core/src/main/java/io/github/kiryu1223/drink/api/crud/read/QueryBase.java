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

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.getTableName;

public abstract class QueryBase implements CRUD
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

    public boolean any()
    {
        SqlSession session = SqlSessionBuilder.getSession();
        List<Object> values = new ArrayList<>();
        String sql = sqlBuilder.getSqlAndValue(values);
        return session.executeQuery(f -> f.next(), "SELECT 1 FROM (" + sql + ")", values);
    }

    public <T> List<T> toList()
    {
        setDefaultMetaData();
        List<Object> values = new ArrayList<>();
        String sql = sqlBuilder.getSqlAndValue(values);
        SqlSession session = SqlSessionBuilder.getSession();
        return session.executeQuery(r ->
                        new ObjectBuilder<>(r, getConfig(), (Class<T>) sqlBuilder.getTargetClass(), propertyMetaData, isSingle).createList(),
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

    protected <R> void select0(Class<R> c)
    {
        getSqlBuilder().setTargetClass(c);
        setDefaultSelectAndMetaData();
    }

    private void setDefaultSelectAndMetaData()
    {
        propertyMetaData = new ArrayList<>();
        List<SqlContext> sqlContextList = new ArrayList<>();
        Class<?> targetClass = getSqlBuilder().getTargetClass();
        if (getSqlBuilder().getOrderedClass().contains(targetClass))
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
        if (propertyMetaData != null) return;
        propertyMetaData = new ArrayList<>();
        Class<?> targetClass = getSqlBuilder().getTargetClass();
        if (getSqlBuilder().getOrderedClass().contains(targetClass))
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
                for (int i = 0; i < metaDataList.size(); i++)
                {
                    for (Map.Entry<String, PropertyMetaData> temp : metaDataList.get(i).getColumns().entrySet())
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

    protected <Tn> QueryBase joinNewQuery()
    {
        return null;
    }

    protected void join(JoinType joinType, Class<?> target, ExprTree<?> expr)
    {
        WhereVisitor whereVisitor = new WhereVisitor(getConfig());
        SqlContext context = whereVisitor.visit(expr.getTree());
        SqlJoinTableContext sqlJoin = new SqlJoinTableContext(joinType, context, getTableName(target));
        getSqlBuilder().addJoin(target, sqlJoin);
    }

    protected void join(JoinType joinType, QueryBase target, ExprTree<?> expr)
    {
        WhereVisitor whereVisitor = new WhereVisitor(getConfig());
        SqlContext context = whereVisitor.visit(expr.getTree());
        SqlJoinQueryContext sqlJoin = new SqlJoinQueryContext(joinType, context, target.getSqlBuilder());
        getSqlBuilder().addJoin(target.getSqlBuilder().getTargetClass(), sqlJoin);
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

    public String toSql()
    {
        return getSqlBuilder().getSql();
    }

    protected void singleCheck(boolean single)
    {
        if (single)
        {
            throw new RuntimeException("query.select(Func<T1,T2..., R> expr) 不允许传入单个元素");
        }
    }
}
