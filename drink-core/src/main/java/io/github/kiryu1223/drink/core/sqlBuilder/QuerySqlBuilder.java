package io.github.kiryu1223.drink.core.sqlBuilder;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.*;
import io.github.kiryu1223.drink.core.expression.factory.SqlExpressionFactory;

import java.util.List;
import java.util.stream.Collectors;

public class QuerySqlBuilder implements ISqlBuilder
{
    private final Config config;
    private final SqlQueryableExpression queryable;
    private boolean isChanged;

    public QuerySqlBuilder(Config config, Class<?> target, int offset)
    {
        this(config, config.getSqlExpressionFactory().table(target), offset);
    }

    public QuerySqlBuilder(Config config, Class<?> target)
    {
        this(config, config.getSqlExpressionFactory().table(target), 0);
    }

    public QuerySqlBuilder(Config config, SqlTableExpression target)
    {
        this(config, target, 0);
    }

    public QuerySqlBuilder(Config config, SqlTableExpression target, int offset)
    {
        this.config = config;
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        queryable = factory.queryable(factory.from(target, offset));
    }

    public void addWhere(SqlExpression cond)
    {
        queryable.addWhere(cond);
        change();
    }

    public void addJoin(JoinType joinType, SqlTableExpression table, SqlExpression conditions)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        SqlJoinExpression join = factory.join(joinType, table, conditions, queryable.getNextIndex());
        queryable.addJoin(join);
        change();
    }

    public void setGroup(SqlGroupByExpression group)
    {
        queryable.setGroup(group);
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

    public void setSelect(SqlSelectExpression select)
    {
        queryable.setSelect(select);
        change();
    }

    public void setLimit(long offset, long rows)
    {
        queryable.setLimit(offset, rows);
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

    public List<Class<?>> getOrderedClass()
    {
        Class<?> tableClass = queryable.getFrom().getSqlTableExpression().getTableClass();
        List<Class<?>> collect = queryable.getJoins().getJoins().stream().map(j -> j.getJoinTable().getTableClass()).collect(Collectors.toList());
        collect.add(0, tableClass);
        return collect;
    }
}
