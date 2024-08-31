package io.github.kiryu1223.drink.core.sqlBuilder;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.JoinType;
import io.github.kiryu1223.drink.core.expression.*;
import io.github.kiryu1223.drink.core.expression.factory.SqlExpressionFactory;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

import java.util.List;

public class QuerySqlBuilder implements ISqlBuilder
{
    private final Config config;
    private final SqlQueryableExpression queryable;
    private boolean isChanged;

    public QuerySqlBuilder(Config config, Class<?> target)
    {
        this.config = config;
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        queryable = factory.queryable(target);
    }

    public QuerySqlBuilder(Config config, SqlTableExpression target)
    {
        this.config = config;
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        queryable = factory.queryable(factory.from(target));
    }

    public void addWhere(SqlExpression cond)
    {
        queryable.addWhere(cond);
        change();
    }

    public void addJoin(JoinType joinType, Class<?> target, SqlConditionsExpression conditions)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        SqlJoinExpression join = factory.join(joinType, factory.table(target), conditions, queryable.getNextIndex());
        queryable.addJoin(join);
        change();
    }

    public void addGroup(SqlColumnExpression column)
    {
        queryable.addGroup(column);
        change();
    }

    public void addHaving(SqlExpression cond)
    {
        queryable.addHaving(cond);
        change();
    }

    public void addOrder(SqlOrderExpression order)
    {
        queryable.addOrder(order);
        change();
    }

    public void setSelect(List<SqlExpression> columns, Class<?> target, boolean isSingle)
    {
        queryable.setSelect(columns, target, isSingle);
        change();
    }

    public void setLimit(long offset, long rows)
    {
        queryable.setLimit(offset,rows);
        change();
    }

    public void setLimit(long rows)
    {
        queryable.setLimit(rows);
        change();
    }

    public void setDistinct(boolean distinct)
    {
        queryable.setDistinct(distinct);
        change();
    }

    private void change()
    {
        isChanged = true;
    }

    @Override
    public Config getConfig()
    {
        return config;
    }

    public SqlQueryableExpression getQueryable()
    {
        return queryable;
    }

    @Override
    public String getSql()
    {
        if (isChanged)
        {
            return queryable.getSql(config);
        }
        else
        {
            SqlTableExpression sqlTableExpression = queryable.getFrom().getSqlTableExpression();
            if (sqlTableExpression instanceof SqlRealTableExpression)
            {
                return queryable.getSql(config);
            }
            return sqlTableExpression.getSql(config);
        }
    }

    @Override
    public String getSqlAndValue(List<Object> values)
    {
        if (isChanged)
        {
            return queryable.getSqlAndValue(config, values);
        }
        else
        {
            SqlTableExpression sqlTableExpression = queryable.getFrom().getSqlTableExpression();
            if (sqlTableExpression instanceof SqlRealTableExpression)
            {
                return queryable.getSqlAndValue(config, values);
            }
            return sqlTableExpression.getSqlAndValue(config, values);
        }
    }

    //    private final Config config;
//    private SqlContext select;
//    private boolean distinct = false;
//    private final SqlAsTableNameContext from;
//    private final List<SqlContext> joins = new ArrayList<>();
//    private final List<SqlContext> wheres = new ArrayList<>();
//    private SqlContext groupBy;
//    private final List<SqlContext> havings = new ArrayList<>();
//    private final List<SqlContext> orderBys = new ArrayList<>();
//    private SqlLimitContext limit;
//    private final List<Class<?>> orderedClass = new ArrayList<>();
//    private Class<?> targetClass;
//    private int startIndex;
//    private boolean changed;
//
//    private void change()
//    {
//        changed = true;
//    }
//
//    public QuerySqlBuilder(Config config, SqlTableContext tableContext)
//    {
//        this(config, tableContext, 0);
//    }
//
//    public QuerySqlBuilder(Config config, SqlTableContext tableContext, int startIndex)
//    {
//        this.config = config;
//        this.startIndex = startIndex;
//        if (tableContext instanceof SqlRealTableContext)
//        {
//            this.from = new SqlAsTableNameContext(startIndex, tableContext);
//        }
//        else
//        {
//            this.from = new SqlAsTableNameContext(startIndex, new SqlParensContext(tableContext));
//        }
//        orderedClass.add(tableContext.getTableClass());
//        this.targetClass = tableContext.getTableClass();
//        this.select = getSelectByClass(targetClass);
//    }
//
//    private SqlContext getSelectByClass(Class<?> targetClass)
//    {
//        if (groupBy == null)
//        {
//            MetaData metaData = MetaDataCache.getMetaData(targetClass);
//            List<SqlContext> sqlContexts = new ArrayList<>();
//            for (PropertyMetaData data : metaData.getNotIgnorePropertys())
//            {
//                sqlContexts.add(new SqlPropertyContext(data, startIndex));
//            }
//            return new SqlSelectorContext(sqlContexts);
//            //setSelect(new SqlSelectorContext(sqlContexts), targetClass);
//        }
//        else
//        {
//            if (groupBy instanceof SqlGroupContext)
//            {
//                SqlGroupContext group = (SqlGroupContext) groupBy;
//                List<SqlContext> sqlContexts = new ArrayList<>();
//                for (Map.Entry<String, SqlContext> entry : group.getContextMap().entrySet())
//                {
//                    sqlContexts.add(new SqlAsNameContext(entry.getKey(), entry.getValue()));
//                }
//                return new SqlSelectorContext(sqlContexts);
//                //setSelect(new SqlSelectorContext(sqlContexts), targetClass);
//            }
//            else
//            {
//                return groupBy;
//                //setSelect(groupBy, targetClass);
//            }
//        }
//    }
//
//    public Class<?> getTargetClass()
//    {
//        return targetClass;
//    }
//
//    public List<Class<?>> getOrderedClass()
//    {
//        return orderedClass;
//    }
//
////    public void setSelect(SqlContext select)
////    {
////        this.select = select;
////        change();
////    }
//
//    public void setSelect(SqlContext select, Class<?> targetClass)
//    {
//        SqlContext temp = select;
//        if (temp == null)
//        {
//            temp = getSelectByClass(targetClass);
//        }
//        this.select = temp;
//        this.targetClass = targetClass;
//        change();
//    }
//
//    public void setSelect(Class<?> targetClass)
//    {
//        setSelect(null, targetClass);
//    }
//
////    public void addFrom(Class<?> queryClass)
////    {
////        SqlTableContext sqlTableContext = new SqlRealTableContext(queryClass);
////        from.add(new SqlAsTableNameContext(from.size() + moveCount, sqlTableContext));
////        orderedClass.add(queryClass);
////        if (targetClass == null)
////        {
////            targetClass = queryClass;
////        }
////    }
////
////    public void addFrom(Class<?>... queryClasses)
////    {
////        for (Class<?> queryClass : queryClasses)
////        {
////            addFrom(queryClass);
////        }
////    }
////
////    public void addFrom(QuerySqlBuilder sqlBuilder)
////    {
////        SqlTableContext sqlTableContext = new SqlVirtualTableContext(sqlBuilder);
////        SqlParensContext sqlParensContext = new SqlParensContext(sqlTableContext);
////        from.add(new SqlAsTableNameContext(from.size() + moveCount, sqlParensContext));
////        orderedClass.addAll(sqlBuilder.orderedClass);
////        if (targetClass == null)
////        {
////            targetClass = sqlBuilder.targetClass;
////        }
////    }
////
////    public void addFrom(QuerySqlBuilder... sqlBuilders)
////    {
////        for (QuerySqlBuilder sqlBuilder : sqlBuilders)
////        {
////            addFrom(sqlBuilder);
////        }
////    }
//
//    public void addJoin(JoinType joinType, SqlTableContext tableContext, SqlContext onContext)
//    {
//        SqlJoinContext joinContext = new SqlJoinContext(
//                joinType,
//                new SqlAsTableNameContext(startIndex + 1 + joins.size(),
//                        tableContext instanceof SqlRealTableContext
//                                ? tableContext :
//                                new SqlParensContext(tableContext)),
//                onContext
//        );
//        joins.add(joinContext);
//        orderedClass.add(tableContext.getTableClass());
//        change();
//    }
//
//    public void addWhere(SqlContext where)
//    {
//        wheres.add(where);
//        change();
//    }
//
//    public void addWhere(Collection<SqlContext> where)
//    {
//        wheres.addAll(where);
//        change();
//    }
//
//    public void addOrWhere(SqlContext where)
//    {
//        removeAndBoxOr(wheres, where);
//        change();
//    }
//
//    public void setGroupBy(SqlContext groupBy, Class<?> targetClass)
//    {
//        this.groupBy = groupBy;
//        setSelect(targetClass);
//        change();
//    }
//
//    public void addHaving(SqlContext having)
//    {
//        havings.add(having);
//        change();
//    }
//
//    public void addOrderBy(SqlContext orderBy)
//    {
//        orderBys.add(orderBy);
//        change();
//    }
//
//    public void addOrderBy(Collection<SqlContext> orderBy)
//    {
//        orderBys.addAll(orderBy);
//        change();
//    }
//
//    public void setLimit(SqlLimitContext limit)
//    {
//        this.limit = limit;
//        change();
//    }
//
//    public void setDistinct(boolean distinct)
//    {
//        this.distinct = distinct;
//        change();
//    }
//
//    public List<SqlContext> getWheres()
//    {
//        return wheres;
//    }
//
//    public List<SqlContext> getOrderBys()
//    {
//        return orderBys;
//    }
//
//    public SqlLimitContext getLimit()
//    {
//        return limit;
//    }
//
//    public SqlContext getGroupBy()
//    {
//        return groupBy;
//    }
//
//    public Config getConfig()
//    {
//        return config;
//    }
//
//    private SqlTableContext unbox(SqlContext context)
//    {
//        if (context instanceof SqlTableContext)
//        {
//            return (SqlTableContext) context;
//        }
//        else if (context instanceof SqlAsTableNameContext)
//        {
//            SqlAsTableNameContext sqlAsTableNameContext = (SqlAsTableNameContext) context;
//            return unbox(sqlAsTableNameContext.getContext());
//        }
//        else if (context instanceof SqlParensContext)
//        {
//            SqlParensContext parensContext = (SqlParensContext) context;
//            return unbox(parensContext.getContext());
//        }
//        else
//        {
//            throw new RuntimeException("Unsupported sql context type: " + context.getClass().getName());
//        }
//    }
//
//    private boolean needUnbox()
//    {
//        return !changed;
//    }
//
//    @Override
//    public String getSql()
//    {
//        if (needUnbox())
//        {
//            return unbox(from).getSql(config);
//        }
//        else
//        {
//            return makeSelect() +
//                    makeFrom() +
//                    makeJoin() +
//                    makeWhere() +
//                    makeGroup() +
//                    makeHaving() +
//                    makeOrder() +
//                    makeLimit();
//        }
//    }
//
//    @Override
//    public String getSqlAndValue(List<Object> values)
//    {
//        if (needUnbox())
//        {
//            return unbox(from).getSqlAndValue(config, values);
//        }
//        else
//        {
//            return makeSelect(values) +
//                    makeFrom(values) +
//                    makeJoin(values) +
//                    makeWhere(values) +
//                    makeGroup(values) +
//                    makeHaving(values) +
//                    makeOrder(values) +
//                    makeLimit(values);
//        }
//    }
//
//    protected String makeSelect(List<Object> values)
//    {
//        String sql;
//        if (values != null)
//        {
//            sql = select.getSqlAndValue(config, values);
//        }
//        else
//        {
//            sql = select.getSql(config);
//        }
//        return "SELECT " + (distinct ? "DISTINCT " : "") + sql;
//    }
//
//    protected String makeSelect()
//    {
//        return makeSelect(null);
//    }
//
//
//    protected String makeFrom(List<Object> values)
//    {
//        List<String> froms = new ArrayList<>(1);
//        sqlOrSqlAndValue(values, froms, from);
//        return " FROM " + String.join(",", froms);
//    }
//
//    protected String makeFrom()
//    {
//        return makeFrom(null);
//    }
//
//    protected String makeJoin(List<Object> values)
//    {
//        if (joins.isEmpty()) return "";
//        List<String> joinStr = new ArrayList<>(joins.size());
//        for (SqlContext context : joins)
//        {
//            sqlOrSqlAndValue(values, joinStr, context);
//        }
//        return " " + String.join(" ", joinStr);
//    }
//
//    protected String makeJoin()
//    {
//        return makeJoin(null);
//    }
//
////    private void makeInclude(List<String> joinStr, List<Object> values)
////    {
////        int index = 0;
////        String inc = "i";
////        for (SqlPropertyContext propertyContext : includes)
////        {
////            PropertyMetaData propertyMetaData = propertyContext.getPropertyMetaData();
////            Navigate navigate = propertyMetaData.getNavigate();
////            Class<?> navigateTargetType =propertyMetaData.getNavigateTargetType();
////            MetaData thisMetaData = MetaDataCache.getMetaData(propertyMetaData.getParentType());
////            MetaData thatMetaData = MetaDataCache.getMetaData(navigateTargetType);
////            PropertyMetaData self = thisMetaData.getPropertyMetaData(navigate.self());
////            PropertyMetaData target = thatMetaData.getPropertyMetaData(navigate.target());
////            SqlPropertyContext left = new SqlPropertyContext(self, 0);
////            SqlPropertyContext right = new SqlPropertyContext(target, index, inc);
////            SqlJoinContext joinContext = new SqlJoinContext(
////                    JoinType.LEFT,
////                    new SqlAsTableNameContext(index, new SqlRealTableContext(navigateTargetType), inc),
////                    new SqlBinaryContext(SqlOperator.EQ, left, right)
////            );
////            if (values == null)
////            {
////                joinStr.add(joinContext.getSql(config));
////            }
////            else
////            {
////                joinStr.add(joinContext.getSqlAndValue(config, values));
////            }
////            index++;
////        }
////    }
//
//    protected String makeWhere(List<Object> values)
//    {
//        if (wheres.isEmpty()) return "";
//        List<String> whereStr = new ArrayList<>(wheres.size());
//        for (SqlContext context : wheres)
//        {
//            sqlOrSqlAndValue(values, whereStr, context);
//        }
//        return " WHERE " + String.join(" AND ", whereStr);
//    }
//
//    protected String makeWhere()
//    {
//        return makeWhere(null);
//    }
//
//    protected String makeGroup(List<Object> values)
//    {
//        if (groupBy == null) return "";
//        if (values != null)
//        {
//            return " GROUP BY " + groupBy.getSqlAndValue(config, values);
//        }
//        else
//        {
//            return " GROUP BY " + groupBy.getSql(config);
//        }
//    }
//
//    protected String makeGroup()
//    {
//        return makeGroup(null);
//    }
//
//    protected String makeHaving(List<Object> values)
//    {
//        if (havings.isEmpty()) return "";
//        List<String> havingStr = new ArrayList<>(havings.size());
//        for (SqlContext context : havings)
//        {
//            sqlOrSqlAndValue(values, havingStr, context);
//        }
//        return " HAVING " + String.join(" AND ", havingStr);
//    }
//
//    protected String makeHaving()
//    {
//        return makeHaving(null);
//    }
//
//    protected String makeOrder(List<Object> values)
//    {
//        if (orderBys.isEmpty()) return "";
//        List<String> orderStr = new ArrayList<>(orderBys.size());
//        for (SqlContext context : orderBys)
//        {
//            sqlOrSqlAndValue(values, orderStr, context);
//        }
//        return " ORDER BY " + String.join(",", orderStr);
//    }
//
//    protected String makeOrder()
//    {
//        return makeOrder(null);
//    }
//
//    protected String makeLimit(List<Object> values)
//    {
//        if (limit == null) return "";
//        if (values != null)
//        {
//            return " " + limit.getSqlAndValue(config, values);
//        }
//        else
//        {
//            return " " + limit.getSql(config);
//        }
//    }
//
//    protected String makeLimit()
//    {
//        return makeLimit(null);
//    }
//
//    private void removeAndBoxOr(List<SqlContext> contexts, SqlContext right)
//    {
//        if (contexts.isEmpty())
//        {
//            contexts.add(right);
//        }
//        else
//        {
//            SqlContext left = contexts.remove(contexts.size() - 1);
//            contexts.add(new SqlBinaryContext(SqlOperator.OR, left, right));
//        }
//    }
//
//    public List<PropertyMetaData> getMappingData(AtomicBoolean isSingle)
//    {
//        List<PropertyMetaData> props;
//        if (select instanceof SqlSelectorContext)
//        {
//            MetaData metaData = MetaDataCache.getMetaData(targetClass);
//            SqlSelectorContext sqlSelectorContext = (SqlSelectorContext) select;
//            props = new ArrayList<>(sqlSelectorContext.getSqlContexts().size());
//            for (SqlContext sqlContext : sqlSelectorContext.getSqlContexts())
//            {
//                if (sqlContext instanceof SqlAsNameContext)
//                {
//                    SqlAsNameContext asNameContext = (SqlAsNameContext) sqlContext;
//                    PropertyMetaData propertyMetaData = metaData.getPropertyMetaData(asNameContext.getAsName());
//                    props.add(propertyMetaData);
//                }
//                else if (sqlContext instanceof SqlPropertyContext)
//                {
//                    SqlPropertyContext propertyContext = (SqlPropertyContext) sqlContext;
//                    props.add(propertyContext.getPropertyMetaData());
//                }
//            }
//        }
//        else
//        {
//            props = Collections.emptyList();
//            if (isSingle != null)
//            {
//                isSingle.set(true);
//            }
//        }
//        return props;
//    }
//
//    private void sqlOrSqlAndValue(List<Object> values, List<String> strings, SqlContext context)
//    {
//        if (values != null)
//        {
//            strings.add(context.getSqlAndValue(config, values));
//        }
//        else
//        {
//            strings.add(context.getSql(config));
//        }
//    }
//
//    public SqlContext getSelect()
//    {
//        return select;
//    }
//
//    private final List<IncludeSet> includes = new ArrayList<>();
//
//    public List<IncludeSet> getIncludes()
//    {
//        return includes;
//    }
//
//    public IncludeSet getLastInclude()
//    {
//        return includes.get(includes.size() - 1);
//    }
}
