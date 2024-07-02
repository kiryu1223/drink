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
import io.github.kiryu1223.expressionTree.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.fieldToSetterName;
import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.getTableName;

public abstract class QueryBase implements CRUD
{
    private final QuerySqlBuilder sqlBuilder;

    private List<ColumnMetaData> columnMetaData;

    public QueryBase(Config config)
    {
        sqlBuilder = new QuerySqlBuilder(config);
    }

    public QueryBase(QuerySqlBuilder sqlBuilder)
    {
        this.sqlBuilder = sqlBuilder;
    }

    @Override
    public QuerySqlBuilder getSqlBuilder()
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
        setDefaultColumnMetaData();
        List<Object> values = new ArrayList<>();
        String sql = sqlBuilder.getSqlAndValue(values);
        SqlSession session = SqlSessionBuilder.getSession();
        return session.executeQuery(r ->
                        new ObjectBuilder<>(r, getConfig(), (Class<T>) sqlBuilder.getTargetClass(), columnMetaData).createList(),
                sql,
                values
        );
    }

    // region [123]
    protected void select(LambdaExpression<?> lambda)
    {
        SelectVisitor selectVisitor = new SelectVisitor(getSqlBuilder().getGroupBy(),getConfig());
        SqlContext context = selectVisitor.visit(lambda);
        getSqlBuilder().setSelect(context);
        getSqlBuilder().setTargetClass(lambda.getReturnType());
        columnMetaData = selectVisitor.getColumnMetaData();
    }

    protected void select0()
    {
        setDefaultData();
    }

    protected <R> void select0(Class<R> c)
    {
        getSqlBuilder().setTargetClass(c);
        setDefaultData();
    }

    private void setDefaultData()
    {
        List<ColumnMetaData> columnMetaData = new ArrayList<>();
        List<SqlContext> sqlContextList = new ArrayList<>();
        Class<?> targetClass = getSqlBuilder().getTargetClass();
        if (getSqlBuilder().getOrderedClass().contains(targetClass))
        {
            int index = getSqlBuilder().getOrderedClass().indexOf(targetClass);
            MetaData metaData = MetaDataCache.getMetaData(targetClass);
            for (Map.Entry<String, Field> name : metaData.getColumns().entrySet())
            {
                sqlContextList.add(new SqlPropertyContext(name.getKey(), index));
                columnMetaData.add(new ColumnMetaData(name.getKey(), ReflectUtil.getMethod(targetClass, fieldToSetterName(name.getValue()), new Class[]{name.getValue().getType()}),getConfig()));
            }
        }
        else
        {
            List<MetaData> metaDataList = MetaDataCache.getMetaData(getSqlBuilder().getOrderedClass());
            MetaData metaData = MetaDataCache.getMetaData(targetClass);
            for (Map.Entry<String, Field> column : metaData.getColumns().entrySet())
            {
                label:
                for (int i = 0; i < metaDataList.size(); i++)
                {
                    for (Map.Entry<String, Field> temp : metaDataList.get(i).getColumns().entrySet())
                    {
                        if (temp.getKey().equals(column.getKey()) && temp.getValue().getType().equals(column.getValue().getType()))
                        {
                            sqlContextList.add(new SqlPropertyContext(column.getKey(), i));
                            columnMetaData.add(new ColumnMetaData(column.getKey(), ReflectUtil.getMethod(targetClass, fieldToSetterName(column.getValue()), new Class[]{column.getValue().getType()}),getConfig()));
                            break label;
                        }
                    }
                }
            }
        }
        getSqlBuilder().setSelect(new SqlSelectorContext(sqlContextList));
        this.columnMetaData = columnMetaData;
    }

    private void setDefaultColumnMetaData()
    {
        if (columnMetaData != null) return;
        List<ColumnMetaData> columnMetaData = new ArrayList<>();
        Class<?> targetClass = getSqlBuilder().getTargetClass();
        if (getSqlBuilder().getOrderedClass().contains(targetClass))
        {
            MetaData metaData = MetaDataCache.getMetaData(targetClass);
            for (Map.Entry<String, Field> name : metaData.getColumns().entrySet())
            {
                columnMetaData.add(new ColumnMetaData(name.getKey(), ReflectUtil.getMethod(targetClass, fieldToSetterName(name.getValue()), new Class[]{name.getValue().getType()}),getConfig()));
            }
        }
        else
        {
            List<MetaData> metaDataList = MetaDataCache.getMetaData(getSqlBuilder().getOrderedClass());
            MetaData metaData = MetaDataCache.getMetaData(targetClass);
            for (Map.Entry<String, Field> column : metaData.getColumns().entrySet())
            {
                label:
                for (int i = 0; i < metaDataList.size(); i++)
                {
                    for (Map.Entry<String, Field> temp : metaDataList.get(i).getColumns().entrySet())
                    {
                        if (temp.getKey().equals(column.getKey()) && temp.getValue().getType().equals(column.getValue().getType()))
                        {
                            columnMetaData.add(new ColumnMetaData(column.getKey(), ReflectUtil.getMethod(targetClass, fieldToSetterName(column.getValue()), new Class[]{column.getValue().getType()}),getConfig()));
                            break label;
                        }
                    }
                }
            }
        }
        this.columnMetaData = columnMetaData;
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
        HavingVisitor havingVisitor = new HavingVisitor(getSqlBuilder().getGroupBy(),getConfig());
        SqlContext context = havingVisitor.visit(lambda);
        getSqlBuilder().addHaving(context);
    }

    protected void orderBy(LambdaExpression<?> lambda, boolean asc)
    {
        HavingVisitor havingVisitor = new HavingVisitor(getSqlBuilder().getGroupBy(),getConfig());
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

    // endregion
}
