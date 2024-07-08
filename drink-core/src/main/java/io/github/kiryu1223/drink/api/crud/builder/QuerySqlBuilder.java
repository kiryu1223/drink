package io.github.kiryu1223.drink.api.crud.builder;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.builder.MetaData;
import io.github.kiryu1223.drink.core.builder.MetaDataCache;
import io.github.kiryu1223.drink.core.builder.PropertyMetaData;
import io.github.kiryu1223.drink.core.context.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuerySqlBuilder implements ISqlBuilder
{
    private final Config config;
    private SqlContext select;
    private int existsCount;
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

    public List<SqlContext> getWheres()
    {
        return wheres;
    }

    public void addExistsCount(int count)
    {
        existsCount += count;
    }

    public SqlContext getSelect()
    {
        return select;
    }

    public List<Class<?>> getOrderedClass()
    {
        return orderedClass;
    }

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
        orderedClass.addAll(querySqlBuilder.orderedClass);
        queried = querySqlBuilder.queried;
    }

    public void setSelect(SqlContext select)
    {
        this.select = select;
        subQueried();
    }

    public void addFrom(Class<?> queryClass)
    {
        SqlTableContext sqlTableContext = new SqlRealTableContext(queryClass);
        from.add(new SqlAsTableNameContext(from.size() + existsCount, sqlTableContext));
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
        SqlTableContext sqlTableContext = new SqlVirtualTableContext(sqlBuilder);
        SqlParensContext sqlParensContext = new SqlParensContext(sqlTableContext);
        from.add(new SqlAsTableNameContext(from.size() + existsCount, sqlParensContext));
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

    public void addJoin(Class<?> target, JoinType joinType, SqlTableContext tableContext, SqlContext onContext)
    {
        SqlJoinContext joinContext = new SqlJoinContext(
                joinType,
                new SqlAsTableNameContext(from.size() + joins.size(),
                        tableContext instanceof SqlRealTableContext
                                ? tableContext :
                                new SqlParensContext(tableContext)),
                onContext
        );
        joins.add(joinContext);
        orderedClass.add(target);
        subQueried();
    }

    public void addWhere(SqlContext where)
    {
        wheres.add(where);
        subQueried();
    }

    public void addOrWhere(SqlContext where)
    {
        removeAndBoxOr(wheres, where);
        subQueried();
    }

//    public void addExists(SqlContext exists)
//    {
//        addWhere(exists);
//        if (hasExists) return;
//        move(from);
//        move(joins);
//        move(joins);
//        hasExists = true;
//    }

    private void move(List<SqlContext> contexts)
    {
        for (SqlContext context : contexts)
        {
            if (context instanceof SqlAsTableNameContext)
            {
                SqlAsTableNameContext sqlAsTableNameContext = (SqlAsTableNameContext) context;
                sqlAsTableNameContext.setIndex(sqlAsTableNameContext.getIndex() + 1);
            }
            else if (context instanceof SqlPropertyContext)
            {
                SqlPropertyContext sqlPropertyContext = (SqlPropertyContext) context;
                sqlPropertyContext.setTableIndex(sqlPropertyContext.getTableIndex() + 1);
            }
        }
    }

    public void setGroupBy(SqlContext groupBy)
    {
        this.groupBy = groupBy;
        // groupBy时应该清空旧的orderBy
        orderBys.clear();
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

    public SqlContext getGroupBy()
    {
        return groupBy;
    }

    public void setDistinct(boolean distinct)
    {
        this.distinct = distinct;
        subQueried();
    }

    public Config getConfig()
    {
        return config;
    }

    @Override
    public String getSql()
    {
        if (queried)
        {
            return makeSelect() +
                    makeFrom() +
                    makeJoin() +
                    makeWhere() +
                    makeGroup() +
                    makeHaving() +
                    makeOrder() +
                    makeLimit();
        }
        else
        {
            SqlContext context = unBox(from.get(0));
            if (context instanceof SqlRealTableContext)
            {
                return makeSelect() + context.getSql(config);
            }
            else
            {
                return context.getSql(config);
            }
        }
    }

    private SqlContext unBox(SqlContext context)
    {
        if (context instanceof SqlAsNameContext)
        {
            SqlAsNameContext sqlAsNameContext = (SqlAsNameContext) context;
            return unBox(sqlAsNameContext.getContext());
        }
        else if (context instanceof SqlAsTableNameContext)
        {
            SqlAsTableNameContext sqlAsTableNameContext = (SqlAsTableNameContext) context;
            return sqlAsTableNameContext.getContext();
        }
        else if (context instanceof SqlParensContext)
        {
            SqlParensContext sqlParensContext = (SqlParensContext) context;
            return sqlParensContext.getContext();
        }

        return context;
    }

    @Override
    public String getSqlAndValue(List<Object> values)
    {
        if (queried)
        {
            return makeSelect(values) +
                    makeFrom(values) +
                    makeJoin(values) +
                    makeWhere(values) +
                    makeGroup(values) +
                    makeHaving(values) +
                    makeOrder(values) +
                    makeLimit(values);
        }
        else
        {
            SqlContext context = unBox(from.get(0));
            if (context instanceof SqlRealTableContext)
            {
                return makeSelect() + context.getSqlAndValue(config, values);
            }
            else
            {
                return context.getSqlAndValue(config, values);
            }
        }
    }

    private String makeSelect(List<Object> values)
    {
        //return "SELECT " + (distinct ? "DISTINCT " : "") + select.getSqlAndValue(config,values);
        if (select == null)
        {
            if (groupBy == null)
            {
                MetaData metaData = MetaDataCache.getMetaData(targetClass);
                List<String> stringList = new ArrayList<>();
                for (PropertyMetaData data : metaData.getColumns().values())
                {
                    stringList.add("t0." + config.getDbConfig().propertyDisambiguation(data.getColumn()));
                }
                return "SELECT " + (distinct ? "DISTINCT " : "") + String.join(",", stringList);
            }
            else
            {
                if (groupBy instanceof SqlGroupContext)
                {
                    SqlGroupContext group = (SqlGroupContext) groupBy;
                    List<String> stringList = new ArrayList<>();
                    for (Map.Entry<String, SqlContext> entry : group.getContextMap().entrySet())
                    {
                        stringList.add(new SqlAsNameContext(entry.getKey(), entry.getValue()).getSqlAndValue(config, values));
                    }
                    return "SELECT " + (distinct ? "DISTINCT " : "") + String.join(",", stringList);
                }
                else
                {
                    return "SELECT " + (distinct ? "DISTINCT " : "") + groupBy.getSqlAndValue(config, values);
                }
            }
        }
        else
        {
            return "SELECT " + (distinct ? "DISTINCT " : "") + select.getSqlAndValue(config, values);
        }
    }

    private String makeSelect()
    {
        // return "SELECT " + (distinct ? "DISTINCT " : "") + select.getSql(config);
        if (select == null)
        {
            if (groupBy == null)
            {
                MetaData metaData = MetaDataCache.getMetaData(targetClass);
                List<String> stringList = new ArrayList<>();
                for (PropertyMetaData data : metaData.getColumns().values())
                {
                    stringList.add("t0." + config.getDbConfig().propertyDisambiguation(data.getColumn()));
                }
                return "SELECT " + (distinct ? "DISTINCT " : "") + String.join(",", stringList);
            }
            else
            {
                if (groupBy instanceof SqlGroupContext)
                {
                    SqlGroupContext group = (SqlGroupContext) groupBy;
                    List<String> stringList = new ArrayList<>();
                    for (Map.Entry<String, SqlContext> entry : group.getContextMap().entrySet())
                    {
                        stringList.add(new SqlAsNameContext(entry.getKey(), entry.getValue()).getSql(config));
                    }
                    return "SELECT " + (distinct ? "DISTINCT " : "") + String.join(",", stringList);
                }
                else
                {
                    return "SELECT " + (distinct ? "DISTINCT " : "") + groupBy.getSql(config);
                }
            }
        }
        else
        {
            return "SELECT " + (distinct ? "DISTINCT " : "") + select.getSql(config);
        }
    }

    private String makeFrom(List<Object> values)
    {
        List<String> froms = new ArrayList<>(from.size());
        for (SqlContext context : from)
        {
            String sqlAndValue = context.getSqlAndValue(config, values);
            froms.add(sqlAndValue);
        }
        return " " + String.join(",", froms);
    }

    private String makeFrom()
    {
        List<String> froms = new ArrayList<>(from.size());
        for (SqlContext context : from)
        {
            froms.add(context.getSql(config));
        }
        return " FROM " + String.join(",", froms);
    }

    private String makeJoin(List<Object> values)
    {
        if (joins.isEmpty()) return "";
        List<String> joinStr = new ArrayList<>(from.size());
        for (SqlContext context : joins)
        {
            joinStr.add(context.getSqlAndValue(config, values));
        }
        return " " + String.join(",", joinStr);
    }

    private String makeJoin()
    {
        if (joins.isEmpty()) return "";
        List<String> joinStr = new ArrayList<>(from.size());
        for (SqlContext context : joins)
        {
            joinStr.add(context.getSql(config));
        }
        return " " + String.join(",", joinStr);
    }

    private String makeWhere(List<Object> values)
    {
        if (wheres.isEmpty()) return "";
        List<String> whereStr = new ArrayList<>(from.size());
        for (SqlContext context : wheres)
        {
            whereStr.add(context.getSqlAndValue(config, values));
        }
        return " WHERE " + String.join(" AND ", whereStr);
    }

    private String makeWhere()
    {
        if (wheres.isEmpty()) return "";
        List<String> whereStr = new ArrayList<>(from.size());
        for (SqlContext context : wheres)
        {
            whereStr.add(context.getSql(config));
        }
        return " WHERE " + String.join(" AND ", whereStr);
    }

    private String makeGroup(List<Object> values)
    {
        if (groupBy == null) return "";
        return " GROUP BY " + groupBy.getSqlAndValue(config, values);
    }

    private String makeGroup()
    {
        if (groupBy == null) return "";
        return " GROUP BY " + groupBy.getSql(config);
    }

    private String makeHaving(List<Object> values)
    {
        if (havings.isEmpty()) return "";
        List<String> havingStr = new ArrayList<>(from.size());
        for (SqlContext context : havings)
        {
            havingStr.add(context.getSqlAndValue(config, values));
        }
        return " HAVING " + String.join(" AND ", havingStr);
    }

    private String makeHaving()
    {
        if (havings.isEmpty()) return "";
        List<String> havingStr = new ArrayList<>(from.size());
        for (SqlContext context : havings)
        {
            havingStr.add(context.getSql(config));
        }
        return " HAVING " + String.join(" AND ", havingStr);
    }

    private String makeOrder(List<Object> values)
    {
        if (orderBys.isEmpty()) return "";
        List<String> orderStr = new ArrayList<>(from.size());
        for (SqlContext context : orderBys)
        {
            orderStr.add(context.getSqlAndValue(config, values));
        }
        return " ORDER BY " + String.join(",", orderStr);
    }

    private String makeOrder()
    {
        if (orderBys.isEmpty()) return "";
        List<String> orderStr = new ArrayList<>(from.size());
        for (SqlContext context : orderBys)
        {
            orderStr.add(context.getSql(config));
        }
        return " ORDER BY " + String.join(",", orderStr);
    }

    private String makeLimit(List<Object> values)
    {
        if (limit == null) return "";
        return limit.getSqlAndValue(config, values);
    }

    private String makeLimit()
    {
        if (limit == null) return "";
        return limit.getSql(config);
    }

    private void removeAndBoxOr(List<SqlContext> contexts, SqlContext right)
    {
        if (contexts.isEmpty())
        {
            contexts.add(right);
        }
        else
        {
            SqlContext left = contexts.remove(contexts.size() - 1);
            contexts.add(new SqlBinaryContext(SqlOperator.OR, left, right));
        }
    }
}
