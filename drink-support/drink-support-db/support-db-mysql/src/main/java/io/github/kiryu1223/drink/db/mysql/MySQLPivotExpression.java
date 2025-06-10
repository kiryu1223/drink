package io.github.kiryu1223.drink.db.mysql;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.expression.impl.SqlPivotExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.transform.Transformer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public class MySQLPivotExpression extends SqlPivotExpression
{
    public MySQLPivotExpression(ISqlQueryableExpression queryableExpression, ISqlTemplateExpression aggregationColumn, Class<?> aggregationType, ISqlColumnExpression transColumn, Collection<Object> transColumnValues, ISqlTableRefExpression tempRefExpression, ISqlTableRefExpression pivotRefExpression)
    {
        super(queryableExpression, aggregationColumn, aggregationType, transColumn, transColumnValues, tempRefExpression, pivotRefExpression);
    }

    @Override
    public ISqlTemplateExpression createAggExpression(IConfig config, List<String> aggString, Object transColumnValue, ISqlExpression aggColumn) {
        // mysql也不支持filter子句[○･｀Д´･ ○]
        return ifStyle(config, aggString, transColumnValue, aggColumn);
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values)
    {
        // mysql不支持pivot
        return groupAggStyle(config, values);
    }
}
