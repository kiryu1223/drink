package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.factory.SqlExpressionFactory;

import java.util.ArrayList;
import java.util.List;

public class SqlQueryableExpression extends SqlTableExpression
{
    private final SqlSelectExpression select;
    private final SqlFromExpression from;
    private final SqlJoinsExpression joins;
    private final SqlWhereExpression where;
    private final SqlGroupByExpression groupBy;
    private final SqlHavingExpression having;
    private final SqlOrderByExpression orderBy;
    private final SqlLimitExpression limit;
    private boolean distinct = false;

    public SqlQueryableExpression(Config config, SqlFromExpression from)
    {
        this.from = from;
        SqlExpressionFactory sqlExpressionFactory = config.getSqlExpressionFactory();
        this.select = sqlExpressionFactory.select(getTableClass());
        this.joins = sqlExpressionFactory.Joins(new ArrayList<>());
        this.where = sqlExpressionFactory.where(sqlExpressionFactory.Condition(new ArrayList<>()));
        this.groupBy = sqlExpressionFactory.groupBy(new ArrayList<>());
        this.having = sqlExpressionFactory.having(sqlExpressionFactory.Condition(new ArrayList<>()));
        this.orderBy = sqlExpressionFactory.orderBy(new ArrayList<>());
        this.limit = sqlExpressionFactory.limit();
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        List<String> strings = new ArrayList<>();
        strings.add(select.getSqlAndValue(config, values));
        if (distinct)
        {
            strings.add("DISTINCT");
        }
        strings.add(from.getSqlAndValue(config, values));
        strings.add(joins.getSqlAndValue(config, values));
        strings.add(where.getSqlAndValue(config, values));
        strings.add(groupBy.getSqlAndValue(config, values));
        strings.add(having.getSqlAndValue(config, values));
        strings.add(orderBy.getSqlAndValue(config, values));
        strings.add(limit.getSqlAndValue(config, values));
        return String.join(" ", strings);
    }

    @Override
    public String getSql(Config config)
    {
        List<String> strings = new ArrayList<>();
        strings.add(select.getSql(config));
        if (distinct)
        {
            strings.add("DISTINCT");
        }
        strings.add(from.getSql(config));
        strings.add(joins.getSql(config));
        strings.add(where.getSql(config));
        strings.add(groupBy.getSql(config));
        strings.add(having.getSql(config));
        strings.add(orderBy.getSql(config));
        strings.add(limit.getSql(config));
        return String.join(" ", strings);
    }

    @Override
    public Class<?> getTableClass()
    {
        return from.getSqlTableExpression().getTableClass();
    }

    public void addWhere(SqlExpression cond)
    {
        where.addCond(cond);
    }

    public void addJoin(SqlJoinExpression join)
    {
        joins.addJoin(join);
    }

    public void addGroup(SqlColumnExpression column)
    {
        groupBy.addColumn(column);
    }

    public void addHaving(SqlExpression cond)
    {
        having.addCond(cond);
    }

    public void addOrder(SqlOrderExpression order)
    {
        orderBy.addOrder(order);
    }

    public void setSelect(List<SqlExpression> columns, Class<?> target, boolean isSingle)
    {
        select.setColumns(columns);
        select.setTarget(target);
        select.setSingle(isSingle);
    }

    public void setLimit(long offset, long rows)
    {
        limit.setOffset(offset);
        limit.setRows(rows);
    }

    public void setLimit(long rows)
    {
        limit.setRows(rows);
    }

    public void setDistinct(boolean distinct)
    {
        this.distinct = distinct;
    }

    public SqlFromExpression getFrom()
    {
        return from;
    }

    public int getNextIndex()
    {
        return 1 + joins.getJoins().size();
    }
}
