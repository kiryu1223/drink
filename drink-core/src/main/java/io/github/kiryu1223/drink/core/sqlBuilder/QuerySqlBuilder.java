package io.github.kiryu1223.drink.core.sqlBuilder;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.builder.IncludeSet;
import io.github.kiryu1223.drink.core.expression.*;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;

import java.util.ArrayList;
import java.util.List;

public class QuerySqlBuilder implements ISqlBuilder
{
    private final Config config;
    private final SqlQueryableExpression queryable;
    private final List<IncludeSet> includeSets = new ArrayList<>();
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
        SqlJoinExpression join = factory.join(joinType, table, conditions, queryable.getOrderedCount());
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

    public void setSelect(Class<?> c)
    {
        List<Class<?>> orderedClass = getOrderedClass();
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        MetaData metaData = MetaDataCache.getMetaData(c);
        List<SqlExpression> expressions = new ArrayList<>();
        if (orderedClass.contains(c))
        {
            int index = orderedClass.indexOf(c);
            for (PropertyMetaData notIgnoreProperty : metaData.getNotIgnorePropertys())
            {
                expressions.add(factory.column(notIgnoreProperty, index));
            }
        }
        else
        {
            for (PropertyMetaData sel : metaData.getNotIgnorePropertys())
            {
                int index = 0;
                GOTO:
                for (MetaData data : MetaDataCache.getMetaData(getOrderedClass()))
                {
                    for (PropertyMetaData noi : data.getNotIgnorePropertys())
                    {
                        if (noi.getColumn().equals(sel.getColumn()) && noi.getType().equals(sel.getType()))
                        {
                            expressions.add(factory.column(sel, index));
                            break GOTO;
                        }
                    }
                    index++;
                }
            }
        }
        queryable.setSelect(factory.select(expressions, c));
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
        return queryable.getOrderedClass();
    }

    public List<PropertyMetaData> getMappingData()
    {
        return queryable.getMappingData(config);
    }

    public boolean isSingle()
    {
        return queryable.getSelect().isSingle();
    }

    public Class<?> getTargetClass()
    {
        return queryable.getSelect().getTarget();
    }

    public SqlQueryableExpression getQueryable()
    {
        return queryable;
    }

    public List<IncludeSet> getIncludeSets()
    {
        return includeSets;
    }
}
