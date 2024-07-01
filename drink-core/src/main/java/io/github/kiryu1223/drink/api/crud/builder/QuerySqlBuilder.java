package io.github.kiryu1223.drink.api.crud.builder;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.SqlAsNameContext;
import io.github.kiryu1223.drink.core.context.SqlContext;
import io.github.kiryu1223.drink.core.context.SqlFromQueryContext;
import io.github.kiryu1223.drink.core.context.SqlFromTableContext;
import io.github.kiryu1223.drink.core.visitor.ExpressionUtil;

import java.util.ArrayList;
import java.util.List;

public class QuerySqlBuilder implements ISqlBuilder
{
    private final Config config;
    private SqlContext select;
    private boolean distinct = false;
    private final List<SqlContext> from = new ArrayList<>();
    private final List<SqlContext> joins = new ArrayList<>();
    private final List<SqlContext> wheres = new ArrayList<>();
    private SqlContext groupBy;
    private final List<SqlContext> havings = new ArrayList<>();
    private final List<SqlContext> orderBys = new ArrayList<>();
    private SqlContext limit;

    private final List<Class<?>> orderedClass = new ArrayList<>();
    private Class<?> targetClass;

    private boolean queried = false;

    public QuerySqlBuilder(Config config)
    {
        this.config = config;
    }

    private void subQueried()
    {
        queried = true;
    }

    public void setTargetClass(Class<?> targetClass)
    {
        this.targetClass = targetClass;
    }

    public Class<?> getTargetClass()
    {
        return targetClass;
    }

    public void joinBy(QuerySqlBuilder querySqlBuilder)
    {
        from.addAll(querySqlBuilder.from);
        joins.addAll(querySqlBuilder.joins);
        targetClass = querySqlBuilder.targetClass;
        queried = querySqlBuilder.queried;
    }

    public void setSelect(SqlContext select)
    {
        this.select = select;
        subQueried();
    }

    public void addFrom(Class<?> queryClass)
    {
        SqlFromTableContext sqlFromTableContext = new SqlFromTableContext(ExpressionUtil.getTableName(queryClass));
        from.add(new SqlAsNameContext("t" + from.size(), sqlFromTableContext));
        orderedClass.add(queryClass);
        if (targetClass == null)
        {
            targetClass = queryClass;
        }
    }

    public void addFrom(Class<?>... queryClasses)
    {
        for (Class<?> queryClass : queryClasses)
        {
            addFrom(queryClass);
        }
    }

    public void addFrom(QuerySqlBuilder sqlBuilder)
    {
        from.add(new SqlAsNameContext("t" + from.size(), new SqlFromQueryContext(sqlBuilder)));
        orderedClass.addAll(sqlBuilder.orderedClass);
        if (targetClass == null)
        {
            targetClass = sqlBuilder.targetClass;
        }
    }

    public void addFrom(QuerySqlBuilder... sqlBuilders)
    {
        for (QuerySqlBuilder sqlBuilder : sqlBuilders)
        {
            addFrom(sqlBuilder);
        }
    }

    public void addJoin(Class<?> target, SqlContext join)
    {
        SqlAsNameContext sqlAsNameContext = new SqlAsNameContext("t" + from.size() + joins.size(), join);
        joins.add(sqlAsNameContext);
        orderedClass.add(target);
        subQueried();
    }

    public void addWhere(SqlContext where)
    {
        wheres.add(where);
        subQueried();
    }

    public void setGroupBy(SqlContext groupBy)
    {
        this.groupBy = groupBy;
        subQueried();
    }

    public void addHaving(SqlContext having)
    {
        havings.add(having);
        subQueried();
    }

    public void addOrderBy(SqlContext orderBy)
    {
        orderBys.add(orderBy);
        subQueried();
    }

    public void setLimit(SqlContext limit)
    {
        this.limit = limit;
        subQueried();
    }

    @Override
    public String getSql()
    {
        return null;
    }

    @Override
    public String getSqlAndValue(List<Object> values)
    {
        return null;
    }

    public SqlContext getGroupBy()
    {
        return groupBy;
    }

    public void setDistinct(boolean distinct)
    {
        this.distinct = distinct;
    }

    public Config getConfig()
    {
        return config;
    }
}
