package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;

import java.util.ArrayList;
import java.util.List;

public class SqlQueryableExpression extends SqlTableExpression
{
    private SqlSelectExpression select;
    private final SqlFromExpression from;
    private SqlJoinsExpression joins;
    private SqlWhereExpression where;
    private SqlGroupByExpression groupBy;
    private SqlHavingExpression having;
    private SqlOrderByExpression orderBy;
    private SqlLimitExpression limit;

    public SqlQueryableExpression(SqlFromExpression from)
    {
        this.from = from;
        this.select = def();
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        List<String> strings = new ArrayList<>();
        strings.add(select.getSqlAndValue(config, values));
        strings.add(from.getSqlAndValue(config, values));
        if (joins != null)
        {
            strings.add(joins.getSqlAndValue(config, values));
        }
        if (where != null)
        {
            strings.add(where.getSqlAndValue(config, values));
        }
        if (groupBy != null)
        {
            strings.add(groupBy.getSqlAndValue(config, values));
        }
        if (having != null)
        {
            strings.add(having.getSqlAndValue(config, values));
        }
        if (orderBy != null)
        {
            strings.add(orderBy.getSqlAndValue(config, values));
        }
        if (limit != null)
        {
            strings.add(limit.getSqlAndValue(config, values));
        }
        return String.join(" ", strings);
    }

    @Override
    public String getSql(Config config)
    {
        List<String> strings = new ArrayList<>();
        strings.add(select.getSql(config));
        strings.add(from.getSql(config));
        if (joins != null)
        {
            strings.add(joins.getSql(config));
        }
        if (where != null)
        {
            strings.add(where.getSql(config));
        }
        if (groupBy != null)
        {
            strings.add(groupBy.getSql(config));
        }
        if (having != null)
        {
            strings.add(having.getSql(config));
        }
        if (orderBy != null)
        {
            strings.add(orderBy.getSql(config));
        }
        if (limit != null)
        {
            strings.add(limit.getSql(config));
        }
        return String.join(" ", strings);
    }

    @Override
    public Class<?> getTableClass()
    {
        return from.getSqlTableExpression().getTableClass();
    }

    private SqlSelectExpression def()
    {
        MetaData metaData = MetaDataCache.getMetaData(getTableClass());
        List<SqlExpression> sqlExpressions = new ArrayList<>();
        for (PropertyMetaData data : metaData.getNotIgnorePropertys())
        {
            sqlExpressions.add(new SqlColumnExpression(data, 0));
        }
        return new SqlSelectExpression(sqlExpressions);
    }

    public void setWhere(SqlWhereExpression where)
    {
        this.where = where;
    }

    public void setJoins(SqlJoinsExpression joins)
    {
        this.joins = joins;
    }

    public void setGroupBy(SqlGroupByExpression groupBy)
    {
        this.groupBy = groupBy;
    }

    public void setHaving(SqlHavingExpression having)
    {
        this.having = having;
    }

    public void setOrderBy(SqlOrderByExpression orderBy)
    {
        this.orderBy = orderBy;
    }

    public void setSelect(SqlSelectExpression select)
    {
        this.select = select;
    }

    public void setLimit(SqlLimitExpression limit)
    {
        this.limit = limit;
    }
}
