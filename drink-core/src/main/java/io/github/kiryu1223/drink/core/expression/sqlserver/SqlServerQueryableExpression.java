package io.github.kiryu1223.drink.core.expression.sqlserver;

import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.expression.impl.SqlQueryableExpression;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.metaData.MetaDataCache;
import io.github.kiryu1223.drink.base.metaData.PropertyMetaData;
import io.github.kiryu1223.drink.core.exception.DrinkLimitNotFoundOrderByException;

import java.util.ArrayList;
import java.util.List;

public class SqlServerQueryableExpression extends SqlQueryableExpression
{
    protected SqlServerQueryableExpression(ISqlSelectExpression select, ISqlFromExpression from, ISqlJoinsExpression joins, ISqlWhereExpression where, ISqlGroupByExpression groupBy, ISqlHavingExpression having, ISqlOrderByExpression orderBy, ISqlLimitExpression limit)
    {
        super(select, from, joins, where, groupBy, having, orderBy, limit);
    }

    @Override
    public String getSqlAndValue(IConfig config, List<Object> values)
    {
        List<String> strings = new ArrayList<>();
        makeSelect(strings, values, config);
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
        if (!from.isEmptyTable() && limit.hasRowsAndOffset() && orderBy.isEmpty())
        {
            addOrder(strings, values, config);
        }
        String limitSqlAndValue = limit.getSqlAndValue(config, values);
        if (!limitSqlAndValue.isEmpty()) strings.add(limitSqlAndValue);
        return String.join(" ", strings);
    }

    private void addOrder(List<String> strings, List<Object> values, IConfig config)
    {
        MetaData metaData = MetaDataCache.getMetaData(from.getSqlTableExpression().getTableClass());
        PropertyMetaData primary = metaData.getPrimary();
        if (primary == null)
        {
            throw new DrinkLimitNotFoundOrderByException(DbType.SQLServer);
        }
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        ISqlOrderByExpression sqlOrderByExpression = factory.orderBy();
        sqlOrderByExpression.addOrder(factory.order(factory.column(primary)));
        strings.add(sqlOrderByExpression.getSqlAndValue(config, values));
    }

    private void makeSelect(List<String> strings, List<Object> values, IConfig config)
    {
        List<String> result = new ArrayList<>();
        result.add("SELECT");
        if (select.isDistinct())
        {
            result.add("DISTINCT");
        }
        if (!from.isEmptyTable() && limit.onlyHasRows())
        {
            result.add("TOP(" + limit.getRows() + ")");
        }
        List<ISqlExpression> columns = select.getColumns();
        List<String> columnsStr = new ArrayList<>(columns.size());
        for (ISqlExpression sqlExpression : columns)
        {
            columnsStr.add(sqlExpression.getSqlAndValue(config, values));
        }
        result.add(String.join(",", columnsStr));
        strings.add(String.join(" ", result));
    }
}
