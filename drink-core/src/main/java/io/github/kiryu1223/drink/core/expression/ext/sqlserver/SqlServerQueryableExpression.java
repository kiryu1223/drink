package io.github.kiryu1223.drink.core.expression.ext.sqlserver;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlFromExpression;
import io.github.kiryu1223.drink.core.expression.SqlQueryableExpression;

import java.util.ArrayList;
import java.util.List;

public class SqlServerQueryableExpression extends SqlQueryableExpression
{
    protected SqlServerQueryableExpression(Config config, SqlFromExpression from)
    {
        super(config, from);
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        List<String> strings = new ArrayList<>();
        strings.add(select.getSqlAndValue(config, values));
        if (distinct)
        {
            strings.add("DISTINCT");
        }
        checkLimit(strings);
        String fromSqlAndValue = from.getSqlAndValue(config, values);
        if (!fromSqlAndValue.isEmpty()) strings.add(fromSqlAndValue);
        String joinsSqlAndValue = joins.getSqlAndValue(config, values);
        if (!joinsSqlAndValue.isEmpty()) strings.add(joinsSqlAndValue);
        String whereSqlAndValue = where.getSqlAndValue(config, values);
        if (!whereSqlAndValue.isEmpty()) strings.add(whereSqlAndValue);
        String groupBySqlAndValue = groupBy.getSqlAndValue(config, values);
        if (!groupBySqlAndValue.isEmpty()) strings.add(groupBySqlAndValue);
        String havingSqlAndValue = having.getSqlAndValue(config, values);
        if (!havingSqlAndValue.isEmpty()) strings.add(havingSqlAndValue);
        String orderBySqlAndValue = orderBy.getSqlAndValue(config, values);
        if (!orderBySqlAndValue.isEmpty()) strings.add(orderBySqlAndValue);
        return String.join(" ", strings);
    }


    @Override
    public String getSql(Config config)
    {
        return super.getSql(config);
    }

    private void checkLimit(List<String> strings)
    {
        if (limit.getRows() > 0)
        {
            if (limit.getOffset() <= 0)
            {
                strings.add("TOP(" + limit.getRows() + ")");
            }
            else
            {

            }
        }
    }
}
