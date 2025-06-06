package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SqlPivotExpression implements ISqlPivotExpression {
    private final ISqlExpression aggregationColumn;
    private final ISqlColumnExpression groupColumn;
    private final List<ISqlExpression> selectColumnValues;
    private final ISqlTableRefExpression tableRefExpression;

    public SqlPivotExpression(ISqlExpression aggregationColumn, ISqlColumnExpression groupColumn, List<ISqlExpression> selectColumnValues, ISqlTableRefExpression tableRefExpression) {
        this.aggregationColumn = aggregationColumn;
        this.groupColumn = groupColumn;
        this.selectColumnValues = selectColumnValues;
        this.tableRefExpression = tableRefExpression;
    }

    @Override
    public ISqlExpression getAggregationColumn() {
        return aggregationColumn;
    }

    @Override
    public ISqlColumnExpression getGroupColumn() {
        return groupColumn;
    }

    @Override
    public List<ISqlExpression> getSelectColumnValues() {
        return selectColumnValues;
    }

    @Override
    public ISqlTableRefExpression getTableRefExpression() {
        return tableRefExpression;
    }

    // PIVOT ({aggregationColumn} FOR {groupColumn} IN ({selectColumnValues})) AS _pivot
    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        StringBuilder builder = new StringBuilder();
        builder.append("PIVOT (")
                .append(aggregationColumn.getSqlAndValue(config, values))
                .append(" FOR ")
                .append(groupColumn.getSqlAndValue(config, values))
                .append(" IN ")
                .append("(" + selectColumnValues.stream().map(e -> e.getSqlAndValue(config, values)).collect(Collectors.joining(",")) + ")")
                .append(") AS ")
                .append(tableRefExpression.getSqlAndValue(config, values));
        return builder.toString();
    }
}
