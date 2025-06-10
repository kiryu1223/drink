package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.transform.Transformer;

import java.util.*;
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
    public ISqlQueryableExpression getQueryableExpression() {
        return queryableExpression;
    }

    @Override
    public ISqlTemplateExpression getAggregationColumn() {
        return aggregationColumn;
    }

    @Override
    public Class<?> getAggregationType() {
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

    @Override
    public ISqlTableRefExpression getTempRefExpression() {
        return tempRefExpression;
    }

    @Override
    public ISqlTableRefExpression getPivotRefExpression() {
        return pivotRefExpression;
    }

    // SELECT * FROM <table> WHERE ...
    // SELECT {所选的字段} FROM (
    //      SELECT <pivot>.xx,<pivot>.aaa
    //      FROM (SELECT {所选的字段} FROM <table> WHERE ...)
    //      PIVOT (...) as <pivot>
    // ) as t
    @Override
    public String pivotStyle(IConfig config, List<SqlValue> values) {
        IDialect dialect = config.getDisambiguation();
        SqlExpressionFactory factory = config.getSqlExpressionFactory();

        // 1. 构建 SELECT 部分
        ISqlSelectExpression select = factory.select(getMainTableClass(), pivotRefExpression);
        transColumnValues.stream()
                .map(o -> o.toString())
                .forEach(columnName ->
                        select.addColumn(factory.dynamicColumn(columnName, aggregationType, pivotRefExpression))
                );

        // 2. 构建 FROM 部分
        String tableName = queryableExpression.getSqlAndValue(config, values);
        String from = String.format(
                "FROM (%s) AS %s",
                tableName,
                dialect.disambiguation(tempRefExpression.getDisPlayName())
        );

        // 3. 构建 PIVOT 部分
        String pivot = String.format(
                "PIVOT (%s FOR %s IN (%s)) AS %s",
                aggregationColumn.getSqlAndValue(config, values),
                transColumn.getSqlAndValue(config, values),
                transColumnValues.stream()
                        .map(e -> dialect.disambiguation(e.toString()))
                        .collect(Collectors.joining(",")),
                dialect.disambiguationTableName(pivotRefExpression.getDisPlayName())
        );

        // 4. 合并 SQL 语句
        return String.join(" ",
                select.getSqlAndValue(config, values),
                from,
                pivot
        );
    }

    // SELECT * FROM <table> WHERE ...
    // SELECT {所选的字段} FROM (
    //      SELECT t.xx, SUM(If(t.xxx = xxx,t.aaa,0)) as xxx
    //      FROM (SELECT {所选的字段} FROM <table> WHERE ...) as t
    //      GROUP BY t.xx
    // ) as t
    @Override
    public String groupAggStyle(IConfig config, List<SqlValue> values) {
        IDialect disambiguation = config.getDisambiguation();
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        Transformer transformer = config.getTransformer();
        StringBuilder selectBuilder = new StringBuilder();
        StringBuilder fromBuilder = new StringBuilder();
        StringBuilder groupBuilder = new StringBuilder();

        // SELECT {选择生成的字段}
        ISqlSelectExpression select = factory.select(getMainTableClass(), tempRefExpression);
        List<String> aggString = aggregationColumn.getTemplateStrings();
        ISqlExpression aggColumn = aggregationColumn.getExpressions().get(0);
        for (Object transColumnValue : transColumnValues) {
            // AGG(CASE WHEN 转换的名称字段的值 = 目标值 THEN 转换的值字段的值 ELSE 0 END)
            ISqlTemplateExpression agg = createAggExpression(config, aggString, transColumnValue, aggColumn);
            // SELECT {选择生成的字段},{聚合函数...}
            select.addColumn(factory.as(agg, transColumnValue.toString()));
        }

        selectBuilder.append(select.getSqlAndValue(config, values));

        fromBuilder.append("FROM (")
                .append(queryableExpression.getSqlAndValue(config, values))
                .append(") AS ")
                .append(disambiguation.disambiguation(tempRefExpression.getDisPlayName()));

        ISqlSelectExpression groupSelect = factory.select(getMainTableClass(), tempRefExpression);
        groupBuilder.append("GROUP BY ");
        StringJoiner joiner = new StringJoiner(",");
        for (ISqlExpression column : groupSelect.getColumns()) {
            joiner.add(column.getSqlAndValue(config, values));
        }
        groupBuilder.append(joiner);

        return String.join(" ", selectBuilder, fromBuilder, groupBuilder);
    }

    @Override
    public ISqlTemplateExpression createAggExpression(IConfig config, List<String> aggString, Object transColumnValue, ISqlExpression aggColumn) {
        return filterStyle(config, aggString, transColumnValue, aggColumn);
    }

    @Override
    public ISqlTemplateExpression ifStyle(IConfig config, List<String> aggString, Object transColumnValue, ISqlExpression aggColumn) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        Transformer transformer = config.getTransformer();
        return factory.template(
                aggString,
                Collections.singletonList(transformer.If(
                        factory.binary(SqlOperator.EQ, transColumn, factory.value(transColumnValue)),
                        aggColumn,
                        factory.constString(0)
                ))
        );
    }

    @Override
    public ISqlTemplateExpression filterStyle(IConfig config, List<String> aggString, Object transColumnValue, ISqlExpression aggColumn) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> copy = new ArrayList<>(aggString);
        copy.set(1, ") FILTER (WHERE");
        copy.add(")");
        ISqlBinaryExpression condition = factory.binary(SqlOperator.EQ, transColumn, factory.value(transColumnValue));
        return factory.template(copy, Arrays.asList(aggColumn, condition));
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        return pivotStyle(config, values);
    }
}
