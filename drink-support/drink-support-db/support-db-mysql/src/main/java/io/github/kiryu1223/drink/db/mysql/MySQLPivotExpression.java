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

    // SELECT {所选的字段} FROM (
    //      SELECT t.xx, SUM(If(t.xxx = xxx,t.aaa,0)) as xxx
    //      FROM (SELECT {所选的字段} FROM <table> WHERE ...) as t
    //      GROUP BY t.xx
    // ) as t
    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values)
    {
        return groupAggStyle(config, values);
    }
}
