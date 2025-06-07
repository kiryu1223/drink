package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlPivotExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTableRefExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.List;
import java.util.stream.Collectors;

public class SqlPivotExpression implements ISqlPivotExpression {
    private final ISqlExpression aggregationColumn;
    private final Class<?> aggregationType;
    private final ISqlColumnExpression transColumn;
    private final List<ISqlExpression> transColumnValues;
//    private final List<ISqlExpression> anotherColumns = new ArrayList<>();
    private final ISqlTableRefExpression tempRefExpression;
    private final ISqlTableRefExpression pivotRefExpression;

    public SqlPivotExpression(ISqlExpression aggregationColumn, Class<?> aggregationType, ISqlColumnExpression transColumn, List<ISqlExpression> transColumnValues, ISqlTableRefExpression tempRefExpression, ISqlTableRefExpression pivotRefExpression) {
        this.aggregationColumn = aggregationColumn;
        this.aggregationType = aggregationType;
        this.transColumn = transColumn;
        this.transColumnValues = transColumnValues;
        this.tempRefExpression = tempRefExpression;
        this.pivotRefExpression = pivotRefExpression;
    }

    @Override
    public ISqlExpression getAggregationColumn() {
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
    public List<ISqlExpression> getTransColumnValues() {
        return transColumnValues;
    }

//    @Override
//    public List<ISqlExpression> getAnotherColumns() {
//        return anotherColumns;
//    }

    @Override
    public ISqlTableRefExpression getTempRefExpression() {
        return tempRefExpression;
    }

    @Override
    public ISqlTableRefExpression getPivotRefExpression()
    {
        return pivotRefExpression;
    }

    // PIVOT ({aggregationColumn} FOR {transColumn} IN ({transColumnValues})) AS _pivot
    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        IDialect disambiguation = config.getDisambiguation();
        StringBuilder builder = new StringBuilder();
        builder.append("PIVOT (")
                .append(aggregationColumn.getSqlAndValue(config, values))
                .append(" FOR ")
                .append(transColumn.getSqlAndValue(config, values))
                .append(" IN ").append("(").append(transColumnValues.stream().map(e -> disambiguation.disambiguation(e.getSqlAndValue(config, values))).collect(Collectors.joining(","))).append(")")
                .append(") AS ")
                .append(disambiguation.disambiguationTableName(pivotRefExpression.getDisPlayName()));
        return builder.toString();
    }
}
