package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SqlPivotExpression implements ISqlPivotExpression {
    protected final ISqlQueryableExpression queryableExpression;
    protected final ISqlTemplateExpression aggregationColumn;
    protected final Class<?> aggregationType;
    protected final ISqlColumnExpression transColumn;
    protected final Collection<Object> transColumnValues;
    protected final ISqlTableRefExpression tempRefExpression;
    protected final ISqlTableRefExpression pivotRefExpression;

    public SqlPivotExpression(ISqlQueryableExpression queryableExpression, ISqlTemplateExpression aggregationColumn, Class<?> aggregationType, ISqlColumnExpression transColumn, Collection<Object> transColumnValues, ISqlTableRefExpression tempRefExpression, ISqlTableRefExpression pivotRefExpression) {
        this.queryableExpression = queryableExpression;
        this.aggregationColumn = aggregationColumn;
        this.aggregationType = aggregationType;
        this.transColumn = transColumn;
        this.transColumnValues = transColumnValues;
        this.tempRefExpression = tempRefExpression;
        this.pivotRefExpression = pivotRefExpression;
    }

    @Override
    public ISqlQueryableExpression getQueryableExpression()
    {
        return queryableExpression;
    }

    @Override
    public ISqlTemplateExpression getAggregationColumn() {
        return aggregationColumn;
    }

    @Override
    public Class<?> getAggregationType()
    {
        return aggregationType;
    }

    @Override
    public ISqlColumnExpression getTransColumn() {
        return transColumn;
    }

    @Override
    public Collection<Object> getTransColumnValues() {
        return transColumnValues;
    }

    public ISqlTableRefExpression getTempRefExpression() {
        return tempRefExpression;
    }

    @Override
    public ISqlTableRefExpression getPivotRefExpression()
    {
        return pivotRefExpression;
    }

    // SELECT * FROM <table> WHERE ...

    // pivot()

    // SELECT {所选的字段} FROM (SELECT {所选的字段} FROM <table> WHERE ...)

    // mssql/oracle

    // SELECT {所选的字段} FROM (
    //      SELECT <pivot>.xx,<pivot>.aaa
    //      FROM (SELECT {所选的字段} FROM <table> WHERE ...)
    //      PIVOT (...) as <pivot>
    // ) as t

    // mysql/...

    // SELECT {所选的字段} FROM (
    //      SELECT t.xx, SUM(If(t.xxx = xxx,t.aaa,0)) as xxx
    //      FROM (SELECT {所选的字段} FROM <table> WHERE ...) as t
    //      GROUP BY t.xx
    // ) as t
    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {

        IDialect disambiguation = config.getDisambiguation();

        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        // 选择的临时目标表字段
        ISqlSelectExpression select = factory.select(getMainTableClass(), pivotRefExpression);
        // 转换后的额外字段
        for (Object transColumnValue : transColumnValues)
        {
            String columnName = transColumnValue.toString();
            ISqlDynamicColumnExpression dynamicColumn = factory.dynamicColumn(columnName, aggregationType, pivotRefExpression);
            select.addColumn(dynamicColumn);
        }

        List<String> strings = new ArrayList<>(3);
        strings.add(select.getSqlAndValue(config, values));

        StringBuilder tableBuilder = new StringBuilder();
        String tableName = queryableExpression.getSqlAndValue(config, values);
        tableBuilder.append("(").append(tableName).append(")");

        String from = "FROM " + tableBuilder + " AS " + disambiguation.disambiguation(tempRefExpression.getDisPlayName());

        strings.add(from);

        StringBuilder builder = new StringBuilder();
        builder.append("PIVOT (")
                .append(aggregationColumn.getSqlAndValue(config, values))
                .append(" FOR ")
                .append(transColumn.getSqlAndValue(config, values))
                .append(" IN ").append("(").append(transColumnValues.stream().map(e -> disambiguation.disambiguation(e.toString())).collect(Collectors.joining(","))).append(")")
                .append(") AS ")
                .append(disambiguation.disambiguationTableName(pivotRefExpression.getDisPlayName()));

        strings.add(builder.toString());

        return String.join(" ", strings);
    }

    @Override
    public Class<?> getMainTableClass()
    {
        return queryableExpression.getMainTableClass();
    }
}
