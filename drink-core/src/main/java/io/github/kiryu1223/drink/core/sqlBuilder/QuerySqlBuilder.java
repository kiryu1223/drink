/*
 * Copyright 2017-2024 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.kiryu1223.drink.core.sqlBuilder;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.annotation.RelationType;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.metaData.MetaDataCache;
import io.github.kiryu1223.drink.base.metaData.NavigateData;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.toBean.Include.IncludeSet;
import io.github.kiryu1223.drink.core.visitor.SqlVisitor;
import io.github.kiryu1223.expressionTree.expressions.LambdaExpression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.getFirst;

/**
 * @author kiryu1223
 * @since 3.0
 */
public class QuerySqlBuilder implements ISqlBuilder
{
    private final IConfig config;
    private ISqlQueryableExpression queryable;
    private final List<IncludeSet> includeSets = new ArrayList<>();
    private final List<String> ignoreFilterIds = new ArrayList<>();
    private boolean ignoreFilterAll = false;

    //    public QuerySqlBuilder(IConfig config, Class<?> target, int offset)
//    {
//        this(config, config.getSqlExpressionFactory().table(target), offset);
//    }
//
//    public QuerySqlBuilder(IConfig config, Class<?> target)
//    {
//        this(config, config.getSqlExpressionFactory().table(target), 0);
//    }
//
//    public QuerySqlBuilder(IConfig config, ISqlTableExpression target)
//    {
//        this(config, target, 0);
//    }
//
//    public QuerySqlBuilder(IConfig config, ISqlTableExpression target, int offset)
//    {
//        this.config = config;
//        SqlExpressionFactory factory = config.getSqlExpressionFactory();
//        queryable = factory.queryable(factory.from(target, offset));
//    }

    public QuerySqlBuilder(IConfig config, ISqlQueryableExpression queryable)
    {
        this.config = config;
        this.queryable = queryable;
    }

    public void addWhere(ISqlExpression cond)
    {
        queryable.addWhere(cond);
    }

    public void addOrWhere(ISqlExpression cond)
    {
        if (queryable.getWhere().isEmpty())
        {
            addWhere(cond);
        }
        else
        {
            SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
            addWhere(factory.unary(SqlOperator.OR, cond));
        }
    }

//    public void addJoin(JoinType joinType, ISqlTableExpression table) {
//        SqlExpressionFactory factory = config.getSqlExpressionFactory();
//        Set<String> stringSet = new HashSet<>(queryable.getJoins().getJoins().size() + 1);
//        stringSet.add(queryable.getFrom().getAsName().getName());
//        for (ISqlJoinExpression join : queryable.getJoins().getJoins()) {
//            stringSet.add(join.getAsName().getName());
//        }
//        String first = getFirst(table.getMainTableClass());
//        AsName asName = doGetAsName(first,stringSet);
//        ISqlJoinExpression join = factory.join(joinType, table, asName);
//        queryable.addJoin(join);
//    }

    public void setGroup(ISqlGroupByExpression group)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        queryable.setGroup(group);
    }

    public void addHaving(ISqlExpression cond)
    {
        queryable.addHaving(cond);
    }

    public void addOrder(ISqlOrderExpression order)
    {
        queryable.addOrder(order);
    }

    public void setSelect(ISqlSelectExpression select)
    {
        queryable.setSelect(select);
    }

    public void setSelect(Class<?> c)
    {
        MetaData metaData = MetaDataCache.getMetaData(c);
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        ISqlFromExpression from = queryable.getFrom();
        ISqlJoinsExpression joins = queryable.getJoins();
        List<ISqlExpression> expressions = new ArrayList<>();
        if (from.getSqlTableExpression().getMainTableClass() == c)
        {
            for (FieldMetaData notIgnoreProperty : metaData.getNotIgnorePropertys())
            {
                expressions.add(factory.column(notIgnoreProperty, from.getAsName()));
            }
        }
        else if (joins.getJoins().stream().anyMatch(join -> join.getJoinTable().getMainTableClass() == c))
        {
            for (ISqlJoinExpression join : joins.getJoins())
            {
                if (join.getJoinTable().getMainTableClass() == c)
                {
                    for (FieldMetaData notIgnoreProperty : metaData.getNotIgnorePropertys())
                    {
                        expressions.add(factory.column(notIgnoreProperty, join.getAsName()));
                    }
                    break;
                }
            }
        }
        else
        {
            GOTO:
            for (FieldMetaData sel : metaData.getNotIgnorePropertys())
            {
                MetaData mainTableMetaData = MetaDataCache.getMetaData(from.getSqlTableExpression().getMainTableClass());
                for (FieldMetaData noi : mainTableMetaData.getNotIgnorePropertys())
                {
                    if (noi.getColumn().equals(sel.getColumn()) && noi.getType().equals(sel.getType()))
                    {
                        expressions.add(factory.column(sel, from.getAsName()));
                        break GOTO;
                    }
                }
                for (ISqlJoinExpression join : joins.getJoins())
                {
                    MetaData joinTableMetaData = MetaDataCache.getMetaData(join.getJoinTable().getMainTableClass());
                    for (FieldMetaData noi : joinTableMetaData.getNotIgnorePropertys())
                    {
                        if (noi.getColumn().equals(sel.getColumn()) && noi.getType().equals(sel.getType()))
                        {
                            expressions.add(factory.column(sel, join.getAsName()));
                            break GOTO;
                        }
                    }
                }
            }
        }
        queryable.setSelect(factory.select(expressions, c));
    }

    public void setLimit(long offset, long rows)
    {
        queryable.setLimit(offset, rows);
    }

    public void setDistinct(boolean distinct)
    {
        queryable.setDistinct(distinct);
    }

    @Override
    public IConfig getConfig()
    {
        return config;
    }

    @Override
    public String getSql()
    {
        return tryFilter(queryable).getSql(config);
    }

    @Override
    public String getSqlAndValue(List<SqlValue> values)
    {
        return tryFilter(queryable).getSqlAndValue(config, values);
    }

//    public String getSqlAndValueAndFirst(List<Object> values)
//    {
//        if (isChanged)
//        {
//            return queryable.getSqlAndValueAndFirst(config, values);
//        }
//        else
//        {
//            SqlTableExpression sqlTableExpression = queryable.getFrom().getSqlTableExpression();
//            if (sqlTableExpression instanceof SqlRealTableExpression)
//            {
//                return queryable.getSqlAndValueAndFirst(config, values);
//            }
//            else
//            {
//                SqlQueryableExpression tableExpression = (SqlQueryableExpression) sqlTableExpression;
//                return tableExpression.getSqlAndValueAndFirst(config, values);
//            }
//        }
//    }

    public List<FieldMetaData> getMappingData()
    {
        return queryable.getMappingData();
    }

    public boolean isSingle()
    {
        return queryable.getSelect().isSingle();
    }

    public <T> Class<T> getTargetClass()
    {
        return (Class<T>) queryable.getMainTableClass();
    }

    public ISqlQueryableExpression getQueryable()
    {
        return queryable;
    }

    public void setQueryable(ISqlQueryableExpression queryable)
    {
        this.queryable = queryable;
    }

    public List<IncludeSet> getIncludeSets()
    {
        return includeSets;
    }

    public IncludeSet getLastIncludeSet()
    {
        return includeSets.get(includeSets.size() - 1);
    }

    @Override
    public void setIgnoreFilterAll(boolean ignoreFilterAll)
    {
        this.ignoreFilterAll = ignoreFilterAll;
    }

    @Override
    public List<String> getIgnoreFilterIds()
    {
        return ignoreFilterIds;
    }

    @Override
    public void addIgnoreFilterId(String filterId)
    {
        ignoreFilterIds.add(filterId);
    }

    @Override
    public boolean isIgnoreFilterAll()
    {
        return ignoreFilterAll;
    }

    public void boxed() {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        AsName asName = queryable.getFrom().getAsName();
        queryable = factory.queryable(queryable, new AsName(asName.getName()));
    }

    public QuerySqlBuilder getCopy() {
        QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(config, queryable.copy(config));
        querySqlBuilder.includeSets.addAll(includeSets);
        querySqlBuilder.ignoreFilterAll=ignoreFilterAll;
        querySqlBuilder.ignoreFilterIds.addAll(ignoreFilterIds);
        return querySqlBuilder;
    }
}
