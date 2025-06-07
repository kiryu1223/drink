/*
 * Copyright 2017-2024 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kiryu1223
 * @since 3.0
 */
public class SqlFromExpression implements ISqlFromExpression
{
    protected final ISqlTableExpression sqlTableExpression;
    protected final ISqlTableRefExpression tableRefExpression;
    protected final List<ISqlPivotExpression> pivotExpressions = new ArrayList<>();

    protected SqlFromExpression(ISqlTableExpression sqlTableExpression, ISqlTableRefExpression tableRefExpression)
    {
        this.sqlTableExpression = sqlTableExpression;
        this.tableRefExpression = tableRefExpression;
    }

    @Override
    public ISqlTableExpression getSqlTableExpression()
    {
        return sqlTableExpression;
    }

    @Override
    public ISqlTableRefExpression getTableRefExpression()
    {
        return tableRefExpression;
    }

    @Override
    public List<ISqlPivotExpression> getPivotExpressions()
    {
        return pivotExpressions;
    }

    @Override
    public String normalTable(IConfig config, List<SqlValue> values)
    {
        IDialect disambiguation = config.getDisambiguation();
        if (pivotExpressions.isEmpty())
        {
            StringBuilder tableBuilder = new StringBuilder();
            String tableName = sqlTableExpression.getSqlAndValue(config, values);
            tableBuilder.append(tableName);

            if (sqlTableExpression instanceof ISqlQueryableExpression)
            {
                tableBuilder.insert(0, "(");
                tableBuilder.append(")");
            }
            return "FROM " + tableBuilder + " AS " + disambiguation.disambiguation(tableRefExpression.getDisPlayName());

        }
        else
        {
            SqlExpressionFactory factory = config.getSqlExpressionFactory();
            ISqlPivotExpression pivotExpression = pivotExpressions.get(0);
            ISqlTableRefExpression pivotRef = pivotExpression.getPivotRefExpression();
            // 选择的临时目标表字段
            ISqlSelectExpression select = factory.select(sqlTableExpression.getMainTableClass(), pivotRef);
            // 转换后的额外字段
            for (ISqlExpression transColumnValue : pivotExpression.getTransColumnValues())
            {
                ISqlConstStringExpression columnValue = (ISqlConstStringExpression) transColumnValue;
                String columnName = columnValue.getString();
                ISqlDynamicColumnExpression dynamicColumn = factory.dynamicColumn(columnName, void.class, pivotRef);
                select.addColumn(dynamicColumn);
            }

            List<String> strings = new ArrayList<>(3);
            strings.add(select.getSqlAndValue(config, values));

            StringBuilder tableBuilder = new StringBuilder();
            String tableName = sqlTableExpression.getSqlAndValue(config, values);
            tableBuilder.append("(").append(tableName).append(")");

            String from = "FROM " + tableBuilder + " AS " + disambiguation.disambiguation(pivotExpression.getTempRefExpression().getDisPlayName());

            strings.add(from);
            strings.add(pivotExpression.getSqlAndValue(config, values));

            return "FROM (" + String.join(" ", strings) + ") AS " + disambiguation.disambiguation(tableRefExpression.getDisPlayName());
        }
    }

    @Override
    public String emptyTable(IConfig config, List<SqlValue> values)
    {
        return "";
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
    public String getSqlAndValue(IConfig config, List<SqlValue> values)
    {
        if (isEmptyTable(config))
        {
            return emptyTable(config, values);
        }
        else
        {
            return normalTable(config, values);
        }
    }
}
