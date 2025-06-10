package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class SqlUnPivotExpression implements ISqlUnPivotExpression
{
    protected final ISqlQueryableExpression queryableExpression;
    protected final String newNameColumnName;
    protected final String newValueColumnName;
    protected final Class<?> newValueColumnType;
    protected final List<ISqlColumnExpression> transColumns;
    protected final ISqlTableRefExpression tempRefExpression;
    protected final ISqlTableRefExpression unpivotRefExpression;


    public SqlUnPivotExpression(ISqlQueryableExpression queryableExpression, String newNameColumnName, String newValueColumnName, Class<?> newValueColumnType, List<ISqlColumnExpression> transColumns, ISqlTableRefExpression tempRefExpression, ISqlTableRefExpression unpivotRefExpression)
    {
        this.queryableExpression = queryableExpression;
        this.newNameColumnName = newNameColumnName;
        this.newValueColumnName = newValueColumnName;
        this.newValueColumnType = newValueColumnType;
        this.transColumns = transColumns;
        this.unpivotRefExpression = unpivotRefExpression;
        this.tempRefExpression = tempRefExpression;
    }

    @Override
    public ISqlQueryableExpression getQueryableExpression()
    {
        return queryableExpression;
    }

    @Override
    public String getNewNameColumnName()
    {
        return newNameColumnName;
    }

    @Override
    public String getNewValueColumnName()
    {
        return newValueColumnName;
    }

    @Override
    public Class<?> getNewValueColumnType()
    {
        return newValueColumnType;
    }

    @Override
    public List<ISqlColumnExpression> getTransColumns()
    {
        return transColumns;
    }

    @Override
    public ISqlTableRefExpression getUnpivotRefExpression()
    {
        return unpivotRefExpression;
    }

    @Override
    public ISqlTableRefExpression getTempRefExpression()
    {
        return tempRefExpression;
    }

    // SELECT *
    // FROM (
    //   SELECT [<unpivot>].*
    //   FROM <table> as t
    //   UNPIVOT [INCLUDE|EXCLUDE NULLS] (
    //   新值列名 FOR 新名列名
    //   IN (t.[列1],t.[列2],[...])
    //   ) as [<unpivot>]
    // )
    // WHERE ...
    @Override
    public String unPivotStyle(IConfig config, List<SqlValue> values)
    {
        IDialect disambiguation = config.getDisambiguation();
        SqlExpressionFactory factory = config.getSqlExpressionFactory();

        StringBuilder selectBuilder = new StringBuilder();
        StringBuilder fromBuilder = new StringBuilder();
        StringBuilder unPivotBuilder = new StringBuilder();

        // SELECT [<unpivot>].{new声明的字段}
        ISqlSelectExpression select = factory.select(getMainTableClass(), unpivotRefExpression);
        // SELECT [<unpivot>].{new声明的字段},[<unpivot>].{新名列名},[<unpivot>].{新值列名}
        select.addColumn(factory.dynamicColumn(newNameColumnName, String.class, unpivotRefExpression));
        select.addColumn(factory.dynamicColumn(newValueColumnName, newValueColumnType, unpivotRefExpression));

        selectBuilder.append(select.getSqlAndValue(config, values));

        fromBuilder.append("FROM (")
                .append(queryableExpression.getSqlAndValue(config, values))
                .append(") AS ")
                .append(disambiguation.disambiguation(tempRefExpression.getDisPlayName()));

        unPivotBuilder.append("UNPIVOT (")
                .append(disambiguation.disambiguation(newValueColumnName))
                .append(" FOR ")
                .append(disambiguation.disambiguation(newNameColumnName))
                .append(" IN (");

        StringJoiner joiner = new StringJoiner(",");
        for (ISqlColumnExpression column : transColumns)
        {
            joiner.add(column.getSqlAndValue(config, values));
        }
        unPivotBuilder.append(joiner)
                .append(")) AS ")
                .append(disambiguation.disambiguation(unpivotRefExpression.getDisPlayName()));

        return String.join(" ", selectBuilder, fromBuilder, unPivotBuilder);
    }

    // SELECT *
    // FROM (
    //      SELECT {new},'trans1' as nameColumn,temp.trans1 as valueColumn FROM <table> as temp
    //      UNION ALL
    //      SELECT {new},'trans2' as nameColumn,temp.trans2 as valueColumn FROM <table> as temp
    //      ...
    // )
    // WHERE ...
    @Override
    public String unionStyle(IConfig config, List<SqlValue> values)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();

        List<ISqlQueryableExpression> unionQueryable = new ArrayList<>();
        for (ISqlColumnExpression transColumn : transColumns)
        {
            ISqlQueryableExpression tempQuery = factory.queryable(queryableExpression, tempRefExpression);
            ISqlSelectExpression select = tempQuery.getSelect();
            String transColumnName = transColumn.getFieldMetaData().getColumn();
            ISqlAsExpression name = factory.as(factory.constString("'" + transColumnName + "'"), newNameColumnName);
            ISqlAsExpression value = factory.as(transColumn, newValueColumnName);
            select.addColumn(name);
            select.addColumn(value);
            unionQueryable.add(tempQuery);
        }

        ISqlUnionQueryableExpression union = factory.unionQueryable(unionQueryable, true);

        return union.getSqlAndValue(config, values);
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values)
    {
        return unPivotStyle(config, values);
    }
}
