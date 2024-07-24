package io.github.kiryu1223.drink.core.sqlBuilder;

import io.github.kiryu1223.drink.annotation.Navigate;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.*;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;
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
    private final List<SqlPropertyContext> includes = new ArrayList<>();

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

    public void addInclude(SqlPropertyContext include)
    {
        includes.add(include);
        subQueried();
    }

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

//    private void move(List<SqlContext> contexts)
//    {
//        for (SqlContext context : contexts)
//        {
//            if (context instanceof SqlAsTableNameContext)
//            {
//                SqlAsTableNameContext sqlAsTableNameContext = (SqlAsTableNameContext) context;
//                sqlAsTableNameContext.setIndex(sqlAsTableNameContext.getIndex() + 1);
//            }
//            else if (context instanceof SqlPropertyContext)
//            {
//                SqlPropertyContext sqlPropertyContext = (SqlPropertyContext) context;
//                sqlPropertyContext.setTableIndex(sqlPropertyContext.getTableIndex() + 1);
//            }
//        }
//    }

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
        if (select == null)
        {
            setDefaultSelect();
        }

        String sql;
        if (values != null)
        {
            sql = select.getSqlAndValue(config, values);
        }
        else
        {
            sql = select.getSql(config);
        }

        List<String> stringList = new ArrayList<>();
        if (!includes.isEmpty())
        {
            int index = 0;
            String inc = "i";
            for (SqlPropertyContext propertyContext : includes)
            {
                PropertyMetaData propertyMetaData = propertyContext.getPropertyMetaData();
                for (PropertyMetaData notIgnoreColumn : MetaDataCache.getMetaData(propertyMetaData.getNavigateTargetType()).getNotIgnoreColumns())
                {
                    stringList.add(inc + index + "." + config.getDisambiguation().disambiguation(notIgnoreColumn.getColumn()));
                }
                index++;
            }
        }

        return "SELECT " + (distinct ? "DISTINCT " : "") + sql + (stringList.isEmpty() ? "" : "," + String.join(",", stringList));
    }

    private void setDefaultSelect()
    {
        if (groupBy == null)
        {
            MetaData metaData = MetaDataCache.getMetaData(targetClass);
            List<SqlContext> sqlContexts = new ArrayList<>();
            for (PropertyMetaData data : metaData.getNotIgnoreColumns())
            {
                sqlContexts.add(new SqlPropertyContext(data, 0));
            }
            setSelect(new SqlSelectorContext(sqlContexts));
        }
        else
        {
            if (groupBy instanceof SqlGroupContext)
            {
                SqlGroupContext group = (SqlGroupContext) groupBy;
                List<SqlContext> sqlContexts = new ArrayList<>();
                for (Map.Entry<String, SqlContext> entry : group.getContextMap().entrySet())
                {
                    sqlContexts.add(new SqlAsNameContext(entry.getKey(), entry.getValue()));
                }
                setSelect(new SqlSelectorContext(sqlContexts));
            }
            else
            {
                setSelect(groupBy);
            }
        }
    }

    private String makeSelect()
    {
        return makeSelect(null);
    }

//    private String makeSelect()
//    {
//        // return "SELECT " + (distinct ? "DISTINCT " : "") + select.getSql(config);
//        if (select == null)
//        {
//            if (groupBy == null)
//            {
//                MetaData metaData = MetaDataCache.getMetaData(targetClass);
//                List<String> stringList = new ArrayList<>();
//                for (PropertyMetaData data : metaData.getColumns().values())
//                {
//                    stringList.add("t0." + config.getDisambiguation().disambiguation(data.getColumn()));
//                }
//                if (!includes.isEmpty())
//                {
//                    int index = 0;
//                    String inc = "i";
//                    for (SqlPropertyContext propertyContext : includes)
//                    {
//                        PropertyMetaData propertyMetaData = propertyContext.getPropertyMetaData();
//                        Navigate navigate = propertyMetaData.getNavigate();
//                        Field field = propertyMetaData.getField();
//                        Class<?> realType;
//                        if (Collection.class.isAssignableFrom(field.getType()))
//                        {
//                            Type genericType = field.getGenericType();
//                            ParameterizedType type = (ParameterizedType) genericType;
//                            realType = (Class<?>) type.getActualTypeArguments()[0];
//                        }
//                        else
//                        {
//                            realType = field.getType();
//                        }
//
//                        for (PropertyMetaData notIgnoreColumn : MetaDataCache.getMetaData(realType).getNotIgnoreColumns())
//                        {
//                            stringList.add(inc + index + "." + config.getDisambiguation().disambiguation(notIgnoreColumn.getColumn()));
//                        }
//                        index++;
//                    }
//                }
//                return "SELECT " + (distinct ? "DISTINCT " : "") + String.join(",", stringList);
//            }
//            else
//            {
//                if (groupBy instanceof SqlGroupContext)
//                {
//                    SqlGroupContext group = (SqlGroupContext) groupBy;
//                    List<String> stringList = new ArrayList<>();
//                    for (Map.Entry<String, SqlContext> entry : group.getContextMap().entrySet())
//                    {
//                        stringList.add(new SqlAsNameContext(entry.getKey(), entry.getValue()).getSql(config));
//                    }
//                    return "SELECT " + (distinct ? "DISTINCT " : "") + String.join(",", stringList);
//                }
//                else
//                {
//                    return "SELECT " + (distinct ? "DISTINCT " : "") + groupBy.getSql(config);
//                }
//            }
//        }
//        else
//        {
//            return "SELECT " + (distinct ? "DISTINCT " : "") + select.getSql(config);
//        }
//    }

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
        if (joins.isEmpty() && includes.isEmpty()) return "";
        List<String> joinStr = new ArrayList<>(joins.size());
        for (SqlContext context : joins)
        {
            joinStr.add(context.getSqlAndValue(config, values));
        }
        makeInclude(joinStr, values);
        return " " + String.join(" ", joinStr);
    }

    private String makeJoin()
    {
        if (joins.isEmpty() && includes.isEmpty()) return "";
        List<String> joinStr = new ArrayList<>(joins.size());
        for (SqlContext context : joins)
        {
            joinStr.add(context.getSql(config));
        }
        makeInclude(joinStr, null);
        return " " + String.join(" ", joinStr);
    }

    private void makeInclude(List<String> joinStr, List<Object> values)
    {
        int index = 0;
        String inc = "i";
        for (SqlPropertyContext propertyContext : includes)
        {
            PropertyMetaData propertyMetaData = propertyContext.getPropertyMetaData();
            Navigate navigate = propertyMetaData.getNavigate();
            Class<?> navigateTargetType =propertyMetaData.getNavigateTargetType();
            MetaData thisMetaData = MetaDataCache.getMetaData(propertyMetaData.getParentType());
            MetaData thatMetaData = MetaDataCache.getMetaData(navigateTargetType);
            PropertyMetaData self = thisMetaData.getPropertyMetaData(navigate.self());
            PropertyMetaData target = thatMetaData.getPropertyMetaData(navigate.target());
            SqlPropertyContext left = new SqlPropertyContext(self, 0);
            SqlPropertyContext right = new SqlPropertyContext(target, index, inc);
            SqlJoinContext joinContext = new SqlJoinContext(
                    JoinType.LEFT,
                    new SqlAsTableNameContext(index, new SqlRealTableContext(navigateTargetType), inc),
                    new SqlBinaryContext(SqlOperator.EQ, left, right)
            );
            if (values == null)
            {
                joinStr.add(joinContext.getSql(config));
            }
            else
            {
                joinStr.add(joinContext.getSqlAndValue(config, values));
            }
            index++;
        }
    }

    private String makeWhere(List<Object> values)
    {
        if (wheres.isEmpty()) return "";
        List<String> whereStr = new ArrayList<>(wheres.size());
        for (SqlContext context : wheres)
        {
            sqlOrSqlAndValue(values, whereStr, context);
        }
        return " WHERE " + String.join(" AND ", whereStr);
    }

    private String makeWhere()
    {
        return makeWhere(null);
    }

    private String makeGroup(List<Object> values)
    {
        if (groupBy == null) return "";
        if (values != null)
        {
            return " GROUP BY " + groupBy.getSqlAndValue(config, values);
        }
        else
        {
            return " GROUP BY " + groupBy.getSql(config);
        }
    }

    private String makeGroup()
    {
        return makeGroup(null);
    }

    private String makeHaving(List<Object> values)
    {
        if (havings.isEmpty()) return "";
        List<String> havingStr = new ArrayList<>(havings.size());
        for (SqlContext context : havings)
        {
            sqlOrSqlAndValue(values, havingStr, context);
        }
        return " HAVING " + String.join(" AND ", havingStr);
    }

    private String makeHaving()
    {
        return makeHaving(null);
    }

    private String makeOrder(List<Object> values)
    {
        if (orderBys.isEmpty()) return "";
        List<String> orderStr = new ArrayList<>(orderBys.size());
        for (SqlContext context : orderBys)
        {
            sqlOrSqlAndValue(values, orderStr, context);
        }
        return " ORDER BY " + String.join(",", orderStr);
    }

    private String makeOrder()
    {
        return makeOrder(null);
    }

    private String makeLimit(List<Object> values)
    {
        if (limit == null) return "";
        if (values != null)
        {
            return " " + limit.getSqlAndValue(config, values);
        }
        else
        {
            return " " + limit.getSql(config);
        }
    }

    private String makeLimit()
    {
        return makeLimit(null);
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
                if (entry.getValue().isIgnoreColumn()) continue;
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
        if (!includes.isEmpty())
        {
            int index = 0;
            String inc = "i";
            for (SqlPropertyContext propertyContext : includes)
            {
                PropertyMetaData propertyMetaData = propertyContext.getPropertyMetaData();
                for (PropertyMetaData notIgnoreColumn : MetaDataCache.getMetaData(propertyMetaData.getNavigateTargetType()).getNotIgnoreColumns())
                {
                    sqlContextList.add(new SqlPropertyContext(notIgnoreColumn,index,inc));
                }
                index++;
            }
        }
        setSelect(new SqlSelectorContext(sqlContextList));
    }

    private void sqlOrSqlAndValue(List<Object> values, List<String> strings, SqlContext context)
    {
        if (values != null)
        {
            strings.add(context.getSqlAndValue(config, values));
        }
        else
        {
            strings.add(context.getSql(config));
        }
    }
}
