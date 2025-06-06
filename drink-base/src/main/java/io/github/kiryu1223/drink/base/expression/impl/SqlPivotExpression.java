package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlPivotExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTableRefExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SqlPivotExpression implements ISqlPivotExpression {
    private final ISqlExpression aggregationColumn;
    private final ISqlColumnExpression transColumn;
    private final List<ISqlExpression> transColumnValues;
    private final List<ISqlExpression> anotherColumns = new ArrayList<>();
    private final ISqlTableRefExpression tableRefExpression;

    public SqlPivotExpression(ISqlExpression aggregationColumn, ISqlColumnExpression transColumn, List<ISqlExpression> transColumnValues, ISqlTableRefExpression tableRefExpression) {
        this.aggregationColumn = aggregationColumn;
        this.transColumn = transColumn;
        this.transColumnValues = transColumnValues;
        this.tableRefExpression = tableRefExpression;
    }

    @Override
    public ISqlExpression getAggregationColumn() {
        return aggregationColumn;
    }

    @Override
    public ISqlColumnExpression getTransColumn() {
        return transColumn;
    }

    @Override
    public List<ISqlExpression> getTransColumnValues() {
        return transColumnValues;
    }

    @Override
    public List<ISqlExpression> getAnotherColumns() {
        return anotherColumns;
    }

    @Override
    public ISqlTableRefExpression getTableRefExpression() {
        return tableRefExpression;
    }

    // PIVOT ({aggregationColumn} FOR {transColumn} IN ({transColumnValues})) AS _pivot
    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        StringBuilder builder = new StringBuilder();
        builder.append("PIVOT (")
                .append(aggregationColumn.getSqlAndValue(config, values))
                .append(" FOR ")
                .append(transColumn.getSqlAndValue(config, values))
                .append(" IN ")
                .append("(" + transColumnValues.stream().map(e -> e.getSqlAndValue(config, values)).collect(Collectors.joining(",")) + ")")
                .append(") AS ")
                .append(tableRefExpression.getSqlAndValue(config, values));
        return builder.toString();
    }
}
