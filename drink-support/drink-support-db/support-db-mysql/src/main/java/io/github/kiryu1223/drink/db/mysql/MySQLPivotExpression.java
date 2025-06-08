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
        for (Object transColumnValue : transColumnValues)
        {
            // AGG(CASE WHEN 转换的名称字段的值 = 目标值 THEN 转换的值字段的值 ELSE 0 END)
            ISqlTemplateExpression agg = factory.template(
                    aggString,
                    Collections.singletonList(transformer.If(
                            factory.binary(SqlOperator.EQ, transColumn, factory.value(transColumnValue)),
                            aggColumn,
                            factory.constString(0)
                    ))
            );
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
        for (ISqlExpression column : groupSelect.getColumns())
        {
            joiner.add(column.getSqlAndValue(config, values));
        }
        groupBuilder.append(joiner);

        return String.join(" ", selectBuilder, fromBuilder, groupBuilder);
    }
}
