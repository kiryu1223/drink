package io.github.kiryu1223.drink.api.crud.builder;

import io.github.kiryu1223.drink.core.context.SqlContext;

import java.util.ArrayList;
import java.util.List;

public class QuerySqlBuilder implements ISqlBuilder
{
    private SqlContext selects;
    private final List<SqlContext> joins = new ArrayList<>();
    private final List<SqlContext> where = new ArrayList<>();
    private SqlContext groupBy;
    private final List<SqlContext> havings = new ArrayList<>();
    private final List<SqlContext> orderBys = new ArrayList<>();
    private long offset = 0, rows = 0;
    private final Class<?> queryClass;
    private final List<Class<?>> joinClass=new ArrayList<>();

    public QuerySqlBuilder(Class<?> queryClass)
    {
        this.queryClass = queryClass;
    }

    public SqlContext getSelects()
    {
        return selects;
    }

    public void setSelects(SqlContext selects)
    {
        this.selects = selects;
    }

    public List<SqlContext> getJoins()
    {
        return joins;
    }

    public List<SqlContext> getWhere()
    {
        return where;
    }

    public SqlContext getGroupBy()
    {
        return groupBy;
    }

    public void setGroupBy(SqlContext groupBy)
    {
        this.groupBy = groupBy;
    }

    public List<SqlContext> getHavings()
    {
        return havings;
    }

    public List<SqlContext> getOrderBys()
    {
        return orderBys;
    }

    public long getOffset()
    {
        return offset;
    }

    public void setOffset(long offset)
    {
        this.offset = offset;
    }

    public long getRows()
    {
        return rows;
    }

    public void setRows(long rows)
    {
        this.rows = rows;
    }

    @Override
    public String getSql()
    {
        return null;
    }

    public Class<?> getQueryClass()
    {
        return queryClass;
    }

    public List<Class<?>> getJoinClass()
    {
        return joinClass;
    }
}
