package io.github.kiryu1223.drink.db.sqlite;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.expression.ISqlQueryableExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTableRefExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTemplateExpression;
import io.github.kiryu1223.drink.base.expression.impl.SqlPivotExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.Collection;
import java.util.List;

public class SQLitePivotExpression extends SqlPivotExpression {
    public SQLitePivotExpression(ISqlQueryableExpression queryableExpression, ISqlTemplateExpression aggregationColumn, Class<?> aggregationType, ISqlColumnExpression transColumn, Collection<Object> transColumnValues, ISqlTableRefExpression tempRefExpression, ISqlTableRefExpression pivotRefExpression) {
        super(queryableExpression, aggregationColumn, aggregationType, transColumn, transColumnValues, tempRefExpression, pivotRefExpression);
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        return groupAggStyle(config, values);
    }
}
