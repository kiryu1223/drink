package io.github.kiryu1223.drink.core.sqlBuilder;

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
    private boolean distinct = false;
    private final SqlContext from;
    private final List<SqlContext> joins = new ArrayList<>();
    private final List<SqlContext> wheres = new ArrayList<>();
    private SqlContext groupBy;
    private final List<SqlContext> havings = new ArrayList<>();
    private final List<SqlContext> orderBys = new ArrayList<>();
    private SqlContext limit;
    private final List<Class<?>> orderedClass = new ArrayList<>();
    private Class<?> targetClass;
    //private int startIndex = 0;

    public QuerySqlBuilder(Config config, SqlTableContext tableContext)
    {
        this(config, tableContext, 0);
    }

    public QuerySqlBuilder(Config config, SqlTableContext tableContext, int startIndex)
    {
        this.config = config;
        if (tableContext instanceof SqlRealTableContext)
        {
            this.from = new SqlAsTableNameContext(startIndex, tableContext);
        }
        else
        {
            this.from = new SqlAsTableNameContext(startIndex, new SqlParensContext(tableContext));
        }
        setTargetClass(tableContext.getTableClass());
        orderedClass.add(tableContext.getTableClass());
    }

    public void setTargetClass(Class<?> targetClass)
    {
        setDefaultSelect(targetClass);
    }

    private void setDefaultSelect(Class<?> targetClass)
    {
        if (groupBy == null)
        {
            MetaData metaData = MetaDataCache.getMetaData(targetClass);
            List<SqlContext> sqlContexts = new ArrayList<>();
            for (PropertyMetaData data : metaData.getNotIgnoreColumns())
            {
                sqlContexts.add(new SqlPropertyContext(data, 0));
            }
            setSelect(new SqlSelectorContext(sqlContexts), targetClass);
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
                setSelect(new SqlSelectorContext(sqlContexts), targetClass);
            }
            else
            {
                setSelect(groupBy, targetClass);
            }
        }
    }

    public Class<?> getTargetClass()
    {
        return targetClass;
    }

    public List<Class<?>> getOrderedClass()
    {
        return orderedClass;
    }

    public void setSelect(SqlContext select, Class<?> targetClass)
    {
        this.select = select;
        this.targetClass = targetClass;
    }

//    public void addFrom(Class<?> queryClass)
//    {
//        SqlTableContext sqlTableContext = new SqlRealTableContext(queryClass);
//        from.add(new SqlAsTableNameContext(from.size() + moveCount, sqlTableContext));
//        orderedClass.add(queryClass);
//        if (targetClass == null)
//        {
//            targetClass = queryClass;
//        }
//    }
//
//    public void addFrom(Class<?>... queryClasses)
//    {
//        for (Class<?> queryClass : queryClasses)
//        {
//            addFrom(queryClass);
//        }
//    }
//
//    public void addFrom(QuerySqlBuilder sqlBuilder)
//    {
//        SqlTableContext sqlTableContext = new SqlVirtualTableContext(sqlBuilder);
//        SqlParensContext sqlParensContext = new SqlParensContext(sqlTableContext);
//        from.add(new SqlAsTableNameContext(from.size() + moveCount, sqlParensContext));
//        orderedClass.addAll(sqlBuilder.orderedClass);
//        if (targetClass == null)
//        {
//            targetClass = sqlBuilder.targetClass;
//        }
//    }
//
//    public void addFrom(QuerySqlBuilder... sqlBuilders)
//    {
//        for (QuerySqlBuilder sqlBuilder : sqlBuilders)
//        {
//            addFrom(sqlBuilder);
//        }
//    }

    public void addJoin(Class<?> target, JoinType joinType, SqlTableContext tableContext, SqlContext onContext)
    {
        SqlJoinContext joinContext = new SqlJoinContext(
                joinType,
                new SqlAsTableNameContext(1 + joins.size(),
                        tableContext instanceof SqlRealTableContext
                                ? tableContext :
                                new SqlParensContext(tableContext)),
                onContext
        );
        joins.add(joinContext);
        orderedClass.add(target);
    }

    public void addWhere(SqlContext where)
    {
        wheres.add(where);
    }

    public void addOrWhere(SqlContext where)
    {
        removeAndBoxOr(wheres, where);
    }

    public void setGroupBy(SqlContext groupBy, Class<?> targetClass)
    {
        this.groupBy = groupBy;
        setTargetClass(targetClass);
    }

    public void addHaving(SqlContext having)
    {
        havings.add(having);

    }

    public void addOrderBy(SqlContext orderBy)
    {
        orderBys.add(orderBy);

    }

    public void setLimit(SqlContext limit)
    {
        this.limit = limit;
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

    @Override
    public String getSql()
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

    @Override
    public String getSqlAndValue(List<Object> values)
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

    private String makeSelect(List<Object> values)
    {
        String sql;
        if (values != null)
        {
            sql = select.getSqlAndValue(config, values);
        }
        else
        {
            sql = select.getSql(config);
        }
        return "SELECT " + (distinct ? "DISTINCT " : "") + sql;
    }

    private String makeSelect()
    {
        return makeSelect(null);
    }


    private String makeFrom(List<Object> values)
    {
        List<String> froms = new ArrayList<>(1);
        sqlOrSqlAndValue(values, froms, from);
        return " FROM " + String.join(",", froms);
    }

    private String makeFrom()
    {
        return makeFrom(null);
    }

    private String makeJoin(List<Object> values)
    {
        if (joins.isEmpty()) return "";
        List<String> joinStr = new ArrayList<>(joins.size());
        for (SqlContext context : joins)
        {
            sqlOrSqlAndValue(values, joinStr, context);
        }
        return " " + String.join(" ", joinStr);
    }

    private String makeJoin()
    {
        return makeJoin(null);
    }

//    private void makeInclude(List<String> joinStr, List<Object> values)
//    {
//        int index = 0;
//        String inc = "i";
//        for (SqlPropertyContext propertyContext : includes)
//        {
//            PropertyMetaData propertyMetaData = propertyContext.getPropertyMetaData();
//            Navigate navigate = propertyMetaData.getNavigate();
//            Class<?> navigateTargetType =propertyMetaData.getNavigateTargetType();
//            MetaData thisMetaData = MetaDataCache.getMetaData(propertyMetaData.getParentType());
//            MetaData thatMetaData = MetaDataCache.getMetaData(navigateTargetType);
//            PropertyMetaData self = thisMetaData.getPropertyMetaData(navigate.self());
//            PropertyMetaData target = thatMetaData.getPropertyMetaData(navigate.target());
//            SqlPropertyContext left = new SqlPropertyContext(self, 0);
//            SqlPropertyContext right = new SqlPropertyContext(target, index, inc);
//            SqlJoinContext joinContext = new SqlJoinContext(
//                    JoinType.LEFT,
//                    new SqlAsTableNameContext(index, new SqlRealTableContext(navigateTargetType), inc),
//                    new SqlBinaryContext(SqlOperator.EQ, left, right)
//            );
//            if (values == null)
//            {
//                joinStr.add(joinContext.getSql(config));
//            }
//            else
//            {
//                joinStr.add(joinContext.getSqlAndValue(config, values));
//            }
//            index++;
//        }
//    }

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

    public List<PropertyMetaData> getMappingData()
    {
        MetaData metaData = MetaDataCache.getMetaData(getTargetClass());
        return new ArrayList<>(metaData.getNotIgnoreColumns());
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
