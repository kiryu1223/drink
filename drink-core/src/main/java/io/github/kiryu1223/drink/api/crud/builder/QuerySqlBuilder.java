package io.github.kiryu1223.drink.api.crud.builder;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;
import io.github.kiryu1223.drink.core.context.*;
import io.github.kiryu1223.drink.core.visitor.ExpressionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.unBox;

public class QuerySqlBuilder implements ISqlBuilder
{
    private final Config config;
    private SqlContext select;
    private int moveCount;
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

    public List<SqlContext> getFrom()
    {
        return from;
    }

    public List<SqlContext> getWheres()
    {
        return wheres;
    }

    public void addMoveCount(int count)
    {
        moveCount += count;
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

    public boolean isQueried()
    {
        return queried;
    }

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

//    public void joinBy(QuerySqlBuilder querySqlBuilder)
//    {
//        from.addAll(querySqlBuilder.from);
//        joins.addAll(querySqlBuilder.joins);
//        targetClass = querySqlBuilder.targetClass;
//        orderedClass.addAll(querySqlBuilder.orderedClass);
//        queried = querySqlBuilder.queried;
//    }

    public void setSelect(SqlContext select)
    {
        this.select = select;
        subQueried();
    }

    public void addFrom(Class<?> queryClass)
    {
        SqlTableContext sqlTableContext = new SqlRealTableContext(queryClass);
        from.add(new SqlAsTableNameContext(from.size() + moveCount, sqlTableContext));
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
        from.add(new SqlAsTableNameContext(from.size() + moveCount, sqlParensContext));
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
                return makeSelect() + " FROM " + new SqlAsTableNameContext(0, context).getSql(config);
            }
            else
            {
                return context.getSql(config);
            }
        }
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
                return makeSelect() + " FROM " + new SqlAsTableNameContext(0, context).getSqlAndValue(config, values);
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
                    stringList.add("t0." + config.getDisambiguation().disambiguation(data.getColumn()));
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
                    stringList.add("t0." + config.getDisambiguation().disambiguation(data.getColumn()));
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
        return " FROM " + String.join(",", froms);
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
        List<String> joinStr = new ArrayList<>(joins.size());
        for (SqlContext context : joins)
        {
            joinStr.add(context.getSqlAndValue(config, values));
        }
        return " " + String.join(" ", joinStr);
    }

    private String makeJoin()
    {
        if (joins.isEmpty()) return "";
        List<String> joinStr = new ArrayList<>(joins.size());
        for (SqlContext context : joins)
        {
            joinStr.add(context.getSql(config));
        }
        return " " + String.join(" ", joinStr);
    }

    private String makeWhere(List<Object> values)
    {
        if (wheres.isEmpty()) return "";
        List<String> whereStr = new ArrayList<>(wheres.size());
        for (SqlContext context : wheres)
        {
            whereStr.add(context.getSqlAndValue(config, values));
        }
        return " WHERE " + String.join(" AND ", whereStr);
    }

    private String makeWhere()
    {
        if (wheres.isEmpty()) return "";
        List<String> whereStr = new ArrayList<>(wheres.size());
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
        List<String> havingStr = new ArrayList<>(havings.size());
        for (SqlContext context : havings)
        {
            havingStr.add(context.getSqlAndValue(config, values));
        }
        return " HAVING " + String.join(" AND ", havingStr);
    }

    private String makeHaving()
    {
        if (havings.isEmpty()) return "";
        List<String> havingStr = new ArrayList<>(havings.size());
        for (SqlContext context : havings)
        {
            havingStr.add(context.getSql(config));
        }
        return " HAVING " + String.join(" AND ", havingStr);
    }

    private String makeOrder(List<Object> values)
    {
        if (orderBys.isEmpty()) return "";
        List<String> orderStr = new ArrayList<>(orderBys.size());
        for (SqlContext context : orderBys)
        {
            orderStr.add(context.getSqlAndValue(config, values));
        }
        return " ORDER BY " + String.join(",", orderStr);
    }

    private String makeOrder()
    {
        if (orderBys.isEmpty()) return "";
        List<String> orderStr = new ArrayList<>(orderBys.size());
        for (SqlContext context : orderBys)
        {
            orderStr.add(context.getSql(config));
        }
        return " ORDER BY " + String.join(",", orderStr);
    }

    private String makeLimit(List<Object> values)
    {
        if (limit == null) return "";
        return " " + limit.getSqlAndValue(config, values);
    }

    private String makeLimit()
    {
        if (limit == null) return "";
        return " " + limit.getSql(config);
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

    public List<PropertyMetaData> getMappingData(AtomicBoolean isSingle)
    {
        List<PropertyMetaData> propertyMetaData = new ArrayList<>();
        MetaData metaData = MetaDataCache.getMetaData(getTargetClass());
        if (isQueried())
        {
            propertyMetaData.addAll(metaData.getColumns().values());
        }
        else
        {
            SqlContext context = getFrom().get(0);
            SqlContext unbox = ExpressionUtil.unBox(context);
            if (unbox instanceof SqlVirtualTableContext)
            {
                SqlVirtualTableContext virtualTableContext = (SqlVirtualTableContext) unbox;
                QuerySqlBuilder sqlBuilder1 = virtualTableContext.getSqlBuilder();
                SqlContext select = sqlBuilder1.getSelect();
                if (select instanceof SqlSelectorContext)
                {
                    SqlSelectorContext sqlSelectorContext = (SqlSelectorContext) select;
                    for (SqlContext sqlContext : sqlSelectorContext.getSqlContexts())
                    {
                        if (sqlContext instanceof SqlAsNameContext)
                        {
                            SqlAsNameContext asNameContext = (SqlAsNameContext) sqlContext;
                            propertyMetaData.add(metaData.getPropertyMetaDataByColumnName(asNameContext.getAsName()));
                        }
                        else if (sqlContext instanceof SqlPropertyContext)
                        {
                            SqlPropertyContext propertyContext = (SqlPropertyContext) sqlContext;
                            propertyMetaData.add(metaData.getPropertyMetaDataByColumnName(propertyContext.getProperty()));
                        }
                        else
                        {
                            throw new RuntimeException();
                        }
                    }
                }
                else
                {
                    isSingle.set(true);

                }
            }
            else if (unbox instanceof SqlRealTableContext)
            {
                propertyMetaData.addAll(metaData.getColumns().values());
            }
            else
            {
                throw new RuntimeException();
            }
        }
        return propertyMetaData;
    }

    public void setTypeSelect()
    {
        List<SqlContext> sqlContextList = new ArrayList<>();
        Class<?> targetClass = getTargetClass();
        if (getGroupBy() != null)
        {
            SqlContext groupBy = getGroupBy();
            MetaData metaData = MetaDataCache.getMetaData(targetClass);
            if (groupBy instanceof SqlGroupContext)
            {
                SqlGroupContext group = (SqlGroupContext) groupBy;
                for (Map.Entry<String, SqlContext> entry : group.getContextMap().entrySet())
                {
                    sqlContextList.add(new SqlAsNameContext(entry.getKey(), entry.getValue()));
                }
            }
            else
            {
                sqlContextList.add(groupBy);
            }
        }
        else if (getOrderedClass().contains(targetClass))
        {
            int index = getOrderedClass().indexOf(targetClass);
            MetaData metaData = MetaDataCache.getMetaData(targetClass);
            for (Map.Entry<String, PropertyMetaData> entry : metaData.getColumns().entrySet())
            {
                sqlContextList.add(new SqlPropertyContext(entry.getValue(), index));
            }
        }
        else
        {
            List<MetaData> metaDataList = MetaDataCache.getMetaData(getOrderedClass());
            MetaData metaData = MetaDataCache.getMetaData(targetClass);
            for (Map.Entry<String, PropertyMetaData> column : metaData.getColumns().entrySet())
            {
                label:
                for (int i = 0; i < metaDataList.size(); i++)
                {
                    for (Map.Entry<String, PropertyMetaData> temp : metaDataList.get(i).getColumns().entrySet())
                    {
                        if (temp.getValue().getColumn().equals(column.getValue().getColumn()))
                        {
                            sqlContextList.add(new SqlPropertyContext(column.getValue(), i));
                            break label;
                        }
                    }
                }
            }
        }
        setSelect(new SqlSelectorContext(sqlContextList));
    }
}
