package io.github.kiryu1223.drink.db.h2;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.expression.impl.SqlPivotExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.Collection;
import java.util.List;

public class H2PivotExpression extends SqlPivotExpression {
    public H2PivotExpression(ISqlQueryableExpression queryableExpression, ISqlTemplateExpression aggregationColumn, Class<?> aggregationType, ISqlColumnExpression transColumn, Collection<Object> transColumnValues, ISqlTableRefExpression tempRefExpression, ISqlTableRefExpression pivotRefExpression) {
        super(queryableExpression, aggregationColumn, aggregationType, transColumn, transColumnValues, tempRefExpression, pivotRefExpression);
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        // h2不支持pivot
        return groupAggStyle(config,values);
    }
}
